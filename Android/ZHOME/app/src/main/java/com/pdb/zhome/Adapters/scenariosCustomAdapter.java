package com.pdb.zhome.Adapters;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.miz.pdb.R;

public class scenariosCustomAdapter extends ArrayAdapter<String> {
    public scenariosCustomAdapter(Context context, String[] values) {
        super(context, R.layout.row_layout_scenarios, values);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater theInflater = LayoutInflater.from(getContext());

        View theView = theInflater.inflate(R.layout.row_layout_scenarios, parent, false);

        String rowItem = getItem(position);

        TextView theTextView = (TextView) theView.findViewById(R.id.scenariosNameText);

        theTextView.setText(rowItem);


        //Scenario Activate Button
        ImageButton theImageView = (ImageButton) theView.findViewById(R.id.scenarioActivateBtn);
        theImageView.setImageResource(R.drawable.power);

        return theView;
    }
}