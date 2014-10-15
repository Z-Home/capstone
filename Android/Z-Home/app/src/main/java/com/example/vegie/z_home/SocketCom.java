package com.example.vegie.z_home;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

/**
 * Created by Bryan on 10/8/2014.
 */
public class SocketCom extends AsyncTask<Void, String, Void> {

    private String dstAddress, fromServerString;
    private int dstPort;
    private Socket socket;
    private BufferedReader in;
    private static PrintWriter out;
    private MainActivity activity;
    private HashMap<String,Device> deviceHashMap;

    public SocketCom(String address, int port, MainActivity activity){
        setAddress(address);
        setPort(port);
        setActivity(activity);
    }

    private void setActivity(MainActivity activity){
        this.activity = activity;
        deviceHashMap = this.activity.deviceHashMap;
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
                            System.out.println("0: LOGGING IN");
                            login();
                            break;
                        case 1://DEVICE ACCESS
                            System.out.println("1: LOGGED IN");
                            publishProgress("Connected");
                            activity.createDevices(fromServerJson.getJSONObject("Message").getJSONObject("devices"));
                            break;
                        case 2://UPDATE
                            System.out.println("2: UPDATE");
                            String[] info = updateHashMap(fromServerJson.getJSONObject("Message").getJSONObject("update"));
                            publishProgress(info);
                            break;
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
        activity.update(values);
    }

    private String[] updateHashMap(JSONObject update){
        String[] values = new String[3];
        String dev, cc, c;

        try {
            dev = update.getString("device");
            cc = update.getString("commandClass");
            c = update.getString("command");

            values = deviceHashMap.get(dev).formatUIinfo(cc, c);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return values;
    }

    private void login() {
        JSONObject jsonLoginInfo = new JSONObject();
        try{
            jsonLoginInfo.put("User", "Bryan");
            jsonLoginInfo.put("Pass", "password");
        }catch(JSONException e){
            e.printStackTrace();
        }

        out.println(jsonLoginInfo.toString());
    }

    public static void sendMessage(JSONObject json) {
        out.println(json.toString());
    }
}

