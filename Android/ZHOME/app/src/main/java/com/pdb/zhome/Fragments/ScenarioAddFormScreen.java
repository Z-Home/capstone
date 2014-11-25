package com.pdb.zhome.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.miz.pdb.R;
import com.pdb.zhome.Activities.MainActivity;
import com.pdb.zhome.Room;
import com.pdb.zhome.SocketCom;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Vegie on 11/18/14.
 */

public class ScenarioAddFormScreen extends Fragment {

    public ScenarioAddFormScreen() {
    }

    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.form_scenario_add, container, false);

        final EditText editText = (EditText) rootView.findViewById(R.id.scenarioNameText);

        Button addButton = (Button) rootView.findViewById(R.id.addScenarioFormBtn);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.getText().length() == 0) {
                    Toast.makeText(getActivity(), "Please give the Scenario a name", Toast.LENGTH_SHORT).show();
                }
                else {
                    Fragment frg = new ScenariosAddDevicesScreen();
                    FragmentManager fragmentManager = getFragmentManager();
                    Bundle args = new Bundle();
                    args.putString("scenario_name", editText.getText().toString());
                    frg.setArguments(args);
                    fragmentManager.beginTransaction().replace(R.id.content_frame, frg, "scenario_add_devices").addToBackStack(null).commit();
                    getActivity().getActionBar().setTitle("Add Devices to " + editText.getText().toString());
                    MainActivity.setCurrentFragment(frg);
                }
            }
        });

        return rootView;
    }
}