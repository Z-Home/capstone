package com.pdb.zhome.Adapters;

import android.app.FragmentManager;
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
import com.pdb.zhome.Fragments.ScenarioSetupScreen;
import com.pdb.zhome.HashMapHelper;
import com.pdb.zhome.SocketCom;

import org.json.JSONObject;

import java.util.HashMap;

public class scenarioSetupAdapter extends ArrayAdapter<String> {

    private HashMap<String, Device> deviceHashMap;
    private SocketCom socketCom;
    private FragmentManager fragmentManager;
    public scenarioSetupAdapter(Context context, String[] values, FragmentManager fragmentManager) {
        super(context, R.layout.screen_scenarios_setup, values);
        this.fragmentManager = fragmentManager;
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        deviceHashMap = MainActivity.getHashMap();
        socketCom = SocketCom.getInstance();
        final ScenarioSetupScreen setup = (ScenarioSetupScreen) fragmentManager.findFragmentByTag("scenarioSetup");

        LayoutInflater theInflater = LayoutInflater.from(getContext());

        View theView = theInflater.inflate(R.layout.row_layout_favorites, parent, false);

        final String rowItem = getItem(position);

        if (deviceHashMap.get(rowItem).getValues().keySet().contains("37")){
            // LIGHT SWITCH

            // Set the view to row layout for lights
            theView = theInflater.inflate(R.layout.row_layout_lights, parent, false);

            TextView theTextView = (TextView) theView.findViewById(R.id.lightsScreenText);

            theTextView.setText(deviceHashMap.get(rowItem).getDevName());

            ImageView theImageView = (ImageView) theView.findViewById(R.id.lightsScreenImageView);

            theImageView.setImageResource(R.drawable.lights);

            final ImageButton powerBtn = (ImageButton) theView.findViewById(R.id.powerBtn);

            powerBtn.setImageResource(R.drawable.power_red);
            powerBtn.setTag("off");

            powerBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    JSONObject command;
                    deviceHashMap = MainActivity.getHashMap();

                    if (powerBtn.getTag().equals("off")){
                        command = deviceHashMap.get(rowItem).command("37", "1");
                        setup.commandArray(command);
                        powerBtn.setImageResource(R.drawable.power_green);
                        powerBtn.setTag("on");
                    }
                    else {
                        command = deviceHashMap.get(rowItem).command("37", "0");
                        setup.commandArray(command);
                        powerBtn.setImageResource(R.drawable.power_red);
                        powerBtn.setTag("off");
                    }


                }

            });

        }else if (deviceHashMap.get(rowItem).getValues().keySet().contains("38")){
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
            //dimmerSlider.setProgress(Integer.parseInt(status));

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
                    setup.commandArray(command);
                }
            });

        }else if (deviceHashMap.get(rowItem).getValues().keySet().contains("66")){
            //THERMOSTAT

//            theView = theInflater.inflate(R.layout.row_layout_thermostat, parent, false);
//
//            TextView theTextView = (TextView) theView.findViewById(R.id.thermostatScreenText);
//
//            theTextView.setText(HashMapHelper.getName(rowItem));
//
//            ImageView theImageView = (ImageView) theView.findViewById(R.id.thermostatScreenImageView);
//
//            theImageView.setImageResource(R.drawable.thermostat);
//
//            TextView tempTextView = (TextView) theView.findViewById(R.id.temperatureTxt);
//
//
//            Float floatTemp = Float.parseFloat(HashMapHelper.getTemp(rowItem).replace("°C", ""));
//            floatTemp = (9 * floatTemp)/5 + 32;
//            Integer intTemp = Math.round(floatTemp);
//
//            tempTextView.setText(intTemp.toString());

        }else{

            //SENSORS?
        }

        return theView;
    }
}