package com.pdb.zhome;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;

import com.miz.pdb.R;

public class RoomsFragment extends Fragment {

	public RoomsFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_rooms, container, false);

        String[] roomsListArray = {
                "Bedroom 1",
                "Bedroom 2",
                "Bedroom 3",
                "Bedroom 4",
                "Bedroom 5",
                "Bedroom 6",
                "Bedroom 7",
                "Bedroom 8",
                "Bedroom 9",
                "Bathroom 1",
                "Bathroom 2",
                "Bathroom 3",
                "Bathroom 4",
                "Kitchen",
                "Living Room",
                "Basement"
        };


        ListAdapter favoritesListAdapter = new roomsCustomAdapter(getActivity(), roomsListArray);

        GridView favoritesListView = (GridView) rootView.findViewById(R.id.roomsGridView);

        favoritesListView.setAdapter(favoritesListAdapter);

		return rootView;
	}

}
