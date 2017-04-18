package com.example.apple.calendargo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class DetailTypeActivity extends AppCompatActivity {
    TextView organizer, eventName, month, day, year, description, address;
    ImageView icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_type);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        organizer = (TextView) findViewById(R.id.organizer);
        eventName = (TextView) findViewById(R.id.eventName);
        month = (TextView) findViewById(R.id.month);
        day = (TextView) findViewById(R.id.day);
        year = (TextView) findViewById(R.id.year);
        description = (TextView) findViewById(R.id.description);
        address = (TextView) findViewById(R.id.address);
        icon = (ImageView) findViewById(R.id.imageView2);

        String[] currEvent = getIntent().getExtras().getStringArray("currEvent");
        String[] date = currEvent[2].split("-");

        if(date[1].length() == 1)
            date[1] = "0" + date[1];

        if(date[0].length() == 1)
            date[0] = "0" + date[0];

        organizer.setText(currEvent[0]);
        eventName.setText(currEvent[1]);
        month.setText(date[0]);
        day.setText(date[1]);
        year.setText(date[2]);
        address.setText(currEvent[4]);
        description.setText(currEvent[3]);

        String[] types = getResources().getStringArray(R.array.category_array);
        String currType = currEvent[7];

        if (currType.equals(types[0]))
            icon.setImageResource(R.drawable.athletics);
        else if (currType.equals(types[1]))
            icon.setImageResource(R.drawable.food);
        else if (currType.equals(types[2]))
            icon.setImageResource(R.drawable.music);
        else if (currType.equals(types[3]))
            icon.setImageResource(R.drawable.family);
        else if (currType.equals(types[4]))
            icon.setImageResource(R.drawable.pet);
        else if (currType.equals(types[5]))
            icon.setImageResource(R.drawable.workshop);
        else if (currType.equals(types[6]))
            icon.setImageResource(R.drawable.party);
        else
            icon.setImageResource(R.drawable.others);

        setTitle(currEvent[1]);
    }

}
