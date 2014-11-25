package com.pdb.zhome.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
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

public class ScenariosFragment extends Fragment {

    private Fragment screen;

	public ScenariosFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_scenarios, container, false);

        //Sample Data only
        String[] favoritesListArray2 = {
                "Scenario 1",
                "Scenario 2",
                "Scenario 3",
        };

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


        ListAdapter scenariosListAdapter = new scenariosCustomAdapter(getActivity(), favoritesListArray2);

        ListView scenariosListView = (ListView) rootView.findViewById(R.id.scenariosListView);

        scenariosListView.setAdapter(scenariosListAdapter);

        scenariosListView.setOnItemClickListener(new  AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String favoriteItemPicked = "You have selected " + String.valueOf(adapterView.getItemAtPosition(i));
                Toast.makeText(getActivity(), favoriteItemPicked, Toast.LENGTH_SHORT).show();
            }
        });


        return rootView;
	}

}
