package com.pdb.zhome.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.miz.pdb.R;
import com.pdb.zhome.Adapters.favoritesAddAdapter;
import com.pdb.zhome.HashMapHelper;

/**
 * Created by Vegie on 11/18/14.
 */

public class FavoritesAddScreen extends Fragment {

    public FavoritesAddScreen() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.screen_favorites_add, container, false);

        String[] deviceIds = HashMapHelper.getAllDeviceIds();

        String[] deviceNames = HashMapHelper.getAllDeviceNames();

        ListAdapter favAddScreenAdapter = new favoritesAddAdapter(getActivity(), deviceNames);

        ListView favoritesAddListView = (ListView) rootView.findViewById(R.id.favoritesAddListView);

        favoritesAddListView.setAdapter(favAddScreenAdapter);

        return rootView;
    }



}