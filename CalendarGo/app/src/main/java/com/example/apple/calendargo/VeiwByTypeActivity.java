package com.example.apple.calendargo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VeiwByTypeActivity extends AppCompatActivity {

    private ListView mListView;
    private String type;
    private ArrayList<Event> eventList;
    private EventJson ej;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setTheme(R.style.splashScreenTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veiw_by_type);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String title = this.getIntent().getExtras().getString("type");

        setTitle(title);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mListView = (ListView) findViewById(R.id.type_view_list);
        ej = new EventJson();

        FirebaseDatabase database;
        DatabaseReference myRef;

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Events").child(title);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                eventList = ej.getEventsFromDataSnapShot(dataSnapshot);

                System.out.println("ViewByType: "+eventList);

                final popAdapter adapter_list = new popAdapter(getBaseContext(), eventList);

                mListView.setAdapter(adapter_list);

                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        Event event_to_be_edited = (Event) adapter_list.getItem(i);
                        String[] event_to_edited_string = new String[8];
                        event_to_edited_string[0] = event_to_be_edited.organizer;
                        event_to_edited_string[1] = event_to_be_edited.name;
                        event_to_edited_string[2] = event_to_be_edited.date;
                        event_to_edited_string[3] = event_to_be_edited.description;
                        event_to_edited_string[4] = event_to_be_edited.address;
                        event_to_edited_string[5] = event_to_be_edited.latitude;
                        event_to_edited_string[6] = event_to_be_edited.longitude;
                        event_to_edited_string[7] = event_to_be_edited.type;

                        Bundle bundle = new Bundle();
                        bundle.putStringArray("currEvent",event_to_edited_string);

                        Intent detail = new Intent(VeiwByTypeActivity.this,DetailTypeActivity.class);

                        detail.putExtras(bundle);

                        startActivity(detail);

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
