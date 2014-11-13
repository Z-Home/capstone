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
import android.widget.Toast;

import com.miz.pdb.R;

import java.util.ArrayList;

public class LightsScreen extends Fragment{

    public LightsScreen() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.screen_lights, container, false);

        ArrayList<String> deviceArrayList = new ArrayList<String>(HashMapHelper.getDeviceNames("switch"));

        String[] lightsListArray = deviceArrayList.toArray(new String[deviceArrayList.size()]);


        ListAdapter lightsScreenAdapter = new lightsScreenAdapter(getActivity(), lightsListArray);

        ListView lightsListView = (ListView) rootView.findViewById(R.id.lightsListView);

        lightsListView.setAdapter(lightsScreenAdapter);

        return rootView;
    }

}
