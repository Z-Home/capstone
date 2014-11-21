package com.pdb.zhome;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.miz.pdb.R;

import java.util.ArrayList;

/**
 * Created by Vegie on 11/18/14.
 */

public class FavoritesAddScreen extends Fragment {

    public FavoritesAddScreen() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.screen_favorites_add, container, false);

        String[] deviceNames = HashMapHelper.getAllDeviceNames();

        ListAdapter favAddScreenAdapter = new favoritesAddAdapter(getActivity(), deviceNames);

        ListView lightsListView = (ListView) rootView.findViewById(R.id.favoritesAddListView);

        lightsListView.setAdapter(favAddScreenAdapter);

        return rootView;
    }



}