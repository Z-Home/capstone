package com.pdb.zhome.Adapters;

import android.app.FragmentManager;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.miz.pdb.R;
import com.pdb.zhome.Activities.MainActivity;
import com.pdb.zhome.Communicator;
import com.pdb.zhome.Devices.Device;
import com.pdb.zhome.HashMapHelper;
import com.pdb.zhome.Room;
import com.pdb.zhome.SocketCom;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class roomAddDevicesAdapter extends ArrayAdapter<String> {

    private HashMap<String, Device> deviceHashMap;
    private SocketCom socketCom;
    private String changedName;
    private String room;
    private FragmentManager fragmentManager;
    public roomAddDevicesAdapter(Context context, String[] values, String room, FragmentManager fragmentManager) {
        super(context, R.layout.row_layout_room_devices_add, values);
        this.room = room;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        deviceHashMap = MainActivity.getHashMap();

        socketCom = SocketCom.getInstance();

        LayoutInflater theInflater = LayoutInflater.from(getContext());

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
        deviceAddBtn.setImageResource(R.drawable.add);


        deviceAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Room> roomHashMap = MainActivity.getRoomsHashMap();
                Room[] rooms = roomHashMap.values().toArray(new Room[roomHashMap.size()]);
                int length = rooms.length;
                int i = 0;
                JSONArray roomsJsonArray = new JSONArray();
                JSONObject finalJson = new JSONObject();
                for(i = 0; i < length; i++){
                    System.out.println(rooms[i].getName());
                    if (rooms[i].getName().equals(room)) {
                        String[] devices = rooms[i].getDevices();
                        ArrayList<String> arrayList = new ArrayList<String>();
                        for (int k = 0; k < devices.length; k++) {
                            arrayList.add(devices[k]);
                        }
                        arrayList.add(item);
                        String[] dev = arrayList.toArray(new String[arrayList.size()]);
                        rooms[i].setDevices(dev);
                    }

                    JSONObject roomJson = new JSONObject();
                    JSONArray devicesJsonArray = new JSONArray();
                    String[] device = rooms[i].getDevices();
                    int j = 0;
                    for(j = 0; j < device.length; j++){
                        devicesJsonArray.put(device[j]);
                    }
                    try {
                        roomJson.put("name", rooms[i].getName());
                        roomJson.put("devices", devicesJsonArray);
                        roomJson.put("type", rooms[i].getStringType());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    roomsJsonArray.put(roomJson);

                    JSONObject roomsJsonObject = new JSONObject();

                    try {
                        roomsJsonObject.put("rooms", roomsJsonArray);
                        finalJson.put("Type", "Rooms");
                        finalJson.put("Json", roomsJsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                SocketCom.getInstance().sendMessage(finalJson);
                fragmentManager.popBackStack();
            }
        });

        //Check if device is already in the room...
        // if true then set check mark icon
        // if not then set add icon to (roomsAddDevicesBtn)

        //Also handle pressing the add icon ((roomsAddDevicesBtn)) to add/remove device to/from room


        return theView;
    }
}