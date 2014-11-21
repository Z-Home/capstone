package com.pdb.zhome;

import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.app.Fragment;
import android.widget.GridView;
import java.util.HashMap;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        final View rootView = inflater.inflate(R.layout.screen_room_devices, container, false);

        MainActivity activity = (MainActivity)getActivity();

        HashMap<String, String[]> rooms = MainActivity.getRoomsHashMap();
        ArrayList<String> deviceArrayList = new ArrayList<String>(HashMapHelper.getDeviceNames("switch"));

        TextView roomNameTextView = (TextView) rootView.findViewById(R.id.roomNameTextView);
        String roomName = activity.getCurrentRoomName();
        roomNameTextView.setText(roomName);

        String[] devices = rooms.get(roomName);

        TextView numDevicesInRoomTextView = (TextView) rootView.findViewById(R.id.roomNumDevicesTextView);
        System.out.println("ROOM DEVICES: " + devices.length);
       // numDevicesInRoomTextView.setText(devices.length);

        ListAdapter roomsScreenAdapter = new roomDevicesAdapter(activity, devices);

        ListView roomListView = (ListView) rootView.findViewById(R.id.roomListView);

        roomListView.setOnItemClickListener(new  AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(MainActivity.getHashMap().get(String.valueOf(adapterView.getItemAtPosition(i))).getValues().keySet().contains("66")) {
                    String thermostatPicked = String.valueOf(adapterView.getItemAtPosition(i));
                    Fragment thermostatView = new ThermostatView();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, thermostatView).addToBackStack(null).commit();
                    getActivity().getActionBar().setTitle(thermostatPicked);
                }

            }
        });

        roomListView.setAdapter(roomsScreenAdapter);

        return rootView;
    }

}
