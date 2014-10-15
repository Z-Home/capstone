package main

import (
	"net"

	"capstone/connect"
)

func main() {
	zHome := connect.NewZHome()

	listener, _ := net.Listen("tcp", ":8000")

	for {
		conn, _ := listener.Accept()
		zHome.NewConn(conn)
	}
}
