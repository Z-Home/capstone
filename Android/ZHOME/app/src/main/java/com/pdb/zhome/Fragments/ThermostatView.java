package com.pdb.zhome.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.miz.pdb.R;
import com.pdb.zhome.HashMapHelper;

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

        RadioButton heatRadioButton = (RadioButton) rootView.findViewById(R.id.heatThermostatRadioBtn);
        RadioButton coolRadioButton = (RadioButton) rootView.findViewById(R.id.coolThermostatRadioBtn);
        RadioButton offRadioButton = (RadioButton) rootView.findViewById(R.id.offThermostatRadioBtn);

        String mode = HashMapHelper.returnThermostatMode(devNum);
        if (mode.equals("0")){
            heatRadioButton.setChecked(false);
            coolRadioButton.setChecked(false);
            offRadioButton.setChecked(true);
        }else if (mode.equals("1")){
            heatRadioButton.setChecked(true);
            coolRadioButton.setChecked(false);
            offRadioButton.setChecked(false);
        }else{
            heatRadioButton.setChecked(false);
            coolRadioButton.setChecked(true);
            offRadioButton.setChecked(false);
        }

        heatRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMapHelper.setThermostatMode(devNum, "1");
            }
        });

        coolRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMapHelper.setThermostatMode(devNum, "2");
            }
        });

        offRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMapHelper.setThermostatMode(devNum, "0");
            }
        });
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
