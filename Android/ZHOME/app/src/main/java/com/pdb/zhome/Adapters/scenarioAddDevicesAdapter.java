package com.pdb.zhome.Adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.miz.pdb.R;
import com.pdb.zhome.Activities.MainActivity;
import com.pdb.zhome.Devices.Device;
import com.pdb.zhome.Fragments.ScenariosAddDevicesScreen;
import com.pdb.zhome.HashMapHelper;
import com.pdb.zhome.SocketCom;

import java.util.ArrayList;
import java.util.HashMap;

public class scenarioAddDevicesAdapter extends ArrayAdapter<String> {

    private HashMap<String, Device> deviceHashMap;
    private SocketCom socketCom;
    private String changedName;
    private FragmentManager fragmentManager;

    public scenarioAddDevicesAdapter(Context context, String[] values, FragmentManager fragmentManager) {
        super(context, R.layout.row_layout_room_devices_add, values);
        this.fragmentManager = fragmentManager;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        deviceHashMap = MainActivity.getHashMap();
        socketCom = SocketCom.getInstance();

        LayoutInflater theInflater = LayoutInflater.from(getContext());

        //Uses the same room layout row.. no need for a new one
        View theView = theInflater.inflate(R.layout.row_layout_room_devices_add, parent, false);

        final String item = getItem(position);

        String type = HashMapHelper.getType(item);

        TextView theEditText = (TextView) theView.findViewById(R.id.roomAddDevicesTextView);

        theEditText.setText(deviceHashMap.get(item).getDevName());
        theEditText.setId(Integer.parseInt(deviceHashMap.get(item).getDevNum()));


        ImageView theImageView = (ImageView) theView.findViewById(R.id.roomAddDevicesImageView);

        if (type.equals("switch")) {
            theImageView.setImageResource(R.drawable.lights);
        } else if (type.equals("thermostat")) {
            theImageView.setImageResource(R.drawable.thermostat);
        } else if (type.equals("sensor")) {
            theImageView.setImageResource(R.drawable.sensors);
        } else {
            theImageView.setImageResource(R.drawable.ic_launcher);
        }

        final ImageButton deviceAddBtn = (ImageButton) theView.findViewById(R.id.roomAddDevicesBtn);
        deviceAddBtn.setImageResource(R.drawable.favorites_false);

        deviceAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScenariosAddDevicesScreen frg = (ScenariosAddDevicesScreen) fragmentManager.findFragmentByTag("scenario_add_devices");
                if (frg.containsDevice(item)) {
                    frg.removeFromSelectedDevices(item);
                    deviceAddBtn.setImageResource(R.drawable.favorites_false);
                }
                else {
                    frg.addToSelectedDevices(item);
                    deviceAddBtn.setImageResource(R.drawable.favorites_true);
                }
            }
        });

        //Check if device is already in the room...
        // if true then set check mark icon
        // if not then set add icon to (roomsAddDevicesBtn)

        //Also handle pressing the add icon ((roomsAddDevicesBtn)) to add/remove device to/from room


        return theView;
    }
}