package com.pdb.zhome;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.app.Fragment;
import android.widget.GridView;

import com.miz.pdb.R;

import java.util.ArrayList;

/**
 * Created by broccard on 11/17/14.
 */
public class RoomDevicesScreen extends Fragment {

    String roomName = "";
    String[] roomDevices;

    public RoomDevicesScreen() {

    }

    public static RoomDevicesScreen newInstance(String roomName, String[] roomDevices) {
        RoomDevicesScreen myFragment = new RoomDevicesScreen();

        Bundle args = new Bundle();
        args.putString("roomName", roomName);
        args.putStringArray("roomDevices", roomDevices);
        myFragment.setArguments(args);

        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.screen_lights, container, false);

        roomName = getArguments().getString("roomName");
        System.out.println("ROOM NAME IN DEVICES SCREEN: " + roomName);
        roomDevices = getArguments().getStringArray("roomDevices");
        int i = 0;
        for(i = 0; i < roomDevices.length; i++){
            System.out.println("room device" + roomDevices[i]);
        }
        ListAdapter roomsListAdapter = new roomsCustomAdapter(getActivity(), roomDevices);
        if(roomsListAdapter == null){
            System.out.println("Rooms List Adapter is null");
        }
        ListView roomsListView = (ListView) rootView.findViewById(R.id.lightsListView);
        if(roomsListView == null){
            System.out.println("Rooms list view is null");
        }
        roomsListView.setAdapter(roomsListAdapter);

        return rootView;
    }

}
