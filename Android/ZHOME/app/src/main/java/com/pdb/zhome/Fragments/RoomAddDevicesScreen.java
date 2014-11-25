package com.pdb.zhome.Fragments;

import android.app.Fragment;
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

import com.miz.pdb.R;
import com.pdb.zhome.Activities.MainActivity;
import com.pdb.zhome.Adapters.roomAddDevicesAdapter;
import com.pdb.zhome.Adapters.roomDevicesAdapter;
import com.pdb.zhome.Devices.Device;
import com.pdb.zhome.HashMapHelper;
import com.pdb.zhome.Room;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by broccard on 11/17/14.
 */
public class RoomAddDevicesScreen extends Fragment {

    private HashMap<String, Device> deviceHashMap;

    public RoomAddDevicesScreen() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.screen_room_add_devices, container, false);

        Bundle bundle = getArguments();
        String room = bundle.getString("name");

        String[] deviceNames = HashMapHelper.getAllDeviceNames();
        deviceHashMap = MainActivity.getHashMap();

        final ListAdapter roomAddDevicesAdapter = new roomAddDevicesAdapter(getActivity(), deviceNames, room, getFragmentManager());

        final ListView roomAddDevicesListView = (ListView) rootView.findViewById(R.id.roomAddDevicesListView);

        roomAddDevicesListView.setAdapter(roomAddDevicesAdapter);

        return rootView;
    }

}
