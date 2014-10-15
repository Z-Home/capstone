package com.example.bryan.zHomeApplication;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class MyActivity extends Activity {

    TextView connect;

    private SocketCom socketCom;
    public HashMap<String, Device> deviceHashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        connect = (TextView)findViewById(R.id.textView);
        conn();
    }

    public void conn(){

        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected()) {
            WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            String wifiMac = wifiInfo.getBSSID();

            if (!wifiMac.isEmpty() && wifiMac.equals("0c:47:3d:e3:6a:68")){
                socketCom = new SocketCom("192.168.0.17", 8000, this);
            } else {
                socketCom = new SocketCom("bryanrich3.ddns.net", 8000, this);
            }
        } else {
            socketCom = new SocketCom("bryanrich3.ddns.net", 8000, this);
        }

        socketCom.execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void send_command(View view) {
        JSONObject command = deviceHashMap.get("3").command("37","0");
        socketCom.sendMessage(command);
    }

    public void light_on(View view) {
        JSONObject command = deviceHashMap.get("3").command("37","1");
        socketCom.sendMessage(command);
    }

    public void update(String[] values){

    }

    public void createDevices(JSONObject devices){
        try {
            Iterator<String> keys = devices.keys();
            deviceHashMap = new HashMap<String, Device>();
            while(keys.hasNext()) {
                String innerKeys = keys.next();

                JSONObject commandClasses = devices.getJSONObject(innerKeys).getJSONObject("commandClasses");
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
                    device = new Thermostat(map, innerKeys);
                }else if (cc.contains("37") || cc.contains("38")){
                    device = new Switch(map, innerKeys);
                }else if (cc.contains("48") || cc.contains("49")){
                    device = new Sensor(map, innerKeys);
                }

                deviceHashMap.put(innerKeys, device);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
