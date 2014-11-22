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
import com.pdb.zhome.Devices.Device;
import com.pdb.zhome.HashMapHelper;
import com.pdb.zhome.Activities.MainActivity;
import com.pdb.zhome.SocketCom;

import org.json.JSONObject;

import java.util.HashMap;

public class lightsScreenAdapter extends ArrayAdapter<String> {

    private HashMap<String, Device> deviceHashMap;
    private SocketCom socketCom;
    private int lightsOn;
    public lightsScreenAdapter(Context context, String[] values) {
        super(context, R.layout.row_layout_lights, values);
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        deviceHashMap = MainActivity.getHashMap();
        socketCom = SocketCom.getInstance();
        LayoutInflater theInflater = LayoutInflater.from(getContext());

        View theView;

        final String rowItem = getItem(position);

        String status = HashMapHelper.getStatus(rowItem);

        if (deviceHashMap.get(rowItem).getValues().keySet().contains("37")){
            // LIGHT SWITCH

            // Set the view to row layout for lights
            theView = theInflater.inflate(R.layout.row_layout_lights, parent, false);

            TextView theTextView = (TextView) theView.findViewById(R.id.lightsScreenText);

            theTextView.setText(deviceHashMap.get(rowItem).getDevName());

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
                    String status = HashMapHelper.getStatus(rowItem);

                    if (status.equals("0") || status.equals("false")){

                        command = deviceHashMap.get(rowItem).command("37", "1");
                        socketCom.sendMessage(command);
                        powerBtn.setImageResource(R.drawable.power_green);
                    }
                    else {
                        command = deviceHashMap.get(rowItem).command("37", "0");
                        socketCom.sendMessage(command);
                        powerBtn.setImageResource(R.drawable.power_red);
                    }
                }

            });

        }else {
            // DIMMER

            // Set the view to row layout for lights
            theView = theInflater.inflate(R.layout.row_layout_dimmer, parent, false);

            TextView theTextView = (TextView) theView.findViewById(R.id.dimmerScreenText);
            theTextView.setText(deviceHashMap.get(rowItem).getDevName());
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

                    command = deviceHashMap.get(rowItem).command("38", progressChanged.toString());

                    socketCom.sendMessage(command);

                }
            });

        }

        return theView;
    }
}