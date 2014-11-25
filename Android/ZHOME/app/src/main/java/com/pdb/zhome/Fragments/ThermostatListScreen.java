package com.pdb.zhome.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.miz.pdb.R;
import com.pdb.zhome.Activities.MainActivity;
import com.pdb.zhome.Adapters.ThermostatCustomAdapter;
import com.pdb.zhome.Devices.Device;
import com.pdb.zhome.HashMapHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class ThermostatListScreen extends Fragment{

    private HashMap<String, Device> deviceHashMap;

    public ThermostatListScreen() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.screen_thermostat, container, false);

        ArrayList<String> stringArrayList = new ArrayList<String>(HashMapHelper.getDeviceNames("thermostat"));

        //Sample Data only
        String[] thermostatStringArray = stringArrayList.toArray(new String[stringArrayList.size()]);

        ListAdapter thermostatScreen = new ThermostatCustomAdapter(getActivity(), thermostatStringArray);

        final ListView thermostatListView = (ListView) rootView.findViewById(R.id.thermostatListView);

        thermostatListView.setAdapter(thermostatScreen);

        thermostatListView.setOnItemClickListener(new  AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                deviceHashMap = MainActivity.getHashMap();
                String thermostatPicked = String.valueOf(adapterView.getItemAtPosition(i));
                Fragment thermostatView = new ThermostatView();
                FragmentManager fragmentManager = getFragmentManager();
                Bundle args = new Bundle();
                args.putString("devNum", thermostatPicked);
                thermostatView.setArguments(args);
                fragmentManager.beginTransaction().replace(R.id.content_frame, thermostatView, "ThermostatView").addToBackStack(null).commit();
                MainActivity.setCurrentFragment(thermostatView);
                getActivity().getActionBar().setTitle(deviceHashMap.get(thermostatPicked).getDevName());
            }
        });

        return rootView;
    }

}
