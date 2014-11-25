package com.pdb.zhome.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
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
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Vegie on 11/18/14.
 */
import com.miz.pdb.R;
import com.pdb.zhome.Activities.MainActivity;
import com.pdb.zhome.Room;
import com.pdb.zhome.SocketCom;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    View rootView;
    Spinner mySpinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.form_room_add, container, false);

        mySpinner = (Spinner) rootView.findViewById(R.id.roomFormSpinner);

        mySpinner.setAdapter(new spinnerAdapter(rootView.getContext(), R.layout.row_spinner, roomNames));

        Button addButton = (Button) rootView.findViewById(R.id.addRoomFormBtn);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject newRoom = new JSONObject();
                EditText roomNameEditText = (EditText)rootView.findViewById(R.id.roomNameText);
                String roomName = roomNameEditText.getText().toString();
                String[] devices = {};
                String roomType = mySpinner.getSelectedItem().toString();
                try {
                    newRoom.put("name", roomName);
                    newRoom.put("type", roomType);
                    JSONArray devicesJsonArray = new JSONArray();
                    int j = 0;
                    for(j = 0; j < devices.length; j++){
                        devicesJsonArray.put(devices[j]);
                    }
                    newRoom.put("devices", devicesJsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                JSONObject finalJson = createRoomUpdateJson(newRoom);



                System.out.println(finalJson.toString());
                SocketCom.sendMessage(finalJson);

                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.popBackStack();

            }
        });

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

    private JSONObject createRoomUpdateJson(JSONObject newRoom) {
        HashMap<String, Room> roomHashMap = MainActivity.getRoomsHashMap();
        Room[] rooms = roomHashMap.values().toArray(new Room[roomHashMap.size()]);
        int length = rooms.length;
        int i = 0;
        JSONArray roomsJsonArray = new JSONArray();
        for(i = 0; i < length; i++){
            JSONObject roomJson = new JSONObject();
            JSONArray devicesJsonArray = new JSONArray();
            String[] devices = rooms[i].getDevices();
            int j = 0;
            for(j = 0; j < devices.length; j++){
                devicesJsonArray.put(devices[j]);
            }
            try {
                roomJson.put("name", rooms[i].getName());
                roomJson.put("devices", devicesJsonArray);
                roomJson.put("type", rooms[i].getStringType());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            roomsJsonArray.put(roomJson);
        }

        roomsJsonArray.put(newRoom);
        JSONObject roomsJsonObject = new JSONObject();
        JSONObject finalJson = new JSONObject();
        try {
            roomsJsonObject.put("rooms", roomsJsonArray);
            finalJson.put("Type", "Rooms");
            finalJson.put("Json", roomsJsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return finalJson;
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