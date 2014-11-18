package com.pdb.zhome;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.miz.pdb.R;

import org.json.JSONObject;

import java.util.HashMap;

class renameScreenAdapter extends ArrayAdapter<String> {

    private String[] changedNameList;
    private HashMap<String, Device> deviceHashMap;
    private SocketCom socketCom;
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

        EditText theEditText = (EditText) theView.findViewById(R.id.renameEditText);

        theEditText.setText(deviceHashMap.get(item).getDevName());

        theEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                changedNameList[position] = s.toString();
                System.out.println(s.toString());
                final String changedName = s.toString();
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