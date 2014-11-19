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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RenameScreen extends Fragment{

    private HashMap<String, Device> deviceHashMap;
    //private String changeText;
    private String num;

    public RenameScreen() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.screen_rename, container, false);


        String[] deviceNames = HashMapHelper.getAllDeviceNames();
        deviceHashMap = MainActivity.getHashMap();

        final ListAdapter renameScreenAdapter = new renameScreenAdapter(getActivity(), deviceNames);

        final ListView lightsListView = (ListView) rootView.findViewById(R.id.renameScreenListView);

        lightsListView.setAdapter(renameScreenAdapter);
        final Button renameButton = (Button) rootView.findViewById(R.id.saveRenameChangesBtn);
        renameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                deviceHashMap = MainActivity.getHashMap();
//                String[] stringArrayList = HashMapHelper.getAllDeviceNames();
//                String commandList = "{\"Type\":\"DeviceList\",\"Json\":{\"devices\":[";
//                String num = null;
//                String name = null;
//                for (int i = 0; i < stringArrayList.length; i++) {
//                    //name = renameScreenAdapter.getChangedNameList();
//                    commandList += "{Num\":\"" + num + "\",\"Name\":\"" + name;
//                }
//
//                Toast.makeText(getActivity(), name, Toast.LENGTH_SHORT).show();
//                JSONObject command;
                //command = '{"Type":"DeviceList","Json":{"devices":[{"Num":"2","Name":"Motion Sensor"},{"Num":"3","Name":"Outlet Switch"},{"Num":"4","Name":"3way Dimmer"},{"Num":"5","Name":"Dimmer"},{"Num":"6","Name":"Thermostat"},{"Num":"7","Name":"Door Sensor"}]}}';
            }
        });

        lightsListView.setOnItemClickListener(new  AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setNum(String.valueOf(adapterView.getItemAtPosition(i)));
                RenameDialog renameDialog = new RenameDialog();
                renameDialog.show(getFragmentManager(), "renameDialog");
            }
        });

        return rootView;
    }

    private void setNum(String num) {
        this.num = num;
    }

    public void changeText(String data) {
        SocketCom socketCom = SocketCom.getInstance();
        JSONObject devices = new JSONObject();
        JSONObject obj = new JSONObject();
        JSONArray ja = new JSONArray();

        for(Map.Entry<String, Device> entry : deviceHashMap.entrySet()) {
            String key = entry.getKey();
            Device temp = entry.getValue();
            String value = temp.getDevName();
            JSONObject jo = new JSONObject();

            if (key.equals(num)){
                try {
                    jo.put("Num", num);
                    jo.put("Name", data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    jo.put("Num", key);
                    jo.put("Name", value);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            ja.put(jo);
        }

        try {
            devices.put("devices", ja);
            obj.put("Type", "DeviceList");
            obj.put("Json", devices);
        } catch (JSONException e) {
            e.printStackTrace();
        };
        System.out.println(obj);
        socketCom.sendMessage(obj);
    }
}
