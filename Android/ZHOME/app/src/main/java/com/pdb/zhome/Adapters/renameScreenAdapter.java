package com.pdb.zhome.Adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.miz.pdb.R;
import com.pdb.zhome.Devices.Device;
import com.pdb.zhome.HashMapHelper;
import com.pdb.zhome.Activities.MainActivity;
import com.pdb.zhome.SocketCom;

import java.util.HashMap;

public class renameScreenAdapter extends ArrayAdapter<String> {

    private String[] changedNameList;
    private HashMap<String, Device> deviceHashMap;
    private SocketCom socketCom;
    private String changedName;
    public renameScreenAdapter(Context context, String[] values) {
        super(context, R.layout.row_layout_rename, values);
    }

    public String[] getChangedNameList(){
        return this.changedNameList;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        deviceHashMap = MainActivity.getHashMap();
        changedNameList = new String[deviceHashMap.size()];
        socketCom = SocketCom.getInstance();

        LayoutInflater theInflater = LayoutInflater.from(getContext());

        View theView = theInflater.inflate(R.layout.row_layout_rename, parent, false);

        final String item = getItem(position);

        String type = HashMapHelper.getType(item);

        TextView theEditText = (TextView) theView.findViewById(R.id.renameEditText);

        theEditText.setText(deviceHashMap.get(item).getDevName());
        theEditText.setId(Integer.parseInt(deviceHashMap.get(item).getDevNum()));


        theEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                changedNameList[position] = s.toString();
                System.out.println(s.toString());
                changedName = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ImageView theImageView = (ImageView) theView.findViewById(R.id.renameImageView);

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