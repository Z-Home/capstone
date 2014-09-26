package connect

import (
	"bufio"
	"net"
)

type Client struct {
	incoming chan string
	outgoing chan string
	reader   *bufio.Reader
	conn     net.Conn
	dead     chan *Client
}

func (client *Client) Read() {
	command := &Command{}
	for {
		line, err := client.reader.ReadString('\n')
		if err != nil {
			break
		}

		command.ReadCommand(line)
	}
	client.dead <- client
}

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
}

func NewClient(connection net.Conn) *Client {
	reader := bufio.NewReader(connection)

	client := &Client{
		incoming: make(chan string),
		outgoing: make(chan string),
		reader:   reader,
		conn:     connection,
		dead:     make(chan *Client),
	}

	client.Listen()

	return client
}
