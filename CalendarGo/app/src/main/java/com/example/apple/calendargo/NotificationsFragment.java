package com.example.apple.calendargo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

/**
 * Created by jd on 11/11/16.
 */

public class NotificationsFragment extends Fragment implements View.OnClickListener {
    @Nullable

    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceStates) {

        View v = inflater.inflate(R.layout.notifications, container, false);

        Button b7 = (Button) v.findViewById(R.id.button7);
        b7.setOnClickListener(this);

        // toggle debug messages
        Switch s5 = (Switch) v.findViewById(R.id.switch5);

        s5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MainActivity.debugEnabled = isChecked;
            }
        });

        // toggle focus window messages
        Switch s4 = (Switch) v.findViewById(R.id.switch4);
        s4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MainActivity.focusEnabled = isChecked;
            }
        });

        return v;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            // cancel button
            case R.id.button7:
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Bundle args = new Bundle();
                args.putBoolean("hasLoggedIn", MainActivity.hasLoggedIn);
                Fragment newFragment = new MoreFragment();
                newFragment.setArguments(args);

                fragmentTransaction.replace(R.id.frame, newFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            default:
        }
    }
}