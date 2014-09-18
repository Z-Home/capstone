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

type ChatRoom struct {
	clients  []*Client
	joins    chan net.Conn
	incoming chan M
	outgoing chan string
	dead     chan *Client
}

func (chatRoom *ChatRoom) Broadcast(data M) {
	for _, client := range chatRoom.clients {
		client.outgoing <- fmt.Sprintf("%s > %s", data.Name, data.Message)
	}
}

func (chatRoom *ChatRoom) Join(connection net.Conn) {
	client := NewClient(connection)
	chatRoom.clients = append(chatRoom.clients, client)
	go func() {
		for {
			chatRoom.incoming <- <-client.incoming
			chatRoom.dead <- <-client.dead
		}
	}()
}

func (chatRoom *ChatRoom) Listen() {
	go func() {
		for {
			select {
			case data := <-chatRoom.incoming:
				chatRoom.Broadcast(data)
			case conn := <-chatRoom.joins:
				chatRoom.Join(conn)
			case <-chatRoom.dead:

			}
		}
	}()
}

func NewChatRoom() *ChatRoom {
	chatRoom := &ChatRoom{
		clients:  make([]*Client, 0),
		joins:    make(chan net.Conn),
		incoming: make(chan M),
		outgoing: make(chan string),
		dead:     make(chan *Client),
	}

	chatRoom.Listen()

	return chatRoom
}

func main() {
	chatRoom := NewChatRoom()

	listener, _ := net.Listen("tcp", ":8000")

	for {
		conn, _ := listener.Accept()
		chatRoom.joins <- conn
	}
}
