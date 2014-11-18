package com.pdb.zhome;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;

import java.util.HashMap;

import com.miz.pdb.R;

public class RoomsFragment extends Fragment {

    private Fragment screen;

	public RoomsFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_rooms, container, false);

        final HashMap<String, String[]> roomsHashMap = MainActivity.getRoomsHashMap();

        String[] roomsListArray = roomsHashMap.keySet().toArray(new String[roomsHashMap.size()]);

        ListAdapter roomsListAdapter = new roomsCustomAdapter(getActivity(), roomsListArray);

        GridView roomsListView = (GridView) rootView.findViewById(R.id.roomsGridView);

        roomsListView.setAdapter(roomsListAdapter);

//        roomsListView.setOnItemClickListener(new  AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                String roomName = String.valueOf(adapterView.getItemAtPosition(i));
//                System.out.println("ROOM NAME: " + roomName);
//                screen = RoomDevicesScreen.newInstance(String.valueOf(roomName), roomsHashMap.get(roomName));
//
//                FragmentManager fragmentManager = getFragmentManager();
//                fragmentManager.beginTransaction().replace(R.id.content_frame, screen).addToBackStack(null).commit();
//                getActivity().getActionBar().setTitle(String.valueOf(adapterView.getItemAtPosition(i)));
//            }
//        });

		return rootView;
	}

}
