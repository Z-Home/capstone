package com.pdb.zhome.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.miz.pdb.R;
import com.pdb.zhome.Activities.MainActivity;
import com.pdb.zhome.Adapters.roomAddDevicesAdapter;
import com.pdb.zhome.Adapters.scenarioAddDevicesAdapter;
import com.pdb.zhome.Devices.Device;
import com.pdb.zhome.HashMapHelper;

import java.util.HashMap;

public class ScenariosAddDevicesScreen extends Fragment {

    private HashMap<String, Device> deviceHashMap;

    public ScenariosAddDevicesScreen() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.screen_room_add_devices, container, false);


        String[] deviceNames = HashMapHelper.getAllDeviceNames();
        deviceHashMap = MainActivity.getHashMap();

        final ListAdapter scenarioAddDevicesAdapter = new scenarioAddDevicesAdapter(getActivity(), deviceNames);

        final ListView scenarioAddDevicesListView = (ListView) rootView.findViewById(R.id.roomAddDevicesListView);

        scenarioAddDevicesListView.setAdapter(scenarioAddDevicesAdapter);

        return rootView;
    }

}
