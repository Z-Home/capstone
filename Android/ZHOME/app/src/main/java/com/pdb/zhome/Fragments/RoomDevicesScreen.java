package com.pdb.zhome.Fragments;

import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.app.Fragment;

import java.util.HashMap;

import com.miz.pdb.R;
import com.pdb.zhome.Activities.MainActivity;
import com.pdb.zhome.Adapters.roomDevicesAdapter;
import com.pdb.zhome.Devices.Device;
import com.pdb.zhome.HashMapHelper;
import com.pdb.zhome.Room;


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

        HashMap<String, Room> rooms = MainActivity.getRoomsHashMap();
        ArrayList<String> deviceArrayList = new ArrayList<String>(HashMapHelper.getDeviceNames("switch"));

        TextView roomNameTextView = (TextView) rootView.findViewById(R.id.roomNameTextView);
        final String roomName = activity.getCurrentRoomName();
        roomNameTextView.setText(roomName);

        Room room = rooms.get(roomName);

        String[] devices = room.getDevices();

        TextView numDevicesInRoomTextView = (TextView) rootView.findViewById(R.id.roomNumDevicesTextView);
        System.out.println("ROOM DEVICES: " + devices.length);
       // numDevicesInRoomTextView.setText(devices.length);

        //Add devices to room
        ImageButton addDevicesToRoomBtn = (ImageButton) rootView.findViewById(R.id.addDevicesToRoomBtn);
        addDevicesToRoomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("name", roomName);

                Fragment addDevicesScreen = new RoomAddDevicesScreen();
                addDevicesScreen.setArguments(bundle);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, addDevicesScreen).addToBackStack(null).commit();
                getActivity().getActionBar().setTitle("Add Devices " + roomName);
                MainActivity.setCurrentFragment(addDevicesScreen);
            }
        });


        ImageView theImageView = (ImageView) rootView.findViewById(R.id.roomDevicesScreenImage);

        int imageResource = 0;

        if(room.getType() == Room.RoomType.BATHROOM){
            imageResource = R.drawable.bathroom;
        }else if(room.getType() == Room.RoomType.BEDROOM){
            imageResource = R.drawable.bedroom;
        }else if(room.getType() == Room.RoomType.DINING){
            imageResource = R.drawable.dining;
        }else if(room.getType() == Room.RoomType.KITCHEN){
            imageResource = R.drawable.kitchen;
        }else if(room.getType() == Room.RoomType.LIVINGROOM){
            imageResource = R.drawable.livingroom;
        }else if(room.getType() == Room.RoomType.OFFICE){
            imageResource = R.drawable.office;
        }else if(room.getType() == Room.RoomType.OUTSIDE){
            imageResource = R.drawable.outside;
        }else{
            imageResource = R.drawable.rooms;
        }

        theImageView.setImageResource(imageResource);

        ListAdapter roomsScreenAdapter = new roomDevicesAdapter(activity, devices);

        ListView roomListView = (ListView) rootView.findViewById(R.id.roomListView);

        roomListView.setOnItemClickListener(new  AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(MainActivity.getHashMap().get(String.valueOf(adapterView.getItemAtPosition(i))).getValues().keySet().contains("66")) {
                    HashMap<String, Device> deviceHashMap = MainActivity.getHashMap();
                    String thermostatPicked = String.valueOf(adapterView.getItemAtPosition(i));
                    Fragment thermostatView = new ThermostatView();
                    FragmentManager fragmentManager = getFragmentManager();
                    Bundle args = new Bundle();
                    args.putString("devNum", thermostatPicked);
                    thermostatView.setArguments(args);
                    fragmentManager.beginTransaction().replace(R.id.content_frame, thermostatView, "ThermostatView").addToBackStack(null).commit();
                    MainActivity.setCurrentFragment(thermostatView);
                    getActivity().getActionBar().setTitle(deviceHashMap.get(thermostatPicked).getDevName());
                }
            }
        });

        roomListView.setAdapter(roomsScreenAdapter);

        return rootView;
    }

}
