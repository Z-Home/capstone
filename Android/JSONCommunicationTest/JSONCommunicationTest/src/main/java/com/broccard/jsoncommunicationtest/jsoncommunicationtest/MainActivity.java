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
    EditText editTextAddress, editTextPort;
    Button buttonConnect, buttonClear;

    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextAddress = (EditText)findViewById(R.id.address);
        editTextPort = (EditText)findViewById(R.id.port);
        buttonConnect = (Button)findViewById(R.id.connect);
        buttonClear = (Button)findViewById(R.id.clear);
        textResponse = (TextView)findViewById(R.id.response);

        buttonConnect.setOnClickListener(buttonConnectOnClickListener);

        buttonClear.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                textResponse.setText("");
            }});
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

        MyClientTask(String addr, int port){
            dstAddress = addr;
            dstPort = port;
        }

        @Override
        protected Void doInBackground(Void... arg0) {


            JSONObject json = null;

            try {
                socket = new Socket(dstAddress, dstPort);

                System.out.println("sending json");
                sendMessage("{\"Device\":\"3\",\"CommandClass\":\"37\",\"Command\":\"0\"}");
                System.out.println("sent json");

                ByteArrayOutputStream byteArrayOutputStream =
                        new ByteArrayOutputStream(1024);
                byte[] buffer = new byte[1024];

                int bytesRead;
                InputStream inputStream = socket.getInputStream();
    
    /*
     * notice:
     * inputStream.read() will block if no data return
     */
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

        private void sendMessage(String s) {
            try	{
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject(s);
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