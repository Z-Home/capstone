package com.pdb.zhome.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.miz.pdb.R;
import com.pdb.zhome.Activities.MainActivity;
import com.pdb.zhome.Adapters.scenarioSetupAdapter;
import com.pdb.zhome.Devices.Device;
import com.pdb.zhome.HashMapHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class ScenarioSetupScreen extends Fragment {
    private HashMap<String, Device> deviceHashMap;
    private JSONArray jsonArray;

    public ScenarioSetupScreen() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        final View rootView = inflater.inflate(R.layout.screen_scenarios_setup, container, false);

        jsonArray = new JSONArray();

        Bundle bundle = getArguments();
        ArrayList<String> selectedDevices = new ArrayList<String>(bundle.getStringArrayList("device_list"));
        final String scenarioName = bundle.getString("scenario_name");
        String[] deviceNames = selectedDevices.toArray(new String[selectedDevices.size()]);

        final ListAdapter scenariosSetupAdapter = new scenarioSetupAdapter(getActivity(), deviceNames, getFragmentManager());

        final ListView scenariosSetupListView = (ListView) rootView.findViewById(R.id.scenariosSetupListView);

        scenariosSetupListView.setAdapter(scenariosSetupAdapter);

        Button saveSettingsBtn = (Button) rootView.findViewById(R.id.saveSettings);
        saveSettingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = getActivity().getApplicationContext().getSharedPreferences("Scenarios", Context.MODE_PRIVATE);
                String scenarios = preferences.getString("scenarios", null);
                SharedPreferences.Editor editor = preferences.edit();

                JSONObject scen = null;
                JSONArray s = new JSONArray();
                try {
                    scen = new JSONObject(scenarios);
                    s = scen.getJSONArray("Scenarios");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONObject object = new JSONObject();
                JSONArray commandsArray = new JSONArray();

                try {
                    object.put("Name", scenarioName);
                    object.put("Commands", jsonArray);
                    s.put(object);

                    scen.put("Scenarios", s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                editor.putString("scenarios", scen.toString());
                editor.commit();

                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragmentManager.findFragmentByTag("scenariosFragment")).addToBackStack(null).commit();

            }
        });

        return rootView;
    }

    public void commandArray(JSONObject obj){
        jsonArray.put(obj);
    }

}
