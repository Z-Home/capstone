package connect

import (
	"encoding/json"
	"fmt"
	"net/http"
	"os"
)

type Command struct {
	Device       string
	CommandClass string
	Command      string
}

func (command *Command) ReadCommand(line string) {
	var c Command
	json.Unmarshal([]byte(line), &c)

	command.SendCommand(c)
}

func (command *Command) SendCommand(c Command) {
	a := fmt.Sprintf("http://192.168.0.17:8083/ZWaveAPI/Run/devices[%s].instances[0].commandClasses[%s].Set(%s)", c.Device, c.CommandClass, c.Command)

	response, err := http.Get(a)
	if err != nil {
		fmt.Printf("%s", err)
		os.Exit(1)
	}
	response.Body.Close()
}
