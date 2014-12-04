package com.pdb.zhome.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.miz.pdb.R;
import com.pdb.zhome.Fragments.ThermostatView;
import com.pdb.zhome.HashMapHelper;

public class ThermostatCustomAdapter extends ArrayAdapter<String> {
    public ThermostatCustomAdapter(Context context, String[] values) {
        super(context, R.layout.row_layout_thermostat, values);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater theInflater = LayoutInflater.from(getContext());

        View theView = theInflater.inflate(R.layout.row_layout_thermostat, parent, false);

        String item = getItem(position);

        TextView theTextView = (TextView) theView.findViewById(R.id.thermostatScreenText);

        theTextView.setText(HashMapHelper.getName(item));

        ImageView theImageView = (ImageView) theView.findViewById(R.id.thermostatScreenImageView);

        theImageView.setImageResource(R.drawable.thermostat);

        TextView tempTextView = (TextView) theView.findViewById(R.id.temperatureTxt);


        Float floatTemp = Float.parseFloat(HashMapHelper.getTemp(item).replace("°C", ""));
        floatTemp = (9 * floatTemp)/5 + 32;
        Integer intTemp = Math.round(floatTemp);

        tempTextView.setText(intTemp.toString() + "°");

        return theView;
    }
}