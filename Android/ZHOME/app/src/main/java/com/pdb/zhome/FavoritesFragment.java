package com.pdb.zhome;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.miz.pdb.R;

public class FavoritesFragment extends Fragment {

	public FavoritesFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_favorites, container, false);

        //Sample Data only
        String[] favoritesListArray2 = {
                "Bedroom Bed Light",
                "Living Room Thermostat",
                "Entrance Light",
                "Living Room Thermostat",
                "Outdoor Light 1",
                "Outdoor Light 2",
                "Coffee Maker Power"
        };


        ListAdapter favoritesListAdapter = new customAdapter(getActivity(), favoritesListArray2);

        ListView favoritesListView = (ListView) rootView.findViewById(R.id.favoritesListView);

        favoritesListView.setAdapter(favoritesListAdapter);

        favoritesListView.setOnItemClickListener(new  AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String favoriteItemPicked = "You have selected " + String.valueOf(adapterView.getItemAtPosition(i));
                Toast.makeText(getActivity(), favoriteItemPicked, Toast.LENGTH_SHORT).show();
            }
        });

		return rootView;
	}

}