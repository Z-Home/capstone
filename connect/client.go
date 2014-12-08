package connect

import (
	"bufio"
	"fmt"
	"github.com/jeffail/gabs"
	"net"
	"strings"
)

type Client struct {
	incoming        chan string
	outgoing        chan string
	outgoingDevices chan string
	reader          *bufio.Reader
	conn            net.Conn
	dead            chan *Client
	auth            bool
	authChan        chan string
	admin           bool
	zhome           *ZHome
	devices         []string
}

//Read incoming info from server
//if not authenticated send info to auth channel
//else send to ReadCommand
func (client *Client) Read() {
	for {
		line, err := client.reader.ReadString('\n')
		if err != nil {
			break
		}

		if client.auth == false {
			client.authChan <- line
		} else {
			go ReadCommand(line, client)
		}
	}
	client.dead <- client
}

//Send devices the user has access to
func (client *Client) WriteDevices() {
	for data := range client.outgoingDevices {
		data = fmt.Sprintf("%s\n", SetDevicesAccess(data, client.devices))
		_, err := client.conn.Write([]byte(data))
		if err != nil {
			client.dead <- client
		}
	}
}

//Set the devices the user has access to
func SetDevicesAccess(data string, devices []string) string {
	t, _ := gabs.ParseJSON([]byte(data))
	rm, _ := t.S("Message", "devices").Children()

	jsonObj, _ := gabs.Consume(map[string]interface{}{})
	jsonObj.Set(1, "Type")

	for _, val := range rm {
		for _, v := range devices {
			if v == val.Path("devNumber").Data().(string) {
				de := val.String()
				jsonObj.Set(de, "Message", "devices", v)
			}
		}
	}

	d := jsonObj.String()
	d = strings.Replace(d, "\\\"", "\"", -1)
	d = strings.Replace(d, "\\\\", "\\", -1)
	d = strings.Replace(d, "\"{\"", "{\"", -1)
	d = strings.Replace(d, "\"}\"", "\"}", -1)
	d = strings.Replace(d, "\\\"}},\"devName\"", "\\\"}\"},\"devName\"", -1)

	return d
}

//Send info to the client
func (client *Client) Write() {
	for data := range client.outgoing {
		_, err := client.conn.Write([]byte(data))
		if err != nil {
			client.dead <- client
		}
	}
}

func (client *Client) Listen() {
	go client.Read()
	go client.Write()
	go client.WriteDevices()
}

func NewClient(connection net.Conn) *Client {
	reader := bufio.NewReader(connection)

	client := &Client{
		incoming:        make(chan string),
		outgoing:        make(chan string),
		outgoingDevices: make(chan string),
		reader:          reader,
		conn:            connection,
		dead:            make(chan *Client),
		auth:            false,
		authChan:        make(chan string),
		admin:           false,
	}

	client.Listen()

	return client
}
