package com.example.apple.calendargo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {
    TextView profile_username;
    TextView profile_email;
    TextView profile_phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        profile_email = (TextView) findViewById(R.id.profile_email);
        profile_phone = (TextView) findViewById(R.id.profile_phone);
        Intent thisIntent = getIntent();
        profile_email.setText(thisIntent.getExtras().getString("email"));
        profile_phone.setText(thisIntent.getExtras().getString("phone"));
    }

}
