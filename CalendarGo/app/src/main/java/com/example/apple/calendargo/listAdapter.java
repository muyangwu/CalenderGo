package com.example.apple.calendargo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by apple on 11/6/16.
 */

public class listAdapter extends BaseAdapter{

    private ArrayList<Event> events;
    private String[] types;
    private Context mContext;
    private LayoutInflater mInflater;
    private int sizeOfTypes;
    private int sizeOfPops;

    public listAdapter(Context mContext, ArrayList<Event> events, String[] types){
        this.mContext = mContext;
        this.events = events;
        this.types  = types;
        sizeOfPops = events.size();
        sizeOfTypes = types.length;

        mInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return (sizeOfPops+sizeOfTypes);
    }

    @Override
    public Object getItem(int position) {
        if (position < sizeOfTypes) return types[position];
        //System.out.println(position+" and "+sizeOfTypes);
        return events.get(position - sizeOfTypes);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView;

        if (position < sizeOfTypes-1){
            rowView = mInflater.inflate(R.layout.type_list, parent, false);

            TextView type = (TextView) rowView.findViewById(R.id.type_list);
            ImageView icon = (ImageView) rowView.findViewById(R.id.type_icon);

            if (getItem(position) == types[0])
                icon.setImageResource(R.drawable.athletics);
            else if (getItem(position) ==types[1])
                icon.setImageResource(R.drawable.food);
            else if (getItem(position) ==types[2])
                icon.setImageResource(R.drawable.music);
            else if (getItem(position) ==types[3])
                icon.setImageResource(R.drawable.family);
            else if (getItem(position) ==types[4])
                icon.setImageResource(R.drawable.pet);
            else if (getItem(position) ==types[5])
                icon.setImageResource(R.drawable.workshop);
            else if (getItem(position) ==types[6])
                icon.setImageResource(R.drawable.party);
            else
                icon.setImageResource(R.drawable.others);


            type.setText((String) getItem(position));

        }
        else if (position == sizeOfTypes-1){
            rowView = mInflater.inflate(R.layout.most_pop, parent, false);

            TextView type = (TextView) rowView.findViewById(R.id.most_pop);

            type.setText((String) getItem(position));
        }
        else{
            rowView = mInflater.inflate(R.layout.pop_list, parent, false);

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

            Event thisEvent = (Event) getItem(position);

            eventName.setText(thisEvent.getName());
            eventDistance.setText(thisEvent.address);
            eventPersons.setText(thisEvent.date);

            ImageView thumbnailImageView =
                    (ImageView) rowView.findViewById(R.id.event_photo);

            if(thisEvent.type.equals(types[0]))
                Picasso.with(mContext).load(thisEvent.image).placeholder(R.drawable.athletics).into(thumbnailImageView);
            else if (thisEvent.type.equals(types[1]))
                Picasso.with(mContext).load(thisEvent.image).placeholder(R.drawable.food).into(thumbnailImageView);
            else if (thisEvent.type.equals(types[2]))
                Picasso.with(mContext).load(thisEvent.image).placeholder(R.drawable.music).into(thumbnailImageView);
            else if (thisEvent.type.equals(types[3]))
                Picasso.with(mContext).load(thisEvent.image).placeholder(R.drawable.family).into(thumbnailImageView);
            else if (thisEvent.type.equals(types[4]))
                Picasso.with(mContext).load(thisEvent.image).placeholder(R.drawable.pet).into(thumbnailImageView);
            else if (thisEvent.type.equals(types[5]))
                Picasso.with(mContext).load(thisEvent.image).placeholder(R.drawable.workshop).into(thumbnailImageView);
            else if (thisEvent.type.equals(types[6]))
                Picasso.with(mContext).load(thisEvent.image).placeholder(R.drawable.party).into(thumbnailImageView);
            else
                Picasso.with(mContext).load(thisEvent.image).placeholder(R.drawable.others).into(thumbnailImageView);
        }

        return rowView;

    }
}
