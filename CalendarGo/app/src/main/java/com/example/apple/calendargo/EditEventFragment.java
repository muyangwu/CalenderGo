package com.example.apple.calendargo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by jd on 11/11/16.
 */

public class EditEventFragment extends Fragment implements View.OnClickListener {
    EditText organizer,eventName,month,day,year,description;
    String[] current_event;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceStates) {

        View v = inflater.inflate(R.layout.edit_event, container, false);

        Button b10 = (Button) v.findViewById(R.id.button10);
        Button b13 = (Button) v.findViewById(R.id.button13);

        organizer = (EditText) v.findViewById(R.id.organizer);
        eventName = (EditText) v.findViewById(R.id.eventName);
        month = (EditText) v.findViewById(R.id.month);
        day = (EditText) v.findViewById(R.id.day);
        year = (EditText) v.findViewById(R.id.year);
        description = (EditText) v.findViewById(R.id.description);

        current_event = getArguments().getStringArray("currEvent");

        String[] date = current_event[2].split("-");

        organizer.setText(current_event[0]);
        eventName.setText(current_event[1]);
        month.setText(date[0]);
        day.setText(date[1]);
        year.setText(date[2]);
        description.setText(current_event[3]);

        final TextView deleteEvent = (TextView)v.findViewById(R.id.deleteEvent);
        deleteEvent.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        //.setIcon(android.R.drawable.ic_dialog_info)
                        .setTitle("Delete confirmation")
                        .setMessage("Are you sure you want to delete this event?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                removeFromFirebase(current_event[1],current_event[7]);

                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                                // update login status
                                Bundle args = new Bundle();
                                args.putBoolean("hasLoggedIn", MainActivity.hasLoggedIn);

                                Fragment newFragment = new ManageEvent();


                                fragmentTransaction.replace(R.id.frame, newFragment);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();

                            }

                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {}
                        }).show();
            }
        });

        b10.setOnClickListener(this);
        b13.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle args = new Bundle();
        args.putBoolean("hasLoggedIn", MainActivity.hasLoggedIn);
        Fragment newFragment = new ManageEvent();
        newFragment.setArguments(args);

        switch(v.getId()) {
            // cancel button
            case R.id.button10:
                fragmentTransaction.replace(R.id.frame, newFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            // save button
            case R.id.button13:

                if (month.getText().toString().isEmpty() || day.getText().toString().isEmpty() || year.getText().toString().isEmpty() || organizer.getText().toString().isEmpty() || eventName.getText().toString().isEmpty()){
                    new AlertDialog.Builder(getActivity())
                            //.setIcon(android.R.drawable.ic_dialog_info)
                            .setTitle("Empty event details")
                            .setMessage("Please fill out all of the fields before changing an event.")
                            .setPositiveButton("Ok", null)
                            .show();
                    break;
                }
                String date = Integer.parseInt(month.getText().toString())+"-"+Integer.parseInt(day.getText().toString())+"-"+Integer.parseInt(year.getText().toString());
                Event editedEvent = new Event(organizer.getText().toString(),current_event[7],current_event[4],date,eventName.getText().toString());
                editedEvent.description = description.getEditableText().toString();
                editedEvent.longitude = current_event[6];
                editedEvent.latitude = current_event[5];

                removeFromFirebase(current_event[1],current_event[7]);

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String uid = user.getUid();

                EventJson.saveEventToFirebase(editedEvent);
                EventJson.saveEventForSpecificUser(uid,editedEvent);

                fragmentTransaction.replace(R.id.frame, newFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            default:
        }
    }

    public void removeFromFirebase(String eventName,String type){
        FirebaseDatabase database;
        DatabaseReference myRef;

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Events");
        myRef = myRef.child(type).child(eventName);
        myRef.removeValue();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        myRef = database.getReference("UserEvents");
        myRef = myRef.child(uid);
        myRef.child(eventName).removeValue();
    }
}
