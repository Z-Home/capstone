package com.pdb.zhome.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.miz.pdb.R;
import com.pdb.zhome.Activities.MainActivity;
import com.pdb.zhome.Adapters.scenariosCustomAdapter;
import com.pdb.zhome.SocketCom;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ScenariosFragment extends Fragment {

    private Fragment screen;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
	public ScenariosFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_scenarios, container, false);

        preferences = getActivity().getApplicationContext().getSharedPreferences("Scenarios", Context.MODE_PRIVATE);
        final String scenarios = preferences.getString("scenarios", null);
        editor = preferences.edit();

        ArrayList<String> arrayList = new ArrayList<String>();
        if (scenarios == null) {
            JSONObject scen = new JSONObject();
            JSONArray array = new JSONArray();
            JSONObject object = new JSONObject();
            JSONArray commandsArray = new JSONArray();

            try {
                object.put("Name", "Example");
                commandsArray.put(MainActivity.getHashMap().get("3").command("37", "0"));
                commandsArray.put(MainActivity.getHashMap().get("5").command("38", "99"));
                object.put("Commands", commandsArray);
                array.put(object);

                scen.put("Scenarios", array);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            editor.putString("scenarios", scen.toString());
            editor.commit();
        } else {
            try {
                JSONObject scen = new JSONObject(scenarios);
                JSONArray s = scen.getJSONArray("Scenarios");
                for (int i = 0; i < s.length(); i++){
                    JSONObject j = s.getJSONObject(i);
                    arrayList.add(j.getString("Name"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            System.out.println(scenarios);
//            editor.remove("scenarios");
//            editor.commit();
        }

        String[] listArray = arrayList.toArray(new String[arrayList.size()]);


        ListAdapter scenariosListAdapter = new scenariosCustomAdapter(getActivity(), listArray);

        ListView scenariosListView = (ListView) rootView.findViewById(R.id.scenariosListView);

        scenariosListView.setAdapter(scenariosListAdapter);

        scenariosListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String run = String.valueOf(adapterView.getItemAtPosition(i));
                try {
                    JSONObject scen = new JSONObject(scenarios);
                    JSONArray s = scen.getJSONArray("Scenarios");
                    for (int j = 0; j < s.length(); j++){
                        JSONObject k = s.getJSONObject(j);
                        if (run.equals(k.getString("Name"))){
                            JSONArray array = k.getJSONArray("Commands");
                            for(int m = 0; m < array.length(); m++){
                                JSONObject message = array.getJSONObject(m);
                                SocketCom.getInstance().sendMessage(message);
                            }
                            break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        ImageButton addScenarioBtn = (ImageButton) rootView.findViewById(R.id.addScenarioBtn);
        addScenarioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                screen = new ScenarioAddFormScreen();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, screen).addToBackStack(null).commit();
                getActivity().getActionBar().setTitle("Add Scenario");
            }
        });

        Button btn = (Button) rootView.findViewById(R.id.testBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                screen = new ScenarioSetupScreen();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, screen).addToBackStack(null).commit();
                getActivity().getActionBar().setTitle("Scenario Setup");
            }
        });


        return rootView;
	}

}
