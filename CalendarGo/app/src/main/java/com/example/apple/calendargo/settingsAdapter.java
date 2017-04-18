package com.example.apple.calendargo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class settingsAdapter extends BaseAdapter {



    private Context thisContext;

    private String[] types;

    private LayoutInflater mInflater;



    public settingsAdapter(Context tContext, String[] types){

        thisContext = tContext;

        this.types = types;

        mInflater = (LayoutInflater) thisContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override

    public int getCount() {

        return types.length;

    }



    @Override

    public Object getItem(int position) {

        return types[position];

    }



    @Override

    public long getItemId(int position) {

        return position;

    }



    @Override

    public View getView(int position, View convertView, ViewGroup parent) {

        // Get view for row item

        View rowView = mInflater.inflate(R.layout.settings_list, parent, false);



        TextView type = (TextView) rowView.findViewById(R.id.type_list);



        type.setText(types[position]);



        return rowView;

    }





}