package com.pdb.zhome;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.miz.pdb.R;

class thermostatCustomAdapter extends ArrayAdapter<String> {
    public thermostatCustomAdapter(Context context, String[] values) {
        super(context, R.layout.row_layout_thermostat, values);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater theInflater = LayoutInflater.from(getContext());

        View theView = theInflater.inflate(R.layout.row_layout_thermostat, parent, false);

        String item = getItem(position);

        TextView theTextView = (TextView) theView.findViewById(R.id.thermostatScreenText);

        theTextView.setText(item);

        ImageView theImageView = (ImageView) theView.findViewById(R.id.thermostatScreenImageView);

        theImageView.setImageResource(R.drawable.thermostat);

        return theView;
    }
}