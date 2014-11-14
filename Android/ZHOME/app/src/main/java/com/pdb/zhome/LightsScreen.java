package com.pdb.zhome;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.miz.pdb.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class LightsScreen extends Fragment{

    public LightsScreen() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.screen_lights, container, false);

        ArrayList<String> deviceArrayList = new ArrayList<String>(HashMapHelper.getDeviceNames("switch"));

        TextView totalSwitches = (TextView) rootView.findViewById(R.id.totalLightsNumTxt);
        Integer switches = deviceArrayList.size();
        totalSwitches.setText(switches.toString());

        TextView numSwitchesOn = (TextView) rootView.findViewById(R.id.lightsOnNumTxt);
        Integer switchesOn = HashMapHelper.getSwitchesOn();
        numSwitchesOn.setText(switchesOn.toString());

        String[] lightsListArray = deviceArrayList.toArray(new String[deviceArrayList.size()]);


        ListAdapter lightsScreenAdapter = new lightsScreenAdapter(getActivity(), lightsListArray);

        ListView lightsListView = (ListView) rootView.findViewById(R.id.lightsListView);

        lightsListView.setAdapter(lightsScreenAdapter);

        return rootView;
    }

}
