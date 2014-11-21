package com.pdb.zhome;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.miz.pdb.R;

import java.util.ArrayList;

public class SummaryFragment extends Fragment {

	public SummaryFragment() {
	}

    private Fragment screen;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		final View rootView = inflater.inflate(R.layout.fragment_summary2, container, false);

        ArrayList<String> lightsDevicesList = new ArrayList<String>(HashMapHelper.getDeviceNames("switch"));

        // Set Lights Status
        TextView lightsStatus = (TextView) rootView.findViewById(R.id.lightsStatusTxt);
        Integer onLights = HashMapHelper.getSwitchesOn();
        Integer totalLights = lightsDevicesList.size();
        lightsStatus.setText(onLights.toString() + " / " + totalLights.toString());


        // Lights Button
        ImageButton lightsBtn = (ImageButton) rootView.findViewById(R.id.lightsBtn);
        lightsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                screen = new LightsScreen();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, screen).addToBackStack(null).commit();
                getActivity().getActionBar().setTitle("Lights");
                MainActivity.setCurrentFragment(screen);
            }
        });

        // Sensors Button
        ImageButton sensorsBtn = (ImageButton) rootView.findViewById(R.id.sensorsBtn);
        sensorsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                screen = new LightsScreen();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, screen).addToBackStack(null).commit();
                getActivity().getActionBar().setTitle("Sensors");
            }
        });

        // Security Button
        ImageButton securityBtn = (ImageButton) rootView.findViewById(R.id.securityBtn);
        securityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                screen = new LightsScreen();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, screen).addToBackStack(null).commit();
                getActivity().getActionBar().setTitle("Security");
            }
        });

        // Outlets Button
        ImageButton outletsBtn = (ImageButton) rootView.findViewById(R.id.outletBtn);
        outletsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                screen = new LightsScreen();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, screen).addToBackStack(null).commit();
                getActivity().getActionBar().setTitle("Outlets");
            }
        });

        // Thermostat Button
        ImageButton thermostatBtn = (ImageButton) rootView.findViewById(R.id.thermostatBtn);
        thermostatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                screen = new ThermostatListScreen();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, screen).addToBackStack(null).commit();
                getActivity().getActionBar().setTitle("Thermostat");
            }
        });

        // Rename Devices Button
        ImageButton renameBtn = (ImageButton) rootView.findViewById(R.id.renameBtn);
        renameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                screen = new RenameScreen();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, screen, "Rename").addToBackStack(null).commit();
                getActivity().getActionBar().setTitle("Rename Devices");
            }
        });
        return rootView;
	}


}
