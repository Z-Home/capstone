package com.pdb.zhome;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.miz.pdb.R;

import java.util.ArrayList;

public class ThermostatView extends Fragment{

    private String devNum;

    public ThermostatView(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.control_thermostat, container, false);
        Bundle args = getArguments();
        devNum = args.getString("devNum");

        TextView tempTextView = (TextView) rootView.findViewById(R.id.temperatureControlTxt);

        tempTextView.setText(convertTemp(HashMapHelper.getTemp(devNum)));
        TextView setTempTextView = (TextView) rootView.findViewById(R.id.textView2);
        if (HashMapHelper.getSetTemp(devNum).equals("off")){
            setTempTextView.setText("off");
        }else
            setTempTextView.setText(convertTemp(HashMapHelper.getSetTemp(devNum)));
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

    public String convertTemp(String stringTemp){
        Float floatTemp = Float.parseFloat(stringTemp);
        floatTemp = (9 * floatTemp)/5 + 32;
        Integer intTemp = Math.round(floatTemp);
        return intTemp.toString();
    }

}
