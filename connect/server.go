package connect

import (
	"fmt"
	"net"
)

type M struct {
	Name    string
	Message string
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

func (zHome *ZHome) NewConn(con net.Conn) {
	zHome.joins <- con
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
