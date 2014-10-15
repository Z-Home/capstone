package connect

import (
	"code.google.com/p/go.crypto/scrypt"
	"crypto/rand"
	"encoding/json"
	"fmt"
	"io"
	"labix.org/v2/mgo"
	"labix.org/v2/mgo/bson"
	"log"
)

const (
	DB_USER       = "test"
	DB_PASS       = "test"
	DB_DB         = "test"
	DB_COLLECTION = "foo"
)

type User struct {
	User  string
	Hash  string
	Salt  string
	Admin bool
}

type Cred struct {
	User string
	Pass string
}

var collection *mgo.Collection

func StartDB() {

	url := fmt.Sprintf("mongodb://%s:%s@%s:27017/%s", DB_USER, DB_PASS, IP_ADDRESS, DB_DB)

	sess, err := mgo.Dial(url)
	if err != nil {
		fmt.Printf("Can't connect to mongo, go error %v\n", err)
	}
	//defer sess.Close()

	sess.SetSafe(&mgo.Safe{})
	collection = sess.DB(DB_DB).C(DB_COLLECTION)
}

func CheckPass(cred string) int {

	result := User{}

	var c Cred
	json.Unmarshal([]byte(cred), &c)

	err := collection.Find(bson.M{"user": c.User}).One(&result)
	if err != nil {
		fmt.Printf("got an error finding a doc %v\n")
	}

	temp, err := scrypt.Key([]byte(c.Pass), []byte(result.Salt), 16384, 8, 1, 32)
	if err != nil {
		fmt.Println("error")
	}

	hash := fmt.Sprintf("%x", temp)

	if hash == result.Hash {
		if result.Admin {
			return 1
		}
		return 2
	}

	return 3
}

func NewUser(user string, password string) {
	salt := make([]byte, 32)
	_, err := io.ReadFull(rand.Reader, salt)
	if err != nil {
		log.Fatal(err)
	}

	s := fmt.Sprintf("%x", salt)

	hash, err := scrypt.Key([]byte(password), []byte(s), 16384, 8, 1, 32)
	if err != nil {
		log.Fatal(err)
	}

	h := fmt.Sprintf("%x", hash)

	err = collection.Insert(&User{user, h, s, false})
	if err != nil {
		fmt.Printf("Can't insert document: %v\n", err)
	}
}
