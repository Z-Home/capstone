package com.pdb.zhome;

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

public class RoomsFragment extends Fragment {

    private Fragment screen;

	public RoomsFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.form_room_add, container, false);

        final HashMap<String, String[]> roomsHashMap = MainActivity.getRoomsHashMap();

        String[] roomsListArray = roomsHashMap.keySet().toArray(new String[roomsHashMap.size()]);

        ListAdapter roomsListAdapter = new roomsCustomAdapter(getActivity(), roomsListArray);

        GridView roomsListView = (GridView) rootView.findViewById(R.id.roomFormSpinner);

        roomsListView.setAdapter(roomsListAdapter);


        ImageButton addRoomBtn = (ImageButton) rootView.findViewById(R.id.addRoomFormBtn);
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
