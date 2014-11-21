package com.pdb.zhome;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.miz.pdb.R;

class favoritesCustomAdapter extends ArrayAdapter<String> {
    public favoritesCustomAdapter(Context context, String[] values) {
        super(context, R.layout.row_layout_favorites, values);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater theInflater = LayoutInflater.from(getContext());

        //
        // A bunch of if statements to check for command class and set the correct row layout
        // (Just like customLightsAdapter)


        View theView = theInflater.inflate(R.layout.row_layout_favorites, parent, false);

        String favoriteItem = getItem(position);

        TextView theTextView = (TextView) theView.findViewById(R.id.favoritesTextView);

        theTextView.setText(favoriteItem);

        ImageView theImageView = (ImageView) theView.findViewById(R.id.favoritesImageView);

        theImageView.setImageResource(R.drawable.sensors);

        return theView;
    }
}