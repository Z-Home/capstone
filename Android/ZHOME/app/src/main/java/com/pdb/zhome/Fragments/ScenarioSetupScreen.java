package com.pdb.zhome.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.miz.pdb.R;
import com.pdb.zhome.Activities.MainActivity;
import com.pdb.zhome.Adapters.scenarioSetupAdapter;
import com.pdb.zhome.Devices.Device;
import com.pdb.zhome.HashMapHelper;

import java.util.HashMap;


public class ScenarioSetupScreen extends Fragment {
    private HashMap<String, Device> deviceHashMap;

    public ScenarioSetupScreen() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        final View rootView = inflater.inflate(R.layout.screen_scenarios_setup, container, false);

        String[] deviceNames = HashMapHelper.getAllDeviceNames();
        deviceHashMap = MainActivity.getHashMap();

        final ListAdapter scenariosSetupAdapter = new scenarioSetupAdapter(getActivity(), deviceNames);

        final ListView scenariosSetupListView = (ListView) rootView.findViewById(R.id.scenariosSetupListView);

        scenariosSetupListView.setAdapter(scenariosSetupAdapter);
        //Add devices to scenario
        ImageButton addDevicesToScenariosBtn = (ImageButton) rootView.findViewById(R.id.addDevicesToScenariosBtn);
        addDevicesToScenariosBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment addDevicesScreen = new ScenariosAddDevicesScreen();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, addDevicesScreen).addToBackStack(null).commit();
                getActivity().getActionBar().setTitle("Add Devices");
                MainActivity.setCurrentFragment(addDevicesScreen);
            }
        });

        return rootView;
    }

}
