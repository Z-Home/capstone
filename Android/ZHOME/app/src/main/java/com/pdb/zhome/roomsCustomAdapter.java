package com.pdb.zhome;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.miz.pdb.R;

class roomsCustomAdapter extends ArrayAdapter<String> {
    public roomsCustomAdapter(Context context, String[] values) {
        super(context, R.layout.grid_single_rooms, values);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater theInflater = LayoutInflater.from(getContext());

        View theView = theInflater.inflate(R.layout.grid_single_rooms, parent, false);

        String favoriteItem = getItem(position);

        TextView theTextView = (TextView) theView.findViewById(R.id.roomsTextView);

        theTextView.setText(favoriteItem);

        ImageView theImageView = (ImageView) theView.findViewById(R.id.roomsImageView);

        theImageView.setImageResource(R.drawable.outlets);

        return theView;
    }
}