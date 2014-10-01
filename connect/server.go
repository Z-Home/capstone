package connect

import (
	"encoding/json"
	"fmt"
	"github.com/jeffail/gabs"
	"io/ioutil"
	"net"
	"net/http"
	"strconv"
	"time"
)

type ZHome struct {
	clients  map[net.Conn]*Client
	joins    chan net.Conn
	incoming chan string
	outgoing chan string
	dead     chan *Client
	devices  []*Devices
}

type Devices struct {
	deviceType     string
	deviceNum      string
	commandClasses []string
	level          map[string]string
}

func (zHome *ZHome) Broadcast(data string) {
	for _, client := range zHome.clients {
		client.outgoing <- data
	}
}

func (zHome *ZHome) Join(connection net.Conn) {
	client := NewClient(connection)
	zHome.clients[connection] = client
	client.outgoing <- "hello\n"
	go func() {
		for {
			zHome.dead <- <-client.dead
		}
	}()
}

func (zHome *ZHome) Listen() {
	go func() {
		for {
			select {
			case data := <-zHome.incoming:
				zHome.Broadcast(data)
			case conn := <-zHome.joins:
				zHome.Join(conn)
			case client := <-zHome.dead:
				delete(zHome.clients, client.conn)
			}
		}
	}()
}

func (zHome *ZHome) NewConn(con net.Conn) {
	zHome.joins <- con
}

func (zHome *ZHome) NewIncoming(data string) {
	zHome.incoming <- data
}

func dialTimeout(network, addr string) (net.Conn, error) {
	var timeout = time.Duration(2 * time.Second)
	return net.DialTimeout(network, addr, timeout)
}

func (zHome *ZHome) Ticker() {
	ticker := time.NewTicker(5 * time.Second)
	quit := make(chan struct{})

	transport := http.Transport{
		Dial: dialTimeout,
	}

	httpClient := http.Client{
		Transport: &transport,
	}

	for {
		select {
		case <-ticker.C:
			go func() {
				for key, val := range zHome.devices {
					for _, v := range val.commandClasses {
						if _, ok := val.level[v]; ok {
							x := strconv.Itoa(key + 1)
							url := fmt.Sprintf("http://192.168.0.17:8083/ZWaveAPI/Run/devices[%s].instances[0].commandClasses[%s].Get()", x, v)
							res, err := httpClient.Get(url)
							if err != nil {
								break
							}
							res.Body.Close()
						}
					}
				}
			}()

			response, err := httpClient.Get("http://192.168.0.17:8083/ZWaveAPI/Data/0")
			if err != nil {
				break
			}
			contents, _ := ioutil.ReadAll(response.Body)
			response.Body.Close()

			jsonParsed, _ := gabs.ParseJSON(contents)

			go func() {
				for key, val := range zHome.devices {
					for _, v := range val.commandClasses {
						x := strconv.Itoa(key + 1)
						if _, ok := val.level[v]; ok {
							path := fmt.Sprintf("devices.%s.instances.0.commandClasses.%s.data.level.value", x, v)
							value := jsonParsed.Path(path).String()
							if val.level[v] != value {
								fmt.Printf("Device %s command class %s set to %s\n", x, v, value)
								zHome.NewIncoming(fmt.Sprintf("Device %s command class %s set to %s\n", x, v, value))

								val.level[v] = value
							}
						}
					}
				}
			}()

		case <-quit:
			ticker.Stop()
			return
		}
	}
}

func (zHome *ZHome) GetDevices() {
	response, _ := http.Get("http://192.168.0.17:8083/ZWaveAPI/Data/0")
	contents, _ := ioutil.ReadAll(response.Body)
	response.Body.Close()

	jsonParsed, _ := gabs.ParseJSON(contents)

	children, _ := jsonParsed.S("devices").Children()

	for key, value := range children {
		devices := &Devices{}

		devices.deviceType = value.Path("data.deviceTypeString.value").String()
		devices.deviceNum = strconv.Itoa(key + 1)

		j := value.Path("instances.0.commandClasses").String()

		c := make(map[string]interface{})

		json.Unmarshal([]byte(j), &c)

		devices.commandClasses = make([]string, len(c))
		devices.level = make(map[string]string)

		i := 0
		for s, _ := range c {
			devices.commandClasses[i] = s
			path := fmt.Sprintf("instances.0.commandClasses.%s.data.level.value", s)
			if ok := value.Path(path).String(); ok != "{}" {
				devices.level[s] = ok
			}
			i++
		}

		zHome.devices = append(zHome.devices, devices)
	}
	fmt.Println("Got Devices")
	go zHome.Ticker()

}

func NewZHome() *ZHome {
	zHome := &ZHome{
		clients:  make(map[net.Conn]*Client),
		joins:    make(chan net.Conn),
		incoming: make(chan string),
		outgoing: make(chan string),
		dead:     make(chan *Client),
		devices:  make([]*Devices, 0),
	}

	zHome.GetDevices()
	zHome.Listen()
	fmt.Println("Running")
	return zHome
}
