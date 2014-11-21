package connect

import (
	"fmt"
	"github.com/jeffail/gabs"
	"net/http"
	"strings"
)

func ReadCommand(line string, client *Client) {
	admin := client.admin

	t, err := gabs.ParseJSON([]byte(line))
	if err != nil {
		fmt.Println(err)
	} else {
		de := t.Path("Json").String()
		if d := strings.Contains(de, "\\"); d {
			de = strings.Replace(de, "\\", "", -1)
		}

		if string(de[0]) == `"` {
			de = de[1 : len(de)-1]
		}

		ty := t.Path("Type").String()
		ty = strings.Replace(ty, "\"", "", -1)

		devJson, err := gabs.ParseJSON([]byte(de))
		if err != nil {
			fmt.Println(err)
		} else {
			switch ty {
			case "Command":
				dev := devJson.Path("device").String()
				dev = strings.Replace(dev, "\"", "", -1)

				cc := devJson.Path("commandClass").String()
				cc = strings.Replace(cc, "\"", "", -1)

				c := devJson.Path("command").String()
				c = strings.Replace(c, "\"", "", -1)

				url := fmt.Sprintf("http://%s:8083/ZWaveAPI/Run/devices[%s].instances[0].commandClasses[%s].Set(%s)", IP_ADDRESS, dev, cc, c)
				SendCommand(url)
			case "NewUser":
				if admin {
					user := devJson.Path("User").String()
					user = strings.Replace(user, "\"", "", -1)

					pass := devJson.Path("Password").String()
					pass = strings.Replace(pass, "\"", "", -1)

					ad := devJson.Path("Admin").String()
					ad = strings.Replace(ad, "\"", "", -1)

					dev := devJson.Path("Devices").Data().(string)

					NewUser(user, pass, ad, dev)
				}
			case "DeviceList":

				UpdateDevList("value", devJson.String())

				jsonParsed, _ := gabs.ParseJSON([]byte(client.zhome.deviceList))
				children, _ := jsonParsed.S("devices").Children()

				dev, _ := devJson.S("devices").Children()

				for _, child := range dev {
					dbNum := child.Path("Num").String()
					dbNum = strings.Replace(dbNum, "\"", "", -1)
					dbName := child.Path("Name").String()
					for _, val := range children {
						temp := val.Path("devNumber").String()
						temp = strings.Replace(temp, "\"", "", -1)

						if dbNum == temp {
							tempName := strings.Replace(dbName, "\"", "", -1)
							jsonParsed.Set(tempName, "devices", dbNum, "devName")
						}
					}
				}

				client.zhome.deviceList = jsonParsed.String()
				y := fmt.Sprintf("{\"Message\":%s}\n", client.zhome.deviceList)
				client.zhome.BroadcastDevices(y)
			case "Rooms":
				if admin {
					UpdateDevList("rooms", devJson.String())
					client.zhome.rooms = devJson.String()
					x := fmt.Sprintf("{\"Type\":6,\"Message\":%s}\n", client.zhome.rooms)
					client.zhome.Broadcast(x)
				}
			}
		}
	}
}

func SendCommand(url string) {
	http.Get(url)
}
