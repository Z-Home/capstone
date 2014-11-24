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
import com.pdb.zhome.Adapters.sensorsScreenAdapter;
import com.pdb.zhome.HashMapHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SensorsScreen extends Fragment{

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

        for (int i =0; i<sensorsListArray.length; i++){
            System.out.println(HashMapHelper.getStatus(sensorsListArray[i]));
            try {
                JSONObject sensors = new JSONObject(sensorsListArray[i]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        ListAdapter sensorsScreenAdapter = new com.pdb.zhome.Adapters.sensorsScreenAdapter(getActivity(), sensorsListArray);

        ListView sensorsListView = (ListView) rootView.findViewById(R.id.sensorsListView);

        sensorsListView.setAdapter(sensorsScreenAdapter);

        return rootView;
    }

}
