package com.example.apple.calendargo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by apple on 11/6/16.
 * show the fragment for the list page (first page in the main page
 */

public class ListFragment extends Fragment {

    private ListView mListViewType;
    private ArrayList<Event> events;
    private int sizeOfTypes;
    private Activity acitivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceStates){
        View v = inflater.inflate(R.layout.list, container,false);

        final Context context = getActivity().getApplicationContext();

        mListViewType = (ListView) v.findViewById(R.id.listViewType);

        final String[] types = new String[] {"Athletics","Free food","Music","Family","Pet friendly","Workshops","Party","Other","Most Popular Events: "};
        //final String[] types;
        //types = getActivity().getResources().getStringArray(R.array.category_array);

        sizeOfTypes = types.length;

        //typesAdapter adapter_type = new typesAdapter(getContext(), types);

        //mListViewType.setAdapter(adapter_type);


       // mListViewPop = (ListView) v.findViewById(R.id.listViewPop);

        events = EventJson.getEventsFromFile("mostPop.json",context);


        //popAdapter adapter_list = new popAdapter(getContext(),events);

        //mListViewPop.setAdapter(adapter_list);

        listAdapter list = new listAdapter(getContext(),events,types);
        mListViewType.setAdapter(list);

        mListViewType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position < sizeOfTypes - 1){
                    String type = types[position];
                    /*Bundle bundle = new Bundle();
                    bundle.putString("type",type);*/

                    Intent type_view_activity = new Intent(getActivity(), VeiwByTypeActivity.class);
                    type_view_activity.putExtra("type",type);

                    startActivity(type_view_activity);

                    /*Fragment f = new EventsFragment();

                    f.setArguments(bundle);

                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame,f).commit();*/

                }
                else if (position >= sizeOfTypes){

                    Event  event_to_be_edited = events.get(position-sizeOfTypes);

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
                    Intent detailIntent = new Intent(context, DetailTypeActivity.class);

                    detailIntent.putExtras(bundle);

                    startActivity(detailIntent);

                }

            }
        });

        return v;
    }


}
