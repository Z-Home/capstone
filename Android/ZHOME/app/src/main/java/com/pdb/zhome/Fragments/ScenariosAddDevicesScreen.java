package com.pdb.zhome.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.miz.pdb.R;
import com.pdb.zhome.Activities.MainActivity;
import com.pdb.zhome.Adapters.roomAddDevicesAdapter;
import com.pdb.zhome.Adapters.scenarioAddDevicesAdapter;
import com.pdb.zhome.Devices.Device;
import com.pdb.zhome.HashMapHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class ScenariosAddDevicesScreen extends Fragment {

    private HashMap<String, Device> deviceHashMap;
    private ArrayList<String> selectedDevices;

    public ScenariosAddDevicesScreen() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.screen_room_add_devices, container, false);

        selectedDevices = new ArrayList<String>();

        Bundle bundle = getArguments();

        final String scenarioName = bundle.getString("scenario_name");

        String[] deviceNames = HashMapHelper.getAllDeviceNames();
        ArrayList<String> deviceList = new ArrayList<String>();
        deviceHashMap = MainActivity.getHashMap();
        for (int i = 0; i< deviceNames.length; i++){
            if (deviceHashMap.get(deviceNames[i]).getType().equals("sensor")){

            } else
                deviceList.add(deviceNames[i]);
        }

        String[] noSensors = deviceList.toArray(new String[deviceList.size()]);
        deviceHashMap = MainActivity.getHashMap();

        final ListAdapter scenarioAddDevicesAdapter = new scenarioAddDevicesAdapter(getActivity(), noSensors, getFragmentManager());

        final ListView scenarioAddDevicesListView = (ListView) rootView.findViewById(R.id.roomAddDevicesListView);

        scenarioAddDevicesListView.setAdapter(scenarioAddDevicesAdapter);

        Button addDevicesToScenario = (Button) rootView.findViewById(R.id.addRoomFormBtn);

        addDevicesToScenario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment scenarioSetupScreen = new ScenarioSetupScreen();
                FragmentManager fragmentManager = getFragmentManager();
                Bundle args = new Bundle(2);
                args.putString("scenario_name", scenarioName);
                args.putStringArrayList("device_list", selectedDevices);
                scenarioSetupScreen.setArguments(args);
                fragmentManager.beginTransaction().replace(R.id.content_frame, scenarioSetupScreen, "scenarioSetup").addToBackStack(null).commit();
                getActivity().getActionBar().setTitle("Set Device Statuses in " + scenarioName);
            }
        });

        return rootView;
    }

    public void addToSelectedDevices(String devNum){
        selectedDevices.add(devNum);
    }

    public void removeFromSelectedDevices(String devNum){
        selectedDevices.remove(devNum);
    }

    public boolean containsDevice(String devNum){
        if (selectedDevices.contains(devNum))
            return true;
        else
            return false;
    }

}
