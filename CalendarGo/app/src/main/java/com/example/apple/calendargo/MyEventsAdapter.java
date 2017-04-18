package com.example.apple.calendargo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by apple on 11/6/16.
 */

public class MyEventsAdapter extends BaseAdapter {

    private Context thisContext;
    private ArrayList<Event> events;
    private LayoutInflater mInflater;

    public MyEventsAdapter(Context context, ArrayList<Event> events){
        thisContext = context;
        this.events = events;
        mInflater = (LayoutInflater) thisContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public Object getItem(int position) {
        return events.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = mInflater.inflate(R.layout.my_event_list, parent, false);

        String[] types = rowView.getResources().getStringArray(R.array.category_array);

        // Get title element
        TextView eventName =
                (TextView) rowView.findViewById(R.id.event_name);

// Get subtitle element
        TextView eventPersons =
                (TextView) rowView.findViewById(R.id.event_persons);

// Get detail element
        TextView eventDistance =
                (TextView) rowView.findViewById(R.id.event_distance);

// Get thumbnail element
        /*ImageView eventPhoto =
                (ImageView) rowView.findViewById(R.id.event_photo);*/

        //Button b12 = (Button)rowView.findViewById(R.id.button12);
        //b12.setClickable(false);
        //b12.setFocusable(false);

        Event thisEvent = (Event) getItem(position);

        eventName.setText(thisEvent.getName());
        //eventDistance.setText(thisEvent.address);
        eventPersons.setText(thisEvent.date);

        ImageView thumbnailImageView =
                (ImageView) rowView.findViewById(R.id.event_photo);

        if(thisEvent.type.equals(types[0]))
            Picasso.with(thisContext).load(thisEvent.image).placeholder(R.drawable.athletics).into(thumbnailImageView);
        else if (thisEvent.type.equals(types[1]))
            Picasso.with(thisContext).load(thisEvent.image).placeholder(R.drawable.food).into(thumbnailImageView);
        else if (thisEvent.type.equals(types[2]))
            Picasso.with(thisContext).load(thisEvent.image).placeholder(R.drawable.music).into(thumbnailImageView);
        else if (thisEvent.type.equals(types[3]))
            Picasso.with(thisContext).load(thisEvent.image).placeholder(R.drawable.family).into(thumbnailImageView);
        else if (thisEvent.type.equals(types[4]))
            Picasso.with(thisContext).load(thisEvent.image).placeholder(R.drawable.pet).into(thumbnailImageView);
        else if (thisEvent.type.equals(types[5]))
            Picasso.with(thisContext).load(thisEvent.image).placeholder(R.drawable.workshop).into(thumbnailImageView);
        else if (thisEvent.type.equals(types[6]))
            Picasso.with(thisContext).load(thisEvent.image).placeholder(R.drawable.party).into(thumbnailImageView);
        else
            Picasso.with(thisContext).load(thisEvent.image).placeholder(R.drawable.others).into(thumbnailImageView);
        return rowView;
    }
}
