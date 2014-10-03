package connect

import (
	"code.google.com/p/go.crypto/scrypt"
	"encoding/json"
	"fmt"
	"labix.org/v2/mgo"
	"labix.org/v2/mgo/bson"
)

const (
	DB_USER       = "test"
	DB_PASS       = "test"
	DB_DB         = "test"
	DB_COLLECTION = "foo"
)

type DB struct {
	collection *mgo.Collection
}

type User struct {
	User string
	Hash string
	Salt string
}

type Cred struct {
	User string
	Pass string
}

func StartDB() *DB {

	url := fmt.Sprintf("mongodb://%s:%s@192.168.0.17:27017/%s", DB_USER, DB_PASS, DB_DB)

	sess, err := mgo.Dial(url)
	if err != nil {
		fmt.Printf("Can't connect to mongo, go error %v\n", err)
	}
	//defer sess.Close()

	sess.SetSafe(&mgo.Safe{})
	collection := sess.DB(DB_DB).C(DB_COLLECTION)

	db := &DB{
		collection: collection,
	}

	return db
}

func (db *DB) CheckPass(cred string) bool {

	result := User{}

	var c Cred
	json.Unmarshal([]byte(cred), &c)

	err := db.collection.Find(bson.M{"user": c.User}).One(&result)
	if err != nil {
		fmt.Printf("got an error finding a doc %v\n")
	}

	temp, err := scrypt.Key([]byte(c.Pass), []byte(result.Salt), 16384, 8, 1, 32)
	if err != nil {
		fmt.Println("error")
	}

	hash := fmt.Sprintf("%x", temp)

	if hash == result.Hash {
		return true
	}

	return false
}
