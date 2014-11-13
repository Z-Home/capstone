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

import org.json.JSONObject;

import java.util.HashMap;

class renameScreenAdapter extends ArrayAdapter<String> {

    private HashMap<String, Device> deviceHashMap;
    private SocketCom socketCom;
    public renameScreenAdapter(Context context, String[] values) {
        super(context, R.layout.row_layout_favorites, values);
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        deviceHashMap = MainActivity.getHashMap();
        socketCom = SocketCom.getInstance();
        LayoutInflater theInflater = LayoutInflater.from(getContext());

        View theView = theInflater.inflate(R.layout.row_layout_favorites, parent, false);

        final String item = getItem(position);

        String type = HashMapHelper.getType(item);

        TextView theTextView = (TextView) theView.findViewById(R.id.favoritesTextView);

        theTextView.setText(deviceHashMap.get(item).getDevName());

        ImageView theImageView = (ImageView) theView.findViewById(R.id.favoritesImageView);

        if (type.equals("switch")) {
            theImageView.setImageResource(R.drawable.lights);

        } else if (type.equals("thermostat")) {
            theImageView.setImageResource(R.drawable.thermostat);

        } else if (type.equals("sensor")) {
            theImageView.setImageResource(R.drawable.sensors);

        } else {
        }


        return theView;
    }
}