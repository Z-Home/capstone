package com.example.vegie.z_home;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Bryan on 10/15/2014.
 */
public class TestTab extends Fragment {
    private SocketCom socketCom;
    private HashMap<String, Device> deviceHashMap;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_test, container, false);

        socketCom = MainActivity.getSocketCom();
        deviceHashMap = MainActivity.getHashMap();
        fillUI();
        return rootView;
    }

    public void fillUI(){
        final LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.layout);
        //Button[] buttons;
        int i = 0;

        for (Map.Entry<String, Device> entry : deviceHashMap.entrySet()) {
            final String key = entry.getKey();
            Device value = entry.getValue();

            HashMap<String,String> map = value.getValues();
            ArrayList<String> cc = new ArrayList<String>(map.values());

            if(cc.contains("37")) {
                LinearLayout l = new LinearLayout(getActivity());
                l.setOrientation(LinearLayout.HORIZONTAL);

                Button on = new Button(getActivity());
                on.setText("On");
                on.setId(i);
                on.setWidth(90);
                on.setHeight(60);
                on.setOnClickListener(new Button.OnClickListener() {
                    public void onClick(View v) {
                        deviceHashMap.get(key).command("37", "1");
                    }
                });
                l.addView(on);
                Button off = new Button(getActivity());
                off.setText("Off");
                off.setId(i);
                off.setWidth(90);
                off.setHeight(60);
                off.setOnClickListener(new Button.OnClickListener() {
                    public void onClick(View v) {
                        deviceHashMap.get(key).command("37", "0");
                    }
                });
                l.addView(off);
                linearLayout.addView(l);
            }
        }
    }
}
