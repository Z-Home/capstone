package connect

import (
	"encoding/json"
	"fmt"
	"github.com/jeffail/gabs"
	"io/ioutil"
	"net"
	"net/http"
	"strconv"
	"strings"
	"time"
)

const (
	IP_ADDRESS = "192.168.0.17"
)

type ZHome struct {
	clients    map[net.Conn]*Client
	joins      chan net.Conn
	incoming   chan string
	outgoing   chan string
	dead       chan *Client
	devices    []*Devices
	deviceList string
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
	joined := make(chan bool)
	go zHome.Auth(client, joined)

	go func() {
		select {
		case <-joined:
			client.outgoing <- fmt.Sprintf("%s\n", zHome.deviceList)
			if client.admin {
				client.outgoing <- "ADMIN\n"
			}
			client.auth = true
			zHome.clients[connection] = client
			go func() {
				for {
					zHome.dead <- <-client.dead
				}
			}()
		}
	}()
}

func (zHome *ZHome) Auth(client *Client, joined chan bool) {
	client.outgoing <- "User must authenticate\n"
	go func() {
		for {
			select {
			case line := <-client.authChan:
				if check := CheckPass(line); check == 1 {
					client.admin = true
					joined <- true
				} else if check == 2 {
					joined <- true
				} else if check == 3 {
					client.outgoing <- "incorect login\n"
				}
			}
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
					for v, _ := range val.level {
						x := strconv.Itoa(key + 1)
						url := fmt.Sprintf("http://%s:8083/ZWaveAPI/Run/devices[%s].instances[0].commandClasses[%s].Get()", IP_ADDRESS, x, v)
						res, err := httpClient.Get(url)
						if err != nil {
							break
						}
						res.Body.Close()
					}
				}
			}()

			response, err := httpClient.Get(fmt.Sprintf("http://%s:8083/ZWaveAPI/Data/0", IP_ADDRESS))
			if err != nil {
				break
			}
			contents, _ := ioutil.ReadAll(response.Body)
			response.Body.Close()

			jsonParsed, _ := gabs.ParseJSON(contents)

			go func() {
				var check bool
				for _, val := range zHome.devices {
					for v, lev := range val.level {
						x := val.deviceNum
						var value string
						check = false
						switch v {
						case "37", "38":
							path := fmt.Sprintf("devices.%s.instances.0.commandClasses.%s.data.level.value", x, v)
							value = jsonParsed.Path(path).String()
							check = true
						case "48":
							path := fmt.Sprintf("devices.%s.instances.0.commandClasses.%s.data.1.level.value", x, v)
							value = jsonParsed.Path(path).String()
							check = true
						case "49":
							j, _ := gabs.ParseJSON([]byte(lev))
							t := j.Path("sensors").String()

							c := make(map[string]interface{})
							json.Unmarshal([]byte(t), &c)

							path := fmt.Sprintf("devices.%s.instances.0.commandClasses.%s", x, v)
							temp := jsonParsed.Path(path)
							child, _ := temp.S("data").Children()

							q := make(map[string]string)
							for _, v := range child {
								if yes := v.Path("sensorTypeString.value").String(); yes != "{}" {
									sensorType := yes
									sensorType = strings.Replace(sensorType, "\"", "", -1)

									sensorValue := v.Path("val.value").String()

									q[sensorType] = sensorValue
								}
							}

							change := false
							for o, p := range q {
								for a, b := range c {
									if o == a {
										if p != b {
											change = true
										}
									}
								}
							}

							if change {
								str := `{"sensors":{`
								for o, p := range q {
									for a, _ := range c {
										if o == a {
											str = fmt.Sprintf(`%s"%s":"%s",`, str, a, p)
										}
									}
								}
								str = strings.TrimSuffix(str, ",")
								str = fmt.Sprintf(`%s}}`, str)

								value = str
							} else {
								value = lev
							}
							check = true
						case "66":
							path := fmt.Sprintf("devices.%s.instances.0.commandClasses.%s.data.state.value", x, v)
							value = jsonParsed.Path(path).String()
							check = true
						case "67":
							c := make(map[string]interface{})
							json.Unmarshal([]byte(lev), &c)

							path := fmt.Sprintf("devices.%s.instances.0.commandClasses.%s.data.1.val.value", x, v)
							one := jsonParsed.Path(path).String()

							path = fmt.Sprintf("devices.%s.instances.0.commandClasses.%s.data.2.val.value", x, v)
							two := jsonParsed.Path(path).String()

							if c["heat"] != one || c["cool"] != two {
								value = fmt.Sprintf(`{"heat":"%s","cool":"%s"}`, one, two)
							} else {
								value = lev
							}

							check = true
						}

						if value != lev && check == true {
							val.level[v] = value

							jsonObj, _ := gabs.Consume(map[string]interface{}{})
							jsonObj.Set(val.level[v], "update", x, v)
							zHome.NewIncoming(fmt.Sprintf("%s\n", jsonObj.String()))

							devList, _ := gabs.ParseJSON([]byte(zHome.deviceList))
							devList.Set(val.level[v], "devices", x, "commandClasses", v)
							zHome.deviceList = devList.String()
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
	response, _ := http.Get(fmt.Sprintf("http://%s:8083/ZWaveAPI/Data/0", IP_ADDRESS))
	contents, _ := ioutil.ReadAll(response.Body)
	response.Body.Close()

	jsonParsed, _ := gabs.ParseJSON(contents)

	children, _ := jsonParsed.S("devices").Children()

	for _, value := range children {
		devices := &Devices{}

		j := value.Path("instances.0.commandClasses").String()

		c := make(map[string]interface{})

		json.Unmarshal([]byte(j), &c)

		devices.commandClasses = make([]string, len(c))
		devices.level = make(map[string]string)

		i := 0
		var path string
		var check bool
		add := false

		for s, _ := range c {
			check = false
			devices.commandClasses[i] = s
			switch s {
			case "37", "38":
				path = fmt.Sprintf("instances.0.commandClasses.%s.data.level.value", s)
				check = true
			case "48":
				path = fmt.Sprintf("instances.0.commandClasses.%s.data.1.level.value", s)
				check = true
			case "49":
				path = fmt.Sprintf("instances.0.commandClasses.%s", s)
				temp := value.Path(path)
				child, _ := temp.S("data").Children()
				sensors := make([]string, 0)
				for _, v := range child {
					if yes := v.Path("sensorTypeString.value").String(); yes != "{}" {
						sensorType := yes
						sensorType = strings.Replace(sensorType, "\"", "", -1)

						sensorValue := v.Path("val.value").String()

						x := fmt.Sprintf(`"%s":"%s"`, sensorType, sensorValue)

						sensors = append(sensors, x)
					}
				}
				x := `{"sensors":{`
				for _, sens := range sensors {
					x = fmt.Sprintf("%s%s,", x, sens)
				}
				x = strings.TrimSuffix(x, ",")
				x = fmt.Sprintf(`%s}}`, x)

				devices.level[s] = x
				add = true
			case "66":
				path = fmt.Sprintf("instances.0.commandClasses.%s.data.state.value", s)
				check = true
			case "67":
				path = fmt.Sprintf("instances.0.commandClasses.%s.data.1.val.value", s)
				one := value.Path(path).String()

				path = fmt.Sprintf("instances.0.commandClasses.%s.data.2.val.value", s)
				two := value.Path(path).String()

				devices.level[s] = fmt.Sprintf(`{"heat":"%s","cool":"%s"}`, one, two)
				add = true
			}

			if check {
				if ok := value.Path(path).String(); ok != "{}" {
					devices.level[s] = ok
					add = true
				}
				check = false
			}
			i++
		}

		if add {
			temp := value.Path("data.deviceTypeString.value").String()
			temp = strings.Replace(temp, "\"", "", -1)
			devices.deviceType = temp
			temp = value.Path("data.name").String()
			num := strings.Split(temp, ".")
			devices.deviceNum = num[1]
			zHome.devices = append(zHome.devices, devices)
		}
	}

	jsonObj, _ := gabs.Consume(map[string]interface{}{})
	for _, value := range zHome.devices {
		for key, val := range value.level {
			jsonObj.Set(value.deviceType, "devices", value.deviceNum, "type")
			jsonObj.Set(val, "devices", value.deviceNum, "commandClasses", key)
		}
	}

	zHome.deviceList = jsonObj.String()
	fmt.Println(zHome.deviceList)
	fmt.Println("Got Devices")
	go zHome.Ticker()

}

func NewZHome() *ZHome {
	StartDB()

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
