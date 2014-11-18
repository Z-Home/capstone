package com.pdb.zhome;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.miz.pdb.R;

import java.util.ArrayList;

public class ThermostatListScreen extends Fragment{

    public ThermostatListScreen() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.screen_thermostat, container, false);

        ArrayList<String> stringArrayList = new ArrayList<String>(HashMapHelper.getDeviceNames("thermostat"));

        //Sample Data only
        String[] thermostatStringArray = stringArrayList.toArray(new String[stringArrayList.size()]);

        ListAdapter thermostatScreen = new thermostatCustomAdapter(getActivity(), thermostatStringArray);

        ListView thermostatListView = (ListView) rootView.findViewById(R.id.thermostatListView);

        thermostatListView.setAdapter(thermostatScreen);

        thermostatListView.setOnItemClickListener(new  AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String thermostatPicked = String.valueOf(adapterView.getItemAtPosition(i));
                Fragment thermostatView = new ThermostatView();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, thermostatView).addToBackStack(null).commit();
                getActivity().getActionBar().setTitle(thermostatPicked);

            }
        });

        return rootView;
    }

}
