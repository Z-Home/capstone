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
		url := fmt.Sprintf("http://192.168.0.17:8083/ZWaveAPI/Run/devices[%s].instances[0].commandClasses[%s].Set(%s)", c["Device"].(string), c["CommandClass"].(string), c["Command"].(string))
		SendCommand(url)
	case "NewUser":
		if admin {
			fmt.Println("NEWUSER")

			NewUser(c["User"].(string), c["Password"].(string))
		}
	}
}

func SendCommand(url string) {
	http.Get(url)
}
