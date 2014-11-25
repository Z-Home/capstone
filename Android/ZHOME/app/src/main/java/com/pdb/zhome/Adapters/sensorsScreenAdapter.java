package com.pdb.zhome.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.miz.pdb.R;
import com.pdb.zhome.Devices.Device;
import com.pdb.zhome.HashMapHelper;
import com.pdb.zhome.SocketCom;

import java.util.ArrayList;
import java.util.HashMap;

public class sensorsScreenAdapter extends ArrayAdapter<String> {

    private HashMap<String, Device> deviceHashMap;
    private HashMap<String, String> hm;
    private SocketCom socketCom;
    private int lightsOn;

    public sensorsScreenAdapter(Context context, ArrayList<String> values, HashMap<String, String> hm) {
        super(context, R.layout.row_layout_sensors, values);
        this.hm = hm;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater theInflater = LayoutInflater.from(getContext());

        View theView = theInflater.inflate(R.layout.row_layout_sensors, parent, false);

        final String rowItem = getItem(position);

        //Set the device name
        TextView theTextView = (TextView) theView.findViewById(R.id.sensorDeviceNameText);
        theTextView.setText(rowItem);

        TextView textView = (TextView) theView.findViewById(R.id.sensorDeviceStatusText);
        textView.setText(hm.get(rowItem));

        //Set the image view of the sensor
        ImageView theImageView = (ImageView) theView.findViewById(R.id.sensorDeviceImageView);
        theImageView.setImageResource(R.drawable.sensors);

        return theView;
    }
}