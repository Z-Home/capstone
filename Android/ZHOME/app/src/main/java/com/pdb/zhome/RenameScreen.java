package com.pdb.zhome;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.miz.pdb.R;

import java.util.ArrayList;

public class RenameScreen extends Fragment{

    public RenameScreen() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.screen_rename, container, false);


        String[] deviceNames = HashMapHelper.getAllDeviceNames();


        ListAdapter renameScreenAdapter = new renameScreenAdapter(getActivity(), deviceNames);

        ListView lightsListView = (ListView) rootView.findViewById(R.id.renameScreenListView);

        lightsListView.setAdapter(renameScreenAdapter);

        return rootView;
    }

}
