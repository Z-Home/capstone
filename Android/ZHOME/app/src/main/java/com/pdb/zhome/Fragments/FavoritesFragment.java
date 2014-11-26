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
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.miz.pdb.R;
import com.pdb.zhome.Activities.MainActivity;
import com.pdb.zhome.Adapters.favoritesCustomAdapter;
import com.pdb.zhome.Devices.Device;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;

public class FavoritesFragment extends Fragment {

    private Fragment screen;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String strJson;
    private JSONArray favoritesJsonArray;

	public FavoritesFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_favorites, container, false);

        preferences = getActivity().getSharedPreferences("ZhomePreferences", Context.MODE_PRIVATE);
        editor = preferences.edit();

        //Get favorites from shared preferences as a json array
        favoritesJsonArray = new JSONArray();
        if(preferences.getString("favorites", null) != null){
            strJson = preferences.getString("favorites", null);
            System.out.println("FIRST GETTING FAVORITES: " + strJson);
            try {
                favoritesJsonArray = new JSONArray(strJson);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            favoritesJsonArray = new JSONArray();
            strJson = favoritesJsonArray.toString();
            System.out.println(strJson);
            editor.putString("favorites", strJson);
            editor.commit();
        }

        int len = favoritesJsonArray.length();

        //Json array to String array
        String[] devices = new String[len];
        int i = 0;
        for(i = 0; i < len; i++){
            try {
                devices[i] = favoritesJsonArray.getString(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ImageButton addFavoritesBtn = (ImageButton) rootView.findViewById(R.id.addFavoritesBtn);
        addFavoritesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                screen = new FavoritesAddScreen();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, screen).addToBackStack(null).commit();
                getActivity().getActionBar().setTitle("Add to Favorites");
            }
        });

        ListAdapter favoritesListAdapter = new favoritesCustomAdapter(getActivity(), devices);

        ListView favoritesListView = (ListView) rootView.findViewById(R.id.favoritesListView);

        favoritesListView.setAdapter(favoritesListAdapter);

        favoritesListView.setOnItemClickListener(new  AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(MainActivity.getHashMap().get(String.valueOf(adapterView.getItemAtPosition(i))).getValues().keySet().contains("66")) {
                    HashMap<String, Device> deviceHashMap = MainActivity.getHashMap();
                    String thermostatPicked = String.valueOf(adapterView.getItemAtPosition(i));
                    Fragment thermostatView = new ThermostatView();
                    FragmentManager fragmentManager = getFragmentManager();
                    Bundle args = new Bundle();
                    args.putString("devNum", thermostatPicked);
                    thermostatView.setArguments(args);
                    fragmentManager.beginTransaction().replace(R.id.content_frame, thermostatView, "ThermostatView").addToBackStack(null).commit();
                    MainActivity.setCurrentFragment(thermostatView);
                    getActivity().getActionBar().setTitle(deviceHashMap.get(thermostatPicked).getDevName());
                }
            }
        });

		return rootView;
	}

}
