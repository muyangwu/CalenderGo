package com.example.apple.calendargo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by apple on 11/6/16.
 * Define the fragment to show events in the fragment.. seems useless.
 */

public class EventsFragment extends Fragment {
    private ListView mListViewPop;
    private String type;
    private popAdapter adapter_list;
    private ImageView backward;
    private ArrayList<Event> events;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceStates){

        View v = inflater.inflate(R.layout.list_events, container,false);

        final Context context = getActivity().getApplicationContext();

        String type = getArguments().getString("type");

        backward = (ImageView) v.findViewById(R.id.backward);


        TextView type_title = (TextView) v.findViewById(R.id.title_type);
        type_title.setText(type);

        events = new ArrayList<>();

        for (int i = 0; i < 10; i++){
            events.add(new Event());
        }


        mListViewPop = (ListView) v.findViewById(R.id.listViewType);

        adapter_list = new popAdapter(getContext(),events);

        mListViewPop.setAdapter(adapter_list);

        backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame,new ListFragment()).commit();
            }
        });

        mListViewPop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*Event currEvent = events.get(position);

                Intent detailIntent = new Intent(context, EventDetailActivity.class);

                detailIntent.putExtra("title", currEvent.getName());
                detailIntent.putExtra("url", currEvent.getUrl());

                // 4
                startActivity(detailIntent);*/
            }
        });
        return v;
    }

}
