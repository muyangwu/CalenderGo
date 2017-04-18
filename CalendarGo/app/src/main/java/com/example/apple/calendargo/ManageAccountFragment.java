package com.example.apple.calendargo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by jd on 11/11/16.
 */

public class ManageAccountFragment extends Fragment implements View.OnClickListener {


    EditText etPassword1, etPassword2;

    @Nullable

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceStates) {

        View v = inflater.inflate(R.layout.manage_account, container, false);

        Button b5 = (Button) v.findViewById(R.id.button5);
        Button b4 = (Button) v.findViewById(R.id.button4);

        b5.setOnClickListener(this);
        b4.setOnClickListener(this);

        etPassword1 = (EditText) v.findViewById(R.id.password1);
        etPassword2 = (EditText) v.findViewById(R.id.password2);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            // cancel button
            case R.id.button5:
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
            // save changes button
            case R.id.button4:
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String password1 = etPassword1.getText().toString();
                String password2 = etPassword2.getText().toString();

                if (password1.length() < 6) {
                    Toast.makeText(getContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password1.equals(password2)){
                    user.updatePassword(password1).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                new AlertDialog.Builder(getActivity())
                                        //.setIcon(android.R.drawable.ic_dialog_info)
                                        .setTitle("Password Reset")
                                        .setMessage("The password has been reset!")
                                        .setPositiveButton("Ok", null)
                                        .show();
                            }
                            else{
                                Toast.makeText(getActivity(),"Fail to update the password.",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(getActivity(),"Please input the same password",Toast.LENGTH_SHORT).show();
                }

                break;
            default:
        }
    }
}
