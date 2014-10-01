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
                        json.put("Device", editTextDevice.getText());
                        json.put("CommandClass", editTextCommandClass.getText());
                        json.put("Command", editTextCommand.getText());
                        System.out.println("Send Command Button clicked. JSON is: " + json.toString());
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




        MyClientTask(String addr, int port){
            dstAddress = addr;
            dstPort = port;
        }

        @Override
        protected Void doInBackground(Void... arg0) {


            try {
                //Create socket connection
                socket = new Socket(dstAddress, dstPort);

                while(socket.isConnected()){
                    if(hasCommandButtonBeenPressed){
                        System.out.println("CommandButtonPressed");
                        sendMessage(json);
                        hasCommandButtonBeenPressed = false;
                    }
                }
                //setup stream to receive responses from server
                ByteArrayOutputStream byteArrayOutputStream =
                        new ByteArrayOutputStream(1024);
                byte[] buffer = new byte[1024];

                int bytesRead;
                InputStream inputStream = socket.getInputStream();


    
    /*
     * notice:
     * inputStream.read() will block if no data return
     */
                //Read responses from server and set the text to the response when received
                while ((bytesRead = inputStream.read(buffer)) != -1){
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                    response = byteArrayOutputStream.toString("UTF-8");
                    handler.post(new Runnable(){
                        public void run(){
                            //Update your view here
                            textResponse.setText(response);
                        }
                    });
                }
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

        private void sendMessage(JSONObject json) {
            System.out.println("Sending Message. JSON is: " + json.toString());
            try	{
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                        socket.getOutputStream()));
                out.write(json.toString());
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