package connect

import (
	"encoding/json"
	"fmt"
	"net/http"
)

func ReadCommand(line string, admin bool) {
	t := make(map[string]interface{})
	json.Unmarshal([]byte(line), &t)

	c := make(map[string]interface{})
	json.Unmarshal([]byte(t["Json"].(string)), &c)

	switch t["Type"].(string) {
	case "Command":
		url := fmt.Sprintf("http://%s:8083/ZWaveAPI/Run/devices[%s].instances[0].commandClasses[%s].Set(%s)", IP_ADDRESS, c["device"].(string), c["commandClass"].(string), c["command"].(string))
		SendCommand(url)
	case "NewUser":
		if admin {
			NewUser(c["User"].(string), c["Password"].(string))
		}
	case "Test":
		x := c["heat"].(string)
		fmt.Print(x)
	}
}

func SendCommand(url string) {
	http.Get(url)
}
