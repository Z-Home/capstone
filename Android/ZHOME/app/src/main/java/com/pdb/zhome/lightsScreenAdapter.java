package com.pdb.zhome;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.miz.pdb.R;

class lightsScreenAdapter extends ArrayAdapter<String> {
    public lightsScreenAdapter(Context context, String[] values) {
        super(context, R.layout.row_layout_favorites, values);
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        LayoutInflater theInflater = LayoutInflater.from(getContext());

        View theView = theInflater.inflate(R.layout.row_layout_lights, parent, false);

        String favoriteItem = getItem(position);

        TextView theTextView = (TextView) theView.findViewById(R.id.lightsScreenText);

        theTextView.setText(favoriteItem);

        ImageView theImageView = (ImageView) theView.findViewById(R.id.lightsScreenImageView);

        theImageView.setImageResource(R.drawable.sensors);

        final ImageButton powerBtn = (ImageButton) theView.findViewById(R.id.powerBtn);

        powerBtn.setImageResource(R.drawable.power_red);
        powerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                powerBtn.setImageResource(R.drawable.power_green);
            }
        });

        final ImageButton renameBtn = (ImageButton) theView.findViewById(R.id.renameBtn);
        renameBtn.setTag(position);
        renameBtn.setImageResource(R.drawable.rename);
        renameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position=(Integer)view.getTag();
                String msg = "You clicked rename at position: " + position;
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });

        return theView;
    }
}