package com.pdb.zhome.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.miz.pdb.R;
import com.pdb.zhome.Activities.MainActivity;
import com.pdb.zhome.Devices.Device;
import com.pdb.zhome.SocketCom;

import org.json.JSONObject;

import java.util.HashMap;

public class TestFragment extends Fragment {
    private SocketCom socketCom;
    private HashMap<String, Device> deviceHashMap;
    private View rootView;

	public TestFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_test, container, false);

        deviceHashMap = MainActivity.getHashMap();
        socketCom = SocketCom.getInstance();
        fillUI();
		return rootView;
	}

    public void fillUI(){
        final LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.layout);

        LinearLayout l = new LinearLayout(getActivity());
        l.setOrientation(LinearLayout.HORIZONTAL);

        Button on = new Button(getActivity());
        on.setText("On");
        on.setWidth(90);
        on.setHeight(60);
        on.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                JSONObject command;
                command = deviceHashMap.get("3").command("37", "1");
                socketCom.sendMessage(command);
            }
        });
        l.addView(on);

        Button off = new Button(getActivity());
        off.setText("Off");
        off.setWidth(90);
        off.setHeight(60);
        off.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                JSONObject command;
                command = deviceHashMap.get("3").command("37", "0");
                socketCom.sendMessage(command);
            }
        });
        l.addView(off);
        linearLayout.addView(l);
    }
}
