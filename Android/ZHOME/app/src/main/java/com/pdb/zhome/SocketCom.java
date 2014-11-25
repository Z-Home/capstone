package com.pdb.zhome;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.app.Activity;

import com.pdb.zhome.Activities.LoginActivity;
import com.pdb.zhome.Activities.MainActivity;
import com.pdb.zhome.Activities.SplashScreenActivity;
import com.pdb.zhome.Devices.Device;
import com.pdb.zhome.Devices.Sensor;
import com.pdb.zhome.Devices.Switch;
import com.pdb.zhome.Devices.Thermostat;
import com.pdb.zhome.Room.RoomType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Bryan on 10/8/2014.
 */
public class SocketCom extends AsyncTask<Void, String, Void> {

    private JSONArray roomsJsonArray;

    public static enum context{LOGIN, MAIN, SPLASH};
    private context currentContext = context.SPLASH;
    private Activity currentActivity = null;
    private LoginActivity loginActivity = null;
    private MainActivity mainActivity = null;
    private SplashScreenActivity splashActivity = null;
    private static SocketCom socketCom = new SocketCom();

    private String dstAddress, fromServerString;
    private int dstPort;
    private Socket socket;
    private BufferedReader in;
    private static PrintWriter out;
    private HashMap<String,Device> deviceHashMap;
    private HashMap<String, Room> roomsHashMap;
    private boolean isConnected = false;

    private SocketCom(){}

    public static SocketCom getInstance(){
        if(socketCom != null) {
            return socketCom;
        }else{
            socketCom = new SocketCom();
            return socketCom;
        }
    }

    public void conn(){
        this.deviceHashMap = MainActivity.getHashMap();

        ConnectivityManager connManager = (ConnectivityManager) currentActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected()) {
            WifiManager wifiManager = (WifiManager) currentActivity.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            String wifiMac = wifiInfo.getBSSID();

            if (!wifiMac.isEmpty() && wifiMac.equals("0c:47:3d:e3:6a:68")){
                setAddress("192.168.0.17");
            } else {
                setAddress("bryanrich3.ddns.net");
            }
        } else {
            setAddress("bryanrich3.ddns.net");
        }

        setPort(8000);
        execute();
    }

    public boolean isConnected(){
        return isConnected;
    }

    private void setAddress(String address) {
        this.dstAddress = address;
    }

    private void setPort(int port) {
        this.dstPort = port;
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        try {
            socket = new Socket(dstAddress, dstPort);
            if(socket.isConnected()){
                isConnected = true;
            }
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            JSONObject fromServerJson;

            while(socket.isConnected()){
                fromServerString = in.readLine();
                System.out.println("Server: " + fromServerString);

                try {
                    fromServerJson = new JSONObject(fromServerString);
                    int resp = fromServerJson.getInt("Type");
                    switch(resp){
                        case 0://AUTHENTICATE
                            System.out.println("0: LOGGING IN");
                            System.out.println("JSON Message: " + fromServerJson.getString("Message"));
                            if(fromServerJson.getString("Message").equals("Incorrect Login")){
                                publishProgress("Incorrect Login");
                            }else{
                                publishProgress("Started");
                            }
                            break;
                        case 1://DEVICE ACCESS
                            System.out.println("1: LOGGED IN");
                            publishProgress("Connected");
                            createDevices(fromServerJson.getJSONObject("Message").getJSONObject("devices"));
                            publishProgress("update");
                            break;
                        case 2://UPDATE
                            System.out.println("2: UPDATE");
                            String[] info = updateHashMap(fromServerJson.getJSONObject("Message").getJSONObject("update"));
                            publishProgress("update");
                            break;
                        case 3://ROOMS
                            JSONArray rooms = fromServerJson.getJSONObject("Message").getJSONArray("rooms");
                            createRooms(rooms);
                            System.out.println(rooms);
                        case 6:
                            JSONArray newRooms = fromServerJson.getJSONObject("Message").getJSONArray("rooms");
                            createRooms(newRooms);
                            publishProgress("update");
                        default:
                            System.out.println("DEFAULT");
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(socket != null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        if(currentContext == context.MAIN) {
            mainActivity.update(values);
        }else if(currentContext == context.SPLASH){
            splashActivity.update(values);
        }else{
            loginActivity.update(values);
        }
    }

    public void createDevices(JSONObject devices){
        try {
            Iterator<String> keys = devices.keys();
            deviceHashMap = MainActivity.getHashMap();
            while(keys.hasNext()) {
                String innerKeys = keys.next();

                JSONObject commandClasses = devices.getJSONObject(innerKeys).getJSONObject("commandClasses");
                String devName = devices.getJSONObject(innerKeys).getString("devName");
                Iterator<String> classes = commandClasses.keys();

                List<String> cc = new ArrayList<String>();
                HashMap<String, String> map = new HashMap<String, String>();

                while(classes.hasNext()){
                    String cla = classes.next();
                    String val = commandClasses.getString(cla);
                    cc.add(cla);
                    map.put(cla, val);
                }

                Device device = null;
                if (cc.contains("66")){
                    device = new Thermostat(map, innerKeys, devName);
                }else if (cc.contains("37") || cc.contains("38")){
                    device = new Switch(map, innerKeys, devName);
                }else if (cc.contains("48") || cc.contains("49")){
                    device = new Sensor(map, innerKeys, devName);
                }

                deviceHashMap.put(innerKeys, device);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        publishProgress("Started");
    }

    private void createRooms(JSONArray roomsArray){
        roomsHashMap = MainActivity.getRoomsHashMap();
        roomsJsonArray = MainActivity.getRoomsJsonArray();
        roomsJsonArray = roomsArray;

        int i = 0;
        int length = roomsArray.length();
        for(i = 0; i < length; i++){
            try {
                //Get the room as a JSONObject
                JSONObject room = roomsArray.getJSONObject(i);
                //Get the room name from the JSONObject
                String roomName = room.getString("name");
                String roomTypeString = room.getString("type");
                RoomType roomType = Room.getTypeGivenString(roomTypeString);
                //Get the array of devices as a JSONArray
                JSONArray devicesJsonArray = room.getJSONArray("devices");
                //Get length of JSONArray
                int numDevices = devicesJsonArray.length();
                //Initialize roomDevices array
                String[] roomDevices = new String[numDevices];
                //Put JSONArray of devices into String room array
                int j = 0;
                for(j = 0; j < numDevices; j++){
                    roomDevices[j] = devicesJsonArray.getString(j);
                }
                Room newRoom = new Room(roomName, roomDevices, roomType);
                //Add to hash map
                roomsHashMap.put(roomName, newRoom);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private String[] updateHashMap(JSONObject update){
        String[] values = new String[3];
        String dev, cc, c;

        try {
            dev = update.getString("device");
            cc = update.getString("commandClass");
            c = update.getString("value");

            values = deviceHashMap.get(dev).formatUIinfo(cc, c);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return values;
    }

    public void attemptLogin(String username, String password) {
        System.out.println("IN SOCKET TRYING: " + username + " " + password);
        JSONObject jsonLoginInfo = new JSONObject();
        try{
            jsonLoginInfo.put("User", username);
            jsonLoginInfo.put("Pass", password);
        }catch(JSONException e){
            e.printStackTrace();
        }
        out.println(jsonLoginInfo.toString());
    }

    public void switchContext(Activity activeActivity, context con){
        if(con == context.LOGIN){
            loginActivity = (LoginActivity)activeActivity;
            currentActivity = loginActivity;
            mainActivity = null;
            splashActivity = null;
        }else if(con == context.SPLASH){
            splashActivity = (SplashScreenActivity)activeActivity;
            currentActivity = splashActivity;
            mainActivity = null;
            loginActivity = null;
        }else{
            mainActivity = (MainActivity) activeActivity;
            currentActivity = mainActivity;
            loginActivity = null;
            splashActivity = null;
        }

        currentContext = con;
    }

    public static void sendMessage(JSONObject json) {
        out.println(json.toString());
    }

    public void closeConnection() throws IOException {
        socket.close();
        this.cancel(true);
        isConnected = false;
        socketCom = null;
    }
}
