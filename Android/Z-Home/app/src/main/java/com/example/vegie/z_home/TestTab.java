package com.example.vegie.z_home;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Bryan on 10/15/2014.
 */
public class TestTab extends Fragment implements View.OnClickListener {
    private SocketCom socketCom;
    private HashMap<String, Device> deviceHashMap;

    public Button onButton;
    public Button offButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_test, container, false);

        onButton = (Button) rootView.findViewById(R.id.button3);
        onButton.setOnClickListener(this);

        offButton = (Button) rootView.findViewById(R.id.button2);
        offButton.setOnClickListener(this);

        socketCom = MainActivity.getSocketCom();
        deviceHashMap = MainActivity.getHashMap();
        return rootView;
    }

    @Override
    public void onClick(View v) {
        JSONObject command;
        switch(v.getId()){
            case R.id.button3:
                command = deviceHashMap.get("3").command("37","1");
                socketCom.sendMessage(command);
                break;
            case R.id.button2:
                command = deviceHashMap.get("3").command("37","0");
                socketCom.sendMessage(command);
                break;
        }
    }
}
