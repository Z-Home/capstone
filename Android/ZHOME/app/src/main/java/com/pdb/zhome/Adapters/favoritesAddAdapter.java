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
    JSONArray favoritesJsonArray = new JSONArray();
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
        String strJson;
        if(preferences.getString("favorites", null) != null){
            strJson = preferences.getString("favorites", null);
            System.out.println("IN FAVORITES ADD: " + strJson);
            System.out.println(strJson);
            try {
                favoritesJsonArray = new JSONArray(strJson);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            favoritesJsonArray = new JSONArray();
            strJson = favoritesJsonArray.toString();
            System.out.println(strJson);
            editor.putString("favorites", strJson);
            editor.commit();
        }

        final ImageButton favBtn = (ImageButton) theView.findViewById(R.id.favoritesIconBtn);
        //Check the whether devices are in set as favorites or not
        //and set the icon based on that check
        try {
            int i = 0;
            boolean isDeviceInFavorites = false;
            for(i = 0; i < favoritesJsonArray.length(); i++){
                System.out.println("ROW ITEM: " + rowItem + ", FAVORITES ARRAY: " + favoritesJsonArray.getString(i));
                if(favoritesJsonArray.getString(i).equals(rowItem)){
                    isDeviceInFavorites = true;
                }
            }
            System.out.println("IN FAVORITES? " + isDeviceInFavorites);
            if(isDeviceInFavorites) {
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
                    int i = 0;
                    boolean isDeviceInFavorites = false;
                    for(i = 0; i < favoritesJsonArray.length(); i++){
                        if(favoritesJsonArray.getString(i).equals(rowItem)){
                            isDeviceInFavorites = true;
                        }
                    }
                    //If already in favorites, remove from favorites and change the image resource
                    if(isDeviceInFavorites){
                        for(i = 0; i < favoritesJsonArray.length(); i++){
                            if(rowItem.equals(favoritesJsonArray.getString(i))){
                                favoritesJsonArray.remove(i);
                            }
                        }
                        favBtn.setImageResource(R.drawable.favorites_false);
                    //Else add to favorites and change the image resource
                    }else{
                        favoritesJsonArray.put(rowItem);
                        favBtn.setImageResource(R.drawable.favorites_true);
                    }
                    System.out.println("FAVORITES: " + favoritesJsonArray.toString());
                    editor.putString("favorites", favoritesJsonArray.toString());
                    editor.commit();
                    System.out.println("FAVORITES IN PREFERENCES: " + preferences.getString("favorites", null));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

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