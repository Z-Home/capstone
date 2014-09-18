package main

import (
	"bufio"
	"encoding/json"
	"fmt"
	"net"
)

type M struct {
	Name    string
	Message string
}

type Client struct {
	incoming chan M
	outgoing chan string
	reader   *bufio.Reader
	conn     net.Conn
	dead     chan *Client
}

func (client *Client) Read() {
	for {
		line, err := client.reader.ReadString('\n')
		if err != nil {
			break
		}
		var m M
		json.Unmarshal([]byte(line), &m)
		client.incoming <- m
	}
	client.dead <- client
}

func (client *Client) Write() {
	for data := range client.outgoing {
		_, err := client.conn.Write([]byte(data))
		if err != nil {
			client.dead <- client
		}
	}
}

func (client *Client) Listen() {
	go client.Read()
	go client.Write()
}

func NewClient(connection net.Conn) *Client {
	reader := bufio.NewReader(connection)

	client := &Client{
		incoming: make(chan M),
		outgoing: make(chan string),
		reader:   reader,
		conn:     connection,
		dead:     make(chan *Client),
	}

	client.Listen()

	return client
}

type ZHome struct {
	clients  []*Client
	joins    chan net.Conn
	incoming chan M
	outgoing chan string
	dead     chan *Client
}

func (zHome *ZHome) Broadcast(data M) {
	for _, client := range zHome.clients {
		client.outgoing <- fmt.Sprintf("%s > %s", data.Name, data.Message)
	}
}

func (zHome *ZHome) Join(connection net.Conn) {
	client := NewClient(connection)
	zHome.clients = append(zHome.clients, client)
	go func() {
		for {
			zHome.incoming <- <-client.incoming
			zHome.dead <- <-client.dead
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
			case <-zHome.dead:

			}
		}
	}()
}

func NewZHome() *ZHome {
	zHome := &ZHome{
		clients:  make([]*Client, 0),
		joins:    make(chan net.Conn),
		incoming: make(chan M),
		outgoing: make(chan string),
		dead:     make(chan *Client),
	}

	zHome.Listen()

	return zHome
}

func main() {
	zHome := NewZHome()

	listener, _ := net.Listen("tcp", ":8000")

	for {
		conn, _ := listener.Accept()
		zHome.joins <- conn
	}
}
