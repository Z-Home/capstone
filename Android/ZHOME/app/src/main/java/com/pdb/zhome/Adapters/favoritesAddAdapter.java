package com.pdb.zhome.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.miz.pdb.R;
import com.pdb.zhome.Devices.Device;
import com.pdb.zhome.HashMapHelper;
import com.pdb.zhome.Activities.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class favoritesAddAdapter extends ArrayAdapter<String> {

    private HashMap<String, Device> deviceHashMap;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    JSONObject jo = new JSONObject();
    JSONArray ja = new JSONArray();
    JSONObject mainObj = new JSONObject();
    JSONObject favoritesJo = new JSONObject();

    public favoritesAddAdapter(Context context, String[] values) {
        super(context, R.layout.row_layout_favorites_add, values);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        preferences = getContext().getSharedPreferences("ZhomePreferences", Context.MODE_PRIVATE);
        editor = preferences.edit();

        deviceHashMap = MainActivity.getHashMap();

        LayoutInflater theInflater = LayoutInflater.from(getContext());

        View theView = theInflater.inflate(R.layout.row_layout_favorites_add, parent, false);

        final String rowItem = getItem(position);

        String type = HashMapHelper.getType(rowItem);

        TextView theTextView = (TextView) theView.findViewById(R.id.favoritesAddTextView);

        theTextView.setText(deviceHashMap.get(rowItem).getDevName());
        theTextView.setId(Integer.parseInt(deviceHashMap.get(rowItem).getDevNum()));

        ImageView theImageView = (ImageView) theView.findViewById(R.id.favoritesAddListImageView);

        // Get JSON from shared preferences
        String strJson = preferences.getString("favorites", "favorites");
        if(strJson != null) try {
            favoritesJo = new JSONObject(strJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final ImageButton favBtn = (ImageButton) theView.findViewById(R.id.favoritesIconBtn);
        //Check the whether devices are in set as favorites or not
        //and set the icon based on that check
        try {
            if(favoritesJo.getString(deviceHashMap.get(rowItem).getDevNum()) != null) {
                favBtn.setImageResource(R.drawable.favorites_true);
            }else{
                favBtn.setImageResource(R.drawable.favorites_false);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    if(favoritesJo.getString(deviceHashMap.get(rowItem).getDevNum()) != null)

                    try {

                        jo.put(deviceHashMap.get(rowItem).getDevNum(), deviceHashMap.get(rowItem).getDevName());

                    } catch (JSONException e) {throw new RuntimeException(e);}
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //put into array
                ja.put(jo);

                //put into main object
                try {
                    mainObj.put("favorites", ja);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                favBtn.setImageResource(R.drawable.favorites_false);

            }
        });

        //store json data as string in preferences
        editor.putString("favorites", mainObj.toString());
        editor.commit();

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