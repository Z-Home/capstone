package com.pdb.zhome;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.miz.pdb.R;
import java.util.HashMap;

class favoritesAddAdapter extends ArrayAdapter<String> {

    private HashMap<String, Device> deviceHashMap;

    public favoritesAddAdapter(Context context, String[] values) {
        super(context, R.layout.row_layout_favorites_add, values);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        deviceHashMap = MainActivity.getHashMap();

        LayoutInflater theInflater = LayoutInflater.from(getContext());

        View theView = theInflater.inflate(R.layout.row_layout_favorites_add, parent, false);

        final String item = getItem(position);

        String type = HashMapHelper.getType(item);

        TextView theTextView = (TextView) theView.findViewById(R.id.favoritesAddTextView);

        theTextView.setText(deviceHashMap.get(item).getDevName());
        theTextView.setId(Integer.parseInt(deviceHashMap.get(item).getDevNum()));

        ImageView theImageView = (ImageView) theView.findViewById(R.id.favoritesAddListImageView);

        final ImageButton favBtn = (ImageButton) theView.findViewById(R.id.favoritesIconBtn);
        //Check the whether devices are in set as favorites or not
        // and set the icon based on that check

        if (type.equals("switch")) {
            theImageView.setImageResource(R.drawable.lights);
        } else if (type.equals("thermostat")) {
            theImageView.setImageResource(R.drawable.thermostat);
        } else if (type.equals("sensor")) {
            theImageView.setImageResource(R.drawable.sensors);
        } else {
            theImageView.setImageResource(R.drawable.ic_launcher);
        }

        return theView;
    }
}