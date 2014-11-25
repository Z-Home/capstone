package com.pdb.zhome.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.miz.pdb.R;
import com.pdb.zhome.Activities.MainActivity;
import com.pdb.zhome.Devices.Device;
import com.pdb.zhome.HashMapHelper;
import com.pdb.zhome.SocketCom;

import org.json.JSONObject;

import java.util.HashMap;

public class favoritesCustomAdapter extends ArrayAdapter<String> {
    private HashMap<String, Device> deviceHashMap;
    private SocketCom socketCom;

    public favoritesCustomAdapter(Context context, String[] values) {
        super(context, R.layout.row_layout_favorites, values);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        deviceHashMap = MainActivity.getHashMap();
        socketCom = SocketCom.getInstance();
        LayoutInflater theInflater = LayoutInflater.from(getContext());

        //
        // A bunch of if statements to check for command class and set the correct row layout
        // (Just like customLightsAdapter)


        View theView = theInflater.inflate(R.layout.row_layout_favorites, parent, false);

        final String favoriteItem = getItem(position);

        String status = HashMapHelper.getStatus(favoriteItem);

        if (deviceHashMap.get(favoriteItem).getValues().keySet().contains("37")){
            // LIGHT SWITCH

            // Set the view to row layout for lights
            theView = theInflater.inflate(R.layout.row_layout_lights, parent, false);

            TextView theTextView = (TextView) theView.findViewById(R.id.lightsScreenText);

            theTextView.setText(deviceHashMap.get(favoriteItem).getDevName());

            ImageView theImageView = (ImageView) theView.findViewById(R.id.lightsScreenImageView);

            theImageView.setImageResource(R.drawable.lights);

            final ImageButton powerBtn = (ImageButton) theView.findViewById(R.id.powerBtn);

            if (status.equals("0") || status.equals("false")){
                powerBtn.setImageResource(R.drawable.power_red);
            }
            else {
                powerBtn.setImageResource(R.drawable.power_green);
            }

            powerBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    JSONObject command;

                    deviceHashMap = MainActivity.getHashMap();
                    String status = HashMapHelper.getStatus(favoriteItem);

                    if (status.equals("0") || status.equals("false")){

                        command = deviceHashMap.get(favoriteItem).command("37", "1");
                        socketCom.sendMessage(command);
                        powerBtn.setImageResource(R.drawable.power_green);
                    }
                    else {
                        command = deviceHashMap.get(favoriteItem).command("37", "0");
                        socketCom.sendMessage(command);
                        powerBtn.setImageResource(R.drawable.power_red);
                    }
                }

            });

        }else if(deviceHashMap.get(favoriteItem).getValues().keySet().contains("38")){
            // DIMMER

            // Set the view to row layout for lights
            theView = theInflater.inflate(R.layout.row_layout_dimmer, parent, false);

            TextView theTextView = (TextView) theView.findViewById(R.id.dimmerScreenText);
            theTextView.setText(deviceHashMap.get(favoriteItem).getDevName());
            ImageView theImageView = (ImageView) theView.findViewById(R.id.dimmerScreenImageView);
            theImageView.setImageResource(R.drawable.dimmer);

            // Declare dimmer slider
            SeekBar dimmerSlider = (SeekBar) theView.findViewById(R.id.dimmerSeekBar);

            // Convert status to integer
            dimmerSlider.setProgress(Integer.parseInt(status));

            dimmerSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                // Slider progress tracker
                Integer progressChanged = 0;

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                    progressChanged = progress;

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    JSONObject command;

                    command = deviceHashMap.get(favoriteItem).command("38", progressChanged.toString());

                    socketCom.sendMessage(command);

                }
            });

        }else if(deviceHashMap.get(favoriteItem).getValues().keySet().contains("66")){
            theView = theInflater.inflate(R.layout.row_layout_thermostat, parent, false);

            TextView theTextView = (TextView) theView.findViewById(R.id.thermostatScreenText);

            theTextView.setText(favoriteItem);

            ImageView theImageView = (ImageView) theView.findViewById(R.id.thermostatScreenImageView);

            theImageView.setImageResource(R.drawable.thermostat);

        }else if(deviceHashMap.get(favoriteItem).getValues().keySet().contains("48")){
            theView = theInflater.inflate(R.layout.row_layout_thermostat, parent, false);

            TextView theTextView = (TextView) theView.findViewById(R.id.thermostatScreenText);

            theTextView.setText(favoriteItem);

            ImageView theImageView = (ImageView) theView.findViewById(R.id.thermostatScreenImageView);

            theImageView.setImageResource(R.drawable.thermostat);

        }

        return theView;
    }
}