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

class lightsScreenAdapter extends ArrayAdapter<String> {

    private HashMap<String, Device> deviceHashMap;
    private SocketCom socketCom;
    public lightsScreenAdapter(Context context, String[] values) {
        super(context, R.layout.row_layout_favorites, values);
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        deviceHashMap = MainActivity.getHashMap();
        socketCom = SocketCom.getInstance();
        LayoutInflater theInflater = LayoutInflater.from(getContext());

        View theView = theInflater.inflate(R.layout.row_layout_lights, parent, false);

        final String favoriteItem = getItem(position);

        String status = HashMapHelper.getStatus(favoriteItem);
        System.out.println(status + ": this is the items status");

        TextView theTextView = (TextView) theView.findViewById(R.id.lightsScreenText);

        theTextView.setText(deviceHashMap.get(favoriteItem).getDevName());

        ImageView theImageView = (ImageView) theView.findViewById(R.id.lightsScreenImageView);

        theImageView.setImageResource(R.drawable.sensors);

        final ImageButton powerBtn = (ImageButton) theView.findViewById(R.id.powerBtn);

        if (status.equals("0") || status.equals("false")){
            powerBtn.setImageResource(R.drawable.power_red);
        }
        else
            powerBtn.setImageResource(R.drawable.power_green);

        powerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deviceHashMap = MainActivity.getHashMap();
                String status = HashMapHelper.getStatus(favoriteItem);
                System.out.println("The status of this device on click is: " + status);
                if (status.equals("0") || status.equals("false")){
                    JSONObject command;
                    if (deviceHashMap.get(favoriteItem).getValues().keySet().contains("38")) {
                        command = deviceHashMap.get(favoriteItem).command("38", "99");
                    }
                    else
                        command = deviceHashMap.get(favoriteItem).command("37", "1");
                    socketCom.sendMessage(command);
                    powerBtn.setImageResource(R.drawable.power_green);
                }
                else {
                    JSONObject command;
                    if (deviceHashMap.get(favoriteItem).getValues().keySet().contains("38")) {
                        command = deviceHashMap.get(favoriteItem).command("38", "0");
                    }
                    else
                        command = deviceHashMap.get(favoriteItem).command("37", "0");
                    socketCom.sendMessage(command);
                    powerBtn.setImageResource(R.drawable.power_red);
                }
            }
        });

        final ImageButton renameBtn = (ImageButton) theView.findViewById(R.id.renameBtn);
        renameBtn.setTag(position);
        renameBtn.setImageResource(R.drawable.rename);
        renameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position=(Integer)view.getTag();
                String msg = "You clicked rename at position: " + position;
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });

        return theView;
    }
}