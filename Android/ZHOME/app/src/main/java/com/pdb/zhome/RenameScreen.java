package com.pdb.zhome;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.miz.pdb.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class RenameScreen extends Fragment{

    private HashMap<String, Device> deviceHashMap;

    public RenameScreen() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.screen_rename, container, false);


        String[] deviceNames = HashMapHelper.getAllDeviceNames();


        final ListAdapter renameScreenAdapter = new renameScreenAdapter(getActivity(), deviceNames);

        final ListView lightsListView = (ListView) rootView.findViewById(R.id.renameScreenListView);

        lightsListView.setAdapter(renameScreenAdapter);
        final Button renameButton = (Button) rootView.findViewById(R.id.saveRenameChangesBtn);
        renameButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                deviceHashMap = MainActivity.getHashMap();
                String[] stringArrayList = HashMapHelper.getAllDeviceNames();
                String commandList = "{\"Type\":\"DeviceList\",\"Json\":{\"devices\":[";
                String num = null;
                String name = null;
                for(int i = 0; i<stringArrayList.length; i++){
                    //name = renameScreenAdapter.getChangedNameList();
                    commandList+="{Num\":\""+num+"\",\"Name\":\""+name;
                }

                Toast.makeText(getActivity(), name, Toast.LENGTH_SHORT).show();
                JSONObject command;
                //command = '{"Type":"DeviceList","Json":{"devices":[{"Num":"2","Name":"Motion Sensor"},{"Num":"3","Name":"Outlet Switch"},{"Num":"4","Name":"3way Dimmer"},{"Num":"5","Name":"Dimmer"},{"Num":"6","Name":"Thermostat"},{"Num":"7","Name":"Door Sensor"}]}}';

            }
        });

        return rootView;
    }

}
