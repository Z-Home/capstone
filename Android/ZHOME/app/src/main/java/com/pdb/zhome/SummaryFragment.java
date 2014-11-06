package com.pdb.zhome;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.miz.pdb.R;

public class SummaryFragment extends Fragment {

	public SummaryFragment() {
	}

    private Fragment screen;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		final View rootView = inflater.inflate(R.layout.fragment_summary, container, false);

        // Lights Button
        ImageButton lightsBtn = (ImageButton) rootView.findViewById(R.id.lightsBtn);
        lightsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                screen = new LightsScreen();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, screen).addToBackStack(null).commit();
                getActivity().getActionBar().setTitle("Lights");
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

                screen = new LightsScreen();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, screen).addToBackStack(null).commit();
                getActivity().getActionBar().setTitle("Thermostat");
            }
        });
		return rootView;
	}


}
