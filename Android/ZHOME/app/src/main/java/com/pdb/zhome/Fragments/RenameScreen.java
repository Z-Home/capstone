package com.pdb.zhome.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.miz.pdb.R;
import com.pdb.zhome.Activities.MainActivity;
import com.pdb.zhome.Devices.Device;
import com.pdb.zhome.HashMapHelper;
import com.pdb.zhome.SocketCom;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RenameScreen extends Fragment{

    private HashMap<String, Device> deviceHashMap;
    private String num;

    public RenameScreen() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.screen_rename, container, false);


        String[] deviceNames = HashMapHelper.getAllDeviceNames();
        deviceHashMap = MainActivity.getHashMap();

        final ListAdapter renameScreenAdapter = new com.pdb.zhome.Adapters.renameScreenAdapter(getActivity(), deviceNames);

        final ListView lightsListView = (ListView) rootView.findViewById(R.id.renameScreenListView);

        lightsListView.setAdapter(renameScreenAdapter);

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

        socketCom.sendMessage(obj);
    }
}
