package connect

import (
	"encoding/json"
	"fmt"
	"net/http"
)

type Command struct {
	Device       string
	CommandClass string
	Command      string
}

func (command *Command) ReadCommand(line string) {
	var c Command
	json.Unmarshal([]byte(line), &c)

	url := fmt.Sprintf("http://192.168.0.17:8083/ZWaveAPI/Run/devices[%s].instances[0].commandClasses[%s].Set(%s)", c.Device, c.CommandClass, c.Command)
	command.SendCommand(url)
}

func (command *Command) SendCommand(url string) {
	http.Get(url)
}
