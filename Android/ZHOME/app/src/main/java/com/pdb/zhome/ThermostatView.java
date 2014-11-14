package com.pdb.zhome;

import android.app.Fragment;
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

public class ThermostatView extends Fragment{

    public ThermostatView() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.control_thermostat, container, false);

//        ArrayList<String> stringArrayList = new ArrayList<String>(HashMapHelper.getDeviceNames("thermostat"));
//
//        //Sample Data only
//        String[] thermostatStringArray = stringArrayList.toArray(new String[stringArrayList.size()]);
//
//        ListAdapter thermostatScreen = new ThermostatCustomAdapter(getActivity(), thermostatStringArray);
//
//        ListView thermostatListView = (ListView) rootView.findViewById(R.id.thermostatListView);
//
//        thermostatListView.setAdapter(thermostatScreen);

        return rootView;
    }

}
