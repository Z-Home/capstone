package com.example.bryan.zHomeApplication;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Bryan on 10/8/2014.
 */
public class SocketCom extends AsyncTask<Void, String, Void> {

    private String dstAddress, fromServerString;
    private int dstPort;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private MyActivity activity;

    public SocketCom(String address, int port, MyActivity activity){
        setAddress(address);
        setPort(port);
        setActivity(activity);
    }

    private void setActivity(MyActivity activity){
        this.activity = activity;
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
                            publishProgress(fromServerJson.getJSONObject("Message").getJSONObject("update").getString("value"));
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
        activity.update(values[0]);
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

    public void sendMessage(JSONObject json) {
        out.println(json.toString());
    }
}

