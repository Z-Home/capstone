package com.pdb.zhome;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.miz.pdb.R;

public class ThermostatScreen extends Fragment{

    public ThermostatScreen() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.screen_thermostat, container, false);

        //Sample Data only
        String[] lightsListArray = {
                "Bedroom Bed Light Test Long text to see what happens? 12 34567 890",
                "Living Room Light 1",
                "Living Room Light 2",
                "Living Room Light 3",
                "Main Entrance Light",
                "Outdoor Light 1",
                "Outdoor Light 2",
                "Outdoor Light 3",
                "Backyard Light",
                "Bedroom Bed Light",
                "Living Room Light 1",
                "Living Room Light 2",
                "Living Room Light 3",
                "Main Entrance Light",
                "Outdoor Light 1",
                "Outdoor Light 2",
                "Outdoor Light 3",
                "Backyard Light"
        };


        ListAdapter lightsScreenAdapter = new lightsScreenAdapter(getActivity(), lightsListArray);

        ListView lightsListView = (ListView) rootView.findViewById(R.id.lightsListView);

        lightsListView.setAdapter(lightsScreenAdapter);

        return rootView;
    }

}
