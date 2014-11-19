package com.pdb.zhome;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by Vegie on 11/18/14.
 */
import com.miz.pdb.R;

import java.util.HashMap;

public class RoomAddFormScreen extends Fragment {

    public RoomAddFormScreen() {
    }

    String[] roomNames = {
            "Bedroom",
            "Office",
            "Bathroom",
            "Kitchen",
            "Dining",
            "Living Room",
            "Outside",
            "Other"
    };


    int roomIcons[] = {
            R.drawable.bedroom,
            R.drawable.office,
            R.drawable.bathroom,
            R.drawable.kitchen,
            R.drawable.dining,
            R.drawable.livingroom,
            R.drawable.outside,
            R.drawable.ic_launcher
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.form_room_add, container, false);

        Spinner mySpinner = (Spinner) rootView.findViewById(R.id.roomFormSpinner);

        mySpinner.setAdapter(new spinnerAdapter(rootView.getContext(), R.layout.row_spinner, roomNames));


//        final HashMap<String, String[]> roomsHashMap = MainActivity.getRoomsHashMap();
//
//        String[] roomsListArray = roomsHashMap.keySet().toArray(new String[roomsHashMap.size()]);
//
//        ListAdapter roomsListAdapter = new roomsCustomAdapter(getActivity(), roomsListArray);
//
//        GridView roomsListView = (GridView) rootView.findViewById(R.id.roomsGridView);
//
//        roomsListView.setAdapter(roomsListAdapter);


        return rootView;
    }



    public class spinnerAdapter extends ArrayAdapter<String> {

        public spinnerAdapter(Context context, int textViewResourceId,   String[] objects) {
            super(context, textViewResourceId, objects);
        }

        @Override
        public View getDropDownView(int position, View convertView,ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {

            LayoutInflater theInflater = LayoutInflater.from(getContext());

            View theView = theInflater.inflate(R.layout.row_spinner, parent, false);

            TextView label = (TextView)theView.findViewById(R.id.roomTypeText);
            label.setText(roomNames[position]);

            ImageView icon = (ImageView) theView.findViewById(R.id.spinnerImageView);
            icon.setImageResource(roomIcons[position]);

            return theView;
        }
    }
}