package main

import (
	"net"

	"capstone/connect"
	//"github.com/davecheney/profile"
)

func main() {
	//defer profile.Start(profile.CPUProfile).Stop()
	//defer profile.Start(profile.MemProfile).Stop()

	zHome := connect.NewZHome()

	defer connect.SessClose()

	listener, _ := net.Listen("tcp", ":8000")

	for {
		conn, _ := listener.Accept()
		zHome.NewConn(conn)
	}
}
