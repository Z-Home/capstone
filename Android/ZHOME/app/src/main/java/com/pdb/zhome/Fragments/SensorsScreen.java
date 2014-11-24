package com.pdb.zhome.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.miz.pdb.R;
import com.pdb.zhome.Activities.MainActivity;
import com.pdb.zhome.Adapters.sensorsScreenAdapter;
import com.pdb.zhome.Devices.Device;
import com.pdb.zhome.HashMapHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SensorsScreen extends Fragment{

    private static HashMap<String, Device> deviceHashMap = MainActivity.getHashMap();

    public SensorsScreen() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.screen_sensors, container, false);

        ArrayList<String> deviceArrayList = new ArrayList<String>(HashMapHelper.getDeviceNames("sensor"));

        TextView totalSensors = (TextView) rootView.findViewById(R.id.totalSensorsTxt);
        Integer switches = deviceArrayList.size();
        totalSensors.setText(switches.toString());



        String[] sensorsListArray = deviceArrayList.toArray(new String[deviceArrayList.size()]);

        ArrayList<String> ar = new ArrayList<String>();
        HashMap<String, String> hm = new HashMap<String, String>();
        for (int i =0; i<sensorsListArray.length; i++){
            HashMap<String, String> vals = deviceHashMap.get(sensorsListArray[i]).getValues();
            for(Map.Entry<String, String> entry : vals.entrySet()) {
                //System.out.println(sensorsListArray[i] + " " + entry.getKey() + " " + entry.getValue());
                if (entry.getKey().equals("49")){
                    try {
                        JSONObject sensors = new JSONObject(entry.getValue());
                        sensors = sensors.getJSONObject("sensors");
                        Iterator<String> iter = sensors.keys();
                        while (iter.hasNext()) {
                            String key = iter.next();
                            Object value = sensors.get(key);
                            System.out.println(key + "----" + value);
                            ar.add(key);
                            hm.put(key.toString(), value.toString());
                        }
                        //System.out.println(sensors);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    ar.add(deviceHashMap.get(sensorsListArray[i]).getDevName());
                    hm.put(deviceHashMap.get(sensorsListArray[i]).getDevName(), deviceHashMap.get(sensorsListArray[i]).getValues().get("48"));
                }
            }

        }
        ListAdapter sensorsScreenAdapter = new com.pdb.zhome.Adapters.sensorsScreenAdapter(getActivity(), ar, hm);

        ListView sensorsListView = (ListView) rootView.findViewById(R.id.sensorsListView);

        sensorsListView.setAdapter(sensorsScreenAdapter);

        return rootView;
    }

}
