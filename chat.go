package main

import (
	"bufio"
	"encoding/json"
	"fmt"
	"log"
	"net"
	"os"
)

type M struct {
	Name string
	Mes  string
}

var (
	clientCount     = 0
	allClients      = make(map[net.Conn]int)
	newConnections  = make(chan net.Conn)
	deadConnections = make(chan net.Conn)
	messages        = make(chan string)
)

func run() {
	for {

		select {

		// Accept new clients
		//
		case conn := <-newConnections:

			log.Printf("Accepted new client, #%d", clientCount)

			// Add this connection to the `allClients` map
			//
			allClients[conn] = clientCount
			clientCount += 1

			// Constantly read incoming messages from this
			// client in a goroutine and push those onto
			// the messages channel for broadcast to others.
			//
			go func(conn net.Conn, clientId int) {
				reader := bufio.NewReader(conn)
				for {
					incoming, err := reader.ReadString('\n')
					if err != nil {
						break
					}
					var m M
					json.Unmarshal([]byte(incoming), &m)
					messages <- fmt.Sprintf("Client %s > %s", m.Name, m.Mes)
				}

				// When we encouter `err` reading, send this
				// connection to `deadConnections` for removal.
				//
				deadConnections <- conn

			}(conn, allClients[conn])

		// Accept messages from connected clients
		//
		case message := <-messages:

			// Loop over all connected clients
			//
			for conn, _ := range allClients {

				// Send them a message in a go-routine
				// so that the network operation doesn't block
				//
				go func(conn net.Conn, message string) {
					_, err := conn.Write([]byte(message))

					// If there was an error communicating
					// with them, the connection is dead.
					if err != nil {
						deadConnections <- conn
					}
				}(conn, message)
			}
			log.Printf("New message: %s", message)
			log.Printf("Broadcast to %d clients", len(allClients))

		// Remove dead clients
		//
		case conn := <-deadConnections:
			log.Printf("Client %d disconnected", allClients[conn])
			delete(allClients, conn)
		}
	}
}

func main() {

	// Start the TCP server
	//
	server, err := net.Listen("tcp", ":8000")
	if err != nil {
		fmt.Println(err)
		os.Exit(1)
	}

	go run()

	// Tell the server to accept connections forever
	// and push new connections into the newConnections channel.
	//
	for {
		conn, err := server.Accept()
		if err != nil {
			fmt.Println(err)
			os.Exit(1)
		}
		newConnections <- conn
	}
}
