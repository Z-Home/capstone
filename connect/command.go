package connect

import (
	"fmt"
	"github.com/jeffail/gabs"
	"net/http"
	"strings"
)

func ReadCommand(line string, admin bool) {

	t, _ := gabs.ParseJSON([]byte(line))

	de := t.Path("Json").String()
	if d := strings.Contains(de, "\\"); d {
		de = strings.Replace(de, "\\", "", -1)
	}

	if string(de[0]) == `"` {
		de = de[1 : len(de)-1]
	}

	ty := t.Path("Type").String()
	ty = strings.Replace(ty, "\"", "", -1)

	devJson, _ := gabs.ParseJSON([]byte(de))
	//fmt.Println(devJson.String())

	switch ty {
	case "Command":
		dev := devJson.Path("device").String()
		dev = strings.Replace(dev, "\"", "", -1)

		cc := devJson.Path("commandClass").String()
		cc = strings.Replace(cc, "\"", "", -1)

		c := devJson.Path("command").String()
		c = strings.Replace(c, "\"", "", -1)
		//fmt.Println(dev, cc, c)
		url := fmt.Sprintf("http://%s:8083/ZWaveAPI/Run/devices[%s].instances[0].commandClasses[%s].Set(%s)", IP_ADDRESS, dev, cc, c)
		SendCommand(url)
	case "NewUser":
		if admin {
			user := devJson.Path("User").String()
			user = strings.Replace(user, "\"", "", -1)

			pass := devJson.Path("Password").String()
			pass = strings.Replace(pass, "\"", "", -1)

			NewUser(user, pass)
			//NewUser(c["User"].(string), c["Password"].(string))
		}
	case "DeviceList":
		jsonParsed, _ := gabs.ParseJSON([]byte(deviceList))
		children, _ := jsonParsed.S("devices").Children()

		dev, _ := devJson.S("devices").Children()

		for _, child := range dev {
			dbNum := child.Path("Num").String()
			dbNum = strings.Replace(dbNum, "\"", "", -1)
			dbName := child.Path("Name").String()
			dbName = strings.Replace(dbName, "\"", "", -1)

			for _, val := range children {
				temp := val.Path("devNumber").String()
				temp = strings.Replace(temp, "\"", "", -1)
				fmt.Println(dbNum, temp)
				if dbNum == temp {
					fmt.Println("true")
					tempName := strings.Replace(dbName, "\"", "", -1)
					jsonParsed.Set(tempName, "devices", dbNum, "devName")
				}
			}
		}

		l, _ := jsonParsed.S("devices").Children()

		x := `{"devices":[`
		for _, e := range l {
			tempName := e.Path("devName").String()
			tempNum := e.Path("devNumber").String()
			x = fmt.Sprintf("%s{\"Num\":%s,\"Name\":%s},", x, tempNum, tempName)
		}
		x = strings.TrimSuffix(x, ",")
		x = fmt.Sprintf(`%s]}`, x)

		UpdateDevList(x)
		deviceList = jsonParsed.String()
	}
}

func SendCommand(url string) {
	http.Get(url)
}
