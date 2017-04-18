package com.example.apple.calendargo;

import android.app.Activity;

import android.content.Context;

import android.content.Intent;

import android.os.Bundle;

import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;

import android.view.View;

import android.view.ViewGroup;

import android.widget.AdapterView;

import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by apple on 10/21/16.
 */

public class MoreFragment extends Fragment {

    private ListView mListViewType;

    private int sizeOfTypes;

    private Activity acitivity;

    private boolean loggedIn;


    @Nullable

    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceStates) {

        View v = inflater.inflate(R.layout.list, container, false);


        final Context context = getActivity().getApplicationContext();

        loggedIn = getArguments().getBoolean("hasLoggedIn");

        mListViewType = (ListView) v.findViewById(R.id.listViewType);


        final String[] types = new String[]{"Create event", "Manage events", "Manage account", "Notifications", "Data & Sync", "About"};
        final String[] guest_types = new String[]{"Notifications", "Data & Sync", "About"};

        sizeOfTypes = getLoggedIn() ? types.length : guest_types.length;


        settingsAdapter typesAdapter = new settingsAdapter(getContext(), getLoggedIn() ? types : guest_types);


        mListViewType.setAdapter(typesAdapter);
        mListViewType.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String tabName = getLoggedIn() ? types[position] : guest_types[position];
                Fragment newFragment;
                FragmentManager fragmentManager;
                FragmentTransaction fragmentTransaction;

                switch (tabName) {
                    case "Create event":
                        fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        newFragment = new CreateEventFragment();

                        fragmentTransaction.replace(R.id.frame, newFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        break;
                    case "Manage events":
                        fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        newFragment = new ManageEvent();

                        fragmentTransaction.replace(R.id.frame, newFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        break;
                    case "Manage account":
                        fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        newFragment = new ManageAccountFragment();

                        fragmentTransaction.replace(R.id.frame, newFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        break;
                    case "Notifications":
                        fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        newFragment = new NotificationsFragment();

                        fragmentTransaction.replace(R.id.frame, newFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        break;
                    case "Data & Sync":
                        fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        newFragment = new DataFragment();

                        fragmentTransaction.replace(R.id.frame, newFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        break;
                    case "About":
                        fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        newFragment = new AboutFragment();

                        fragmentTransaction.replace(R.id.frame, newFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                }


            }

        });


        return v;
    }

    private boolean getLoggedIn() {
        return loggedIn;
    }

}
