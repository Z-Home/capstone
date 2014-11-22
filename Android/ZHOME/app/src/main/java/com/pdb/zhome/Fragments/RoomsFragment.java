package com.pdb.zhome.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListAdapter;

import java.util.HashMap;

import com.miz.pdb.R;
import com.pdb.zhome.Activities.MainActivity;
import com.pdb.zhome.Adapters.roomsCustomAdapter;
import com.pdb.zhome.Room;

public class RoomsFragment extends Fragment {

    private Fragment screen;

	public RoomsFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_rooms, container, false);

        HashMap<String, Room> roomsHashMap = MainActivity.getRoomsHashMap();

        String[] roomsListArray = roomsHashMap.keySet().toArray(new String[roomsHashMap.size()]);

        ListAdapter roomsListAdapter = new roomsCustomAdapter(getActivity(), roomsListArray);

        GridView roomsListView = (GridView) rootView.findViewById(R.id.roomsGridView);

        roomsListView.setAdapter(roomsListAdapter);

        roomsListView.setOnItemClickListener(new  AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String room = String.valueOf(adapterView.getItemAtPosition(i));
                MainActivity activity = (MainActivity)getActivity();
                activity.setCurrentRoomName(room);
                screen = new RoomDevicesScreen();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, screen).addToBackStack(null).commit();
                getActivity().getActionBar().setTitle(room);
                MainActivity.setCurrentFragment(screen);
            }
        });

        ImageButton addRoomBtn = (ImageButton) rootView.findViewById(R.id.addRoomBtn);
        addRoomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                screen = new RoomAddFormScreen();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, screen).addToBackStack(null).commit();
                getActivity().getActionBar().setTitle("Add Room");
            }
        });

		return rootView;
	}

}
