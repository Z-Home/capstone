package com.pdb.zhome.Adapters;

import android.app.Fragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.miz.pdb.R;
import com.pdb.zhome.Activities.MainActivity;
import com.pdb.zhome.Room;

import java.util.HashMap;

public class roomsCustomAdapter extends ArrayAdapter<String> {

    private Fragment screen;

    public roomsCustomAdapter(Context context, String[] values) {
        super(context, R.layout.grid_single_rooms, values);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater theInflater = LayoutInflater.from(getContext());

        View theView = theInflater.inflate(R.layout.grid_single_rooms, parent, false);

        HashMap<String, Room> roomsHashMap = MainActivity.getRoomsHashMap();

        String roomName = getItem(position);

        Room room = roomsHashMap.get(roomName);

        TextView theTextView = (TextView) theView.findViewById(R.id.roomsTextView);

        theTextView.setText(roomName);

        ImageView theImageView = (ImageView) theView.findViewById(R.id.roomsImageView);

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

        return theView;
    }
}