package com.broccard.jsoncommunicationtest.jsoncommunicationtest;

import java.net.*;
import java.io.*;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.json.JSONObject;
import org.json.JSONException;
import java.nio.charset.StandardCharsets;
import  	android.os.Handler;

public class MainActivity extends Activity {

    TextView textResponse;
    EditText editTextAddress, editTextPort, editTextDevice, editTextCommandClass, editTextCommand;
    Button buttonConnect, buttonClear, buttonSendCommand;
    boolean hasCommandButtonBeenPressed = false;
    enum ServerRespTypes { AUTHENTICATE, DEVICE_ACCESS, UPDATE }

    final Handler handler = new Handler();

    JSONObject json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextAddress = (EditText)findViewById(R.id.address);
        editTextPort = (EditText)findViewById(R.id.port);
        editTextDevice = (EditText)findViewById(R.id.editTextDevice);
        editTextCommandClass = (EditText)findViewById(R.id.editTextCommandClass);
        editTextCommand = (EditText)findViewById(R.id.editTextCommand);
        buttonConnect = (Button)findViewById(R.id.connect);
        buttonClear = (Button)findViewById(R.id.clear);
        buttonSendCommand = (Button)findViewById(R.id.buttonSendCommand);
        textResponse = (TextView)findViewById(R.id.response);

        buttonConnect.setOnClickListener(buttonConnectOnClickListener);

        buttonClear.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                textResponse.setText("");
            }});

        buttonSendCommand.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                json = new JSONObject();
                hasCommandButtonBeenPressed = true;
                if(editTextCommand.getText() != null && editTextCommandClass.getText() != null && editTextCommand.getText() != null){
                    try {
                        json.put("device", editTextDevice.getText());
                        json.put("commandClass", editTextCommandClass.getText());
                        json.put("command", editTextCommand.getText());
                        //System.out.println("Send Command Button clicked. JSON is: " + json.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    public JSONObject getJson(){
        return json;
    }

    OnClickListener buttonConnectOnClickListener =
            new OnClickListener(){

                @Override
                public void onClick(View arg0) {
                    MyClientTask myClientTask = new MyClientTask(
                            editTextAddress.getText().toString(),
                            Integer.parseInt(editTextPort.getText().toString()));
                    myClientTask.execute();
                }};

    public class MyClientTask extends AsyncTask<Void, Void, Void> {

        String dstAddress;
        int dstPort;
        String response = "";
        Socket socket = null;
        final Handler clientThreadHandler = new Handler();
        String fromServerString = "";

        MyClientTask(String addr, int port){
            dstAddress = addr;
            dstPort = port;
        }

        @Override
        protected Void doInBackground(Void... arg0) {


            try {
                //Create socket connection
                socket = new Socket(dstAddress, dstPort);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));

                String fromUser = "";
                JSONObject fromServerJson = new JSONObject();
                boolean isLoggedIn = false;

                while(socket.isConnected()){
                    System.out.println("LOOP");
                    if(((fromServerString = in.readLine()) != null)){
                        System.out.println("GOT SERVER COMMUNICATION");
                        handler.post(new Runnable(){
                            public void run(){
                                //Update your view here
                                textResponse.setText(fromServerString);
                            }
                        });
                        System.out.println("Server: " + fromServerString);

                        try {
                            fromServerJson = new JSONObject(fromServerString);
                            int resp = fromServerJson.getInt("Type");
                            switch(resp){
                                //AUTHENTICATE
                                case 0:
                                    System.out.println("0: LOGGING IN");
                                    login();
                                    break;
                                //DEVICE ACCESS
                                case 1:
                                    System.out.println("1: LOGGED IN");
                                    isLoggedIn = true;
                                    break;
                                //UPDATE
                                case 2:
                                    System.out.println("2: UPDATE");
                                    break;
                                default:
                                    System.out.println("DEFAULT");
                                    break;

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    //System.out.println("ACCEPTING USER INPUT. hasCommandButtonBeenPressed: " + hasCommandButtonBeenPressed + ", isLoggedIn: " + isLoggedIn);

                    if(hasCommandButtonBeenPressed && isLoggedIn) {
                        System.out.println("CommandButtonPressed");
                        sendMessage(json);
                        hasCommandButtonBeenPressed = false;
                    }
                }
//                if(socket.isConnected()){
//                    login();
//                }
//
////                ByteArrayOutputStream byteArrayOutputStream =
////                        new ByteArrayOutputStream(1024);
////                byte[] buffer = new byte[1024];
////
////                int bytesRead;
////                InputStream inputStream = socket.getInputStream();
////                inputStream.read();
////
////                //Read responses from server and set the text to the response when received
////                while ((bytesRead = inputStream.read(buffer)) != -1){
////                    byteArrayOutputStream.write(buffer, 0, bytesRead);
////                    response = byteArrayOutputStream.toString("UTF-8");
////                    handler.post(new Runnable(){
////                        public void run(){
////                            //Update your view here
////                            textResponse.setText(response);
////                        }
////                    });
////                }
//
//                while(socket.isConnected()){
//                    if(hasCommandButtonBeenPressed) {
//                        System.out.println("CommandButtonPressed");
//                        sendMessage(json);
//                        hasCommandButtonBeenPressed = false;
//
////                    }else{
////                        //Read responses from server and set the text to the response when received
////                        while ((bytesRead = inputStream.read(buffer)) != -1){
////                            byteArrayOutputStream.write(buffer, 0, bytesRead);
////                            response = byteArrayOutputStream.toString("UTF-8");
////                            handler.post(new Runnable(){
////                                public void run(){
////                                    //Update your view here
////                                    textResponse.setText(response);
////                                }
////                            });
////                        }
//
//                    }
//                }
                //setup stream to receive responses from server
//                ByteArrayOutputStream byteArrayOutputStream =
//                        new ByteArrayOutputStream(1024);
//                byte[] buffer = new byte[1024];
//
//                int bytesRead;
//                InputStream inputStream = socket.getInputStream();
//
//
//
//    /*
//     * notice:
//     * inputStream.read() will block if no data return
//     */
//                //Read responses from server and set the text to the response when received
//                while ((bytesRead = inputStream.read(buffer)) != -1){
//                    byteArrayOutputStream.write(buffer, 0, bytesRead);
//                    response = byteArrayOutputStream.toString("UTF-8");
//                    handler.post(new Runnable(){
//                        public void run(){
//                            //Update your view here
//                            textResponse.setText(response);
//                        }
//                    });
//                }
            } catch (UnknownHostException e) {
                e.printStackTrace();
                response = "UnknownHostException: " + e.toString();
            } catch (IOException e) {
                e.printStackTrace();
                response = "IOException: " + e.toString();
            }finally{
                if(socket != null){
                    try {
                        socket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        private void login() {
            JSONObject jsonLogin = new JSONObject();
            JSONObject jsonLoginInfo = new JSONObject();
            try{
               jsonLoginInfo.put("User", "Bryan");
               jsonLoginInfo.put("Pass", "password");
            }catch(JSONException e){
                e.printStackTrace();
            }

            try{
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                        socket.getOutputStream()));
                out.write(jsonLoginInfo.toString());
                out.newLine();
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void sendMessage(JSONObject json) {
            System.out.println("Sending Message. JSON is: " + json.toString());
            JSONObject jsonToSend = new JSONObject();

            try {
                jsonToSend.put("Type", "Command");
                jsonToSend.put("Json", json.toString());
                System.out.println("Sending JSON: " + jsonToSend.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try	{
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                        socket.getOutputStream()));
                out.write(jsonToSend.toString());
                out.newLine();
                out.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(Void result) {
            textResponse.setText(response);
            super.onPostExecute(result);
        }
    }

}