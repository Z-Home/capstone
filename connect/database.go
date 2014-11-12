package connect

import (
	"code.google.com/p/go.crypto/scrypt"
	"crypto/rand"
	"encoding/json"
	"fmt"
	"github.com/jeffail/gabs"
	"io"
	"labix.org/v2/mgo"
	"labix.org/v2/mgo/bson"
	"log"
	"runtime/debug"
	"strings"
)

const (
	DB_USER       = "admin"
	DB_PASS       = "admin"
	DB_DB         = "users"
	DB_COLLECTION = "info"
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

type dev struct {
	Devlist string
	Value   string
}

var collection *mgo.Collection

func StartDB(zHome *ZHome) {

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
		return 3
	}

	temp, err := scrypt.Key([]byte(c.Pass), []byte(result.Salt), 16384, 8, 1, 32)
	if err != nil {
		fmt.Println("error")
	}

	hash := fmt.Sprintf("%x", temp)

	debug.FreeOSMemory()

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

func DeviceName(deviceList string) string {
	list := dev{}

	err := collection.Find(bson.M{"devlist": 1}).One(&list)
	if err != nil {
		fmt.Printf("got an error finding a doc %v\n")
		return "nil"
	}

	jsonParsed, _ := gabs.ParseJSON([]byte(deviceList))
	children, _ := jsonParsed.S("devices").Children()

	if list.Value == "" {
		jsonObj, _ := gabs.Consume(map[string]interface{}{})
		for _, value := range children {
			num := value.Path("devNumber").String()
			num = strings.Replace(num, "\"", "", -1)
			jsonObj.Set("", num)
		}

		err = collection.Update(bson.M{"devlist": 1}, bson.M{"$set": bson.M{"value": jsonObj.String()}})
		if err != nil {
			fmt.Printf("Can't update document %v\n", err)
		}
	} else {
		devJson, _ := gabs.ParseJSON([]byte(list.Value))
		dev, _ := devJson.S("devices").Children()

		for _, child := range dev {
			dbNum := child.Path("Num").String()
			dbNum = strings.Replace(dbNum, "\"", "", -1)
			dbName := child.Path("Name").String()
			for _, val := range children {
				temp := val.Path("devNumber").String()
				temp = strings.Replace(temp, "\"", "", -1)

				if dbNum == temp {
					tempName := strings.Replace(dbName, "\"", "", -1)
					jsonParsed.Set(tempName, "devices", dbNum, "devName")
				}
			}
		}

		l, _ := jsonParsed.S("devices").Children()

		x := `{"devices":[`
		for _, e := range l {
			tempName := e.Path("devName").String()
			tempNum := e.Path("devNumber").String()
			x = fmt.Sprintf("%s{\"Num\":%s,\"Name\":%s},", x, tempNum, tempName)
		}
		x = strings.TrimSuffix(x, ",")
		x = fmt.Sprintf(`%s]}`, x)

		err = collection.Update(bson.M{"devlist": 1}, bson.M{"$set": bson.M{"value": x}})
		if err != nil {
			fmt.Printf("Can't update document %v\n", err)
		}
	}
	fmt.Println(jsonParsed.String())
	return jsonParsed.String()
}

func UpdateDevList(x string) {
	err := collection.Update(bson.M{"devlist": 1}, bson.M{"$set": bson.M{"value": x}})
	if err != nil {
		fmt.Printf("Can't update document %v\n", err)
	}
}
