package com.example.apple.calendargo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class DetailPopActivity extends AppCompatActivity {

    TextView organizer, eventName, month, day, year, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pop);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        organizer = (TextView) findViewById(R.id.organizer);
        eventName = (TextView) findViewById(R.id.eventName);
        month = (TextView) findViewById(R.id.month);
        day = (TextView) findViewById(R.id.day);
        year = (TextView) findViewById(R.id.year);
        description = (TextView) findViewById(R.id.description);

        String[] currEvent = getIntent().getExtras().getStringArray("currEvent");
        String[] date = currEvent[2].split("-");

        organizer.setText(currEvent[0]);
        eventName.setText(currEvent[1]);
        month.setText(date[0]);
        day.setText(date[1]);
        year.setText(date[2]);

        description.setText(currEvent[3]);

        setTitle(currEvent[1]);
    }

}
