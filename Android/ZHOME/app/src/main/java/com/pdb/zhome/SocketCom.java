package com.pdb.zhome;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.app.Activity;

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

    public static enum context{LOGIN, MAIN};
    private context currentContext = context.LOGIN;
    private Activity currentActivity = null;
    private LoginActivity loginActivity = null;
    private MainActivity mainActivity = null;
    private static SocketCom socketCom = new SocketCom();

    private String dstAddress, fromServerString;
    private int dstPort;
    private Socket socket;
    private BufferedReader in;
    private static PrintWriter out;
    private HashMap<String,Device> deviceHashMap;

    private SocketCom(){}

    public static SocketCom getInstance(){
        return socketCom;
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
                            publishProgress("Started");
                            System.out.println("0: LOGGING IN");
                            System.out.println("JSON Message: " + fromServerJson.getString("Message"));
                            if(fromServerJson.getString("Message").equals("Incorect Login")){
                                System.out.println("Incorrect Login");
                                if(!loginActivity.isViewInitialized()) {
                                    loginActivity.initLoginScreen();
                                }else{
                                    loginActivity.showIncorrectLoginDialog();
                                }
                            }
                            break;
                        case 1://DEVICE ACCESS
                            System.out.println("1: LOGGED IN");
                            publishProgress("Connected");
                            loginActivity.login();
                            createDevices(fromServerJson.getJSONObject("Message").getJSONObject("devices"));
                            break;
                        case 2://UPDATE
                            System.out.println("2: UPDATE");
                            String[] info = updateHashMap(fromServerJson.getJSONObject("Message").getJSONObject("update"));
                            publishProgress("update");
                            break;
                        case 3://ROOMS
                            JSONArray rooms = fromServerJson.getJSONObject("Message").getJSONArray("rooms");
                            System.out.println(rooms);
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
        System.out.println("Attempting login. Username: " + username + ". Password: " + password);
        JSONObject jsonLoginInfo = new JSONObject();
        try{
            System.out.println("in try");
            if(currentContext == context.LOGIN) {
                System.out.println("in context block");
                jsonLoginInfo.put("User", username);
                jsonLoginInfo.put("Pass", password);
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
        if(out == null){
            System.out.println("out is null");

        }else if(jsonLoginInfo == null){
            System.out.println("jsonLoginInfo is null");
        }else{

        }
        out.println(jsonLoginInfo.toString());
    }

    public void switchContext(Activity activeActivity, context con){
        if(con == context.LOGIN){
            loginActivity = (LoginActivity)activeActivity;
            currentActivity = loginActivity;
            mainActivity = null;
        }else{
            mainActivity = (MainActivity) activeActivity;
            currentActivity = mainActivity;
            loginActivity = null;
        }

        currentContext = con;
    }

    public static void sendMessage(JSONObject json) {
        out.println(json.toString());
    }
}
