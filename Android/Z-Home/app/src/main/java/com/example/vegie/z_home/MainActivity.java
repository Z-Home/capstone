package com.example.vegie.z_home;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class MainActivity extends Activity {

    // Declare Tab Variables
    ActionBar.Tab Tab1, Tab2, Tab3, Tab4, Tab5;
    Fragment fragmentTab1 = new FavoritesTab();
    Fragment fragmentTab2 = new RoomsTab();
    Fragment fragmentTab3 = new ScenariosTab();
    Fragment fragmentTab4 = new SummaryTab();
    Fragment fragmentTab5 = new TestTab();

    private static SocketCom socketCom;
    public static HashMap<String, Device> deviceHashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ActionBar actionBar = getActionBar();

        // Create Actionbar Tabs
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Set Tab Icon and Titles
        //Tab1 = actionBar.newTab().setIcon(R.drawable.tab1);//ICON
        Tab1 = actionBar.newTab().setIcon(R.drawable.favorites);
        Tab2 = actionBar.newTab().setIcon(R.drawable.rooms);
        Tab3 = actionBar.newTab().setIcon(R.drawable.scenarios);
        Tab4 = actionBar.newTab().setIcon(R.drawable.summary);
        Tab5 = actionBar.newTab().setText("Test");

        // Set Tab Listeners
        Tab1.setTabListener(new TabListener(fragmentTab1));
        Tab2.setTabListener(new TabListener(fragmentTab2));
        Tab3.setTabListener(new TabListener(fragmentTab3));
        Tab4.setTabListener(new TabListener(fragmentTab4));
        Tab5.setTabListener(new TabListener(fragmentTab5));

        // Add tabs to actionbar
        actionBar.addTab(Tab1);
        actionBar.addTab(Tab2);
        actionBar.addTab(Tab3);
        actionBar.addTab(Tab4);
        actionBar.addTab(Tab5);

        conn();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_add) {

            DialogFragment myFragment = new MyDialogFragment();

            myFragment.show(getFragmentManager(), "theDialog");

            return true;
        } else if (id == R.id.exit_the_app) {
            finish();
            return true;
        } else if (id == R.id.go_to_test){
            Intent intent = new Intent(this, TestActivity.class);
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
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

        TestTab fragment = (TestTab) getFragmentManager().findFragmentById(R.id.activityTest);
        fragment.fillUI();
    }

    public static SocketCom getSocketCom(){
        return socketCom;
    }

    public static HashMap<String,Device> getHashMap(){
        return deviceHashMap;
    }
}
