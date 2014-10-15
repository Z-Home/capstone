package main

import (
	"fmt"
	"labix.org/v2/mgo"
	"labix.org/v2/mgo/bson"
	"os"
)

type Person struct {
	First string
	Last  string
	Age   int32
}

func main() {
	sess, err := mgo.Dial("mongodb://test:test@192.168.0.17:27017/test")
	if err != nil {
		fmt.Printf("Can't connect to mongo, go error %v\n", err)
		os.Exit(1)
	}
	defer sess.Close()

	sess.SetSafe(&mgo.Safe{})
	collection := sess.DB("test").C("foo")
	err = collection.Insert(&Person{"Bryan", "Rich", 22})
	if err != nil {
		fmt.Printf("Can't insert document: %v\n", err)
		os.Exit(1)
	}

	err = collection.Update(bson.M{"first": "Bryan"}, bson.M{"$set": bson.M{"age": 22}})
	if err != nil {
		fmt.Printf("Can't update document %v\n", err)
		os.Exit(1)
	}

	var result []Person
	//err = collection.Find(bson.M{"first": "Bryan"}).All(&result)
	err = collection.Find(bson.M{}).All(&result)
	if err != nil {
		fmt.Printf("got an error finding a doc %v\n")
		os.Exit(1)
	}

	for _, value := range result {
		fmt.Printf("%v %v is %v\n", value.First, value.Last, value.Age)
	}
}
