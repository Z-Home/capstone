package com.pdb.zhome;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.miz.pdb.R;

class customAdapter extends ArrayAdapter<String> {
    public customAdapter(Context context, String[] values) {
        super(context, R.layout.favorites_row_layout, values);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater theInflater = LayoutInflater.from(getContext());

        View theView = theInflater.inflate(R.layout.favorites_row_layout, parent, false);

        String favoriteItem = getItem(position);

        TextView theTextView = (TextView) theView.findViewById(R.id.favoritesTextView);

        theTextView.setText(favoriteItem);

        ImageView theImageView = (ImageView) theView.findViewById(R.id.favoritesImageView);

        theImageView.setImageResource(R.drawable.ic_launcher);

        return theView;
    }
}