package com.example.apple.calendargo;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ManageEvent extends Fragment{

    ListView mListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_manage_event, container, false);

        mListView = (ListView) v.findViewById(R.id.my_event_list_view);

        FirebaseDatabase database;
        DatabaseReference myRef;

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("UserEvents");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        myRef = myRef.child(uid);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                EventJson ej = new EventJson();

                ArrayList<Event> events = ej.getEventForSpecificUser(dataSnapshot);

                final MyEventsAdapter manage_event_adapter = new MyEventsAdapter(getContext(),events);

                mListView.setAdapter(manage_event_adapter);

                mListView.setOnItemClickListener(new ListView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> a, View v, int i, long l) {
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                        /* get event from the adapter */
                        Event event_to_be_edited = (Event) manage_event_adapter.getItem(i);
                        String[] event_to_edited_string = new String[8];
                        event_to_edited_string[0] = event_to_be_edited.organizer;
                        event_to_edited_string[1] = event_to_be_edited.name;
                        event_to_edited_string[2] = event_to_be_edited.date;
                        event_to_edited_string[3] = event_to_be_edited.description;
                        event_to_edited_string[4] = event_to_be_edited.address;
                        event_to_edited_string[5] = event_to_be_edited.latitude;
                        event_to_edited_string[6] = event_to_be_edited.longitude;
                        event_to_edited_string[7] = event_to_be_edited.type;

                        Bundle args = new Bundle();
                        args.putBoolean("hasLoggedIn", MainActivity.hasLoggedIn);
                        args.putStringArray("currEvent",event_to_edited_string);

                        Fragment newFragment = new EditEventFragment();
                        newFragment.setArguments(args);

                        fragmentTransaction.replace(R.id.frame, newFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }

                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        // Inflate the layout for this fragment
        return v;
    }

}
