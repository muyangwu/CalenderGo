package com.example.apple.calendargo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created to implement the map functionality of the app
 */

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private EventJson ej;
    public HashMap<Marker, Event> hmap = new HashMap<Marker, Event>();
    String[] types;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceStates){
        View v = inflater.inflate(R.layout.map, null,false);
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);


        types = v.getResources().getStringArray(R.array.category_array);

        mapFragment.getMapAsync(this);
        return v;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if(hmap.get(marker) == null)
                    return;

                Event event = hmap.get(marker);
                Intent detailEvent = new Intent(getActivity(),DetailTypeActivity.class);

                //creating a string array to story different properties associated with the event
                String[] event_to_edited_string = new String[8];
                event_to_edited_string[0] = event.organizer;
                event_to_edited_string[1] = event.name;
                event_to_edited_string[2] = event.date;
                event_to_edited_string[3] = event.description;
                event_to_edited_string[4] = event.address;
                event_to_edited_string[5] = event.latitude;
                event_to_edited_string[6] = event.longitude;
                event_to_edited_string[7] = event.type;

                Bundle bundle = new Bundle();
                bundle.putStringArray("currEvent",event_to_edited_string);

                detailEvent.putExtras(bundle);
                startActivity(detailEvent);
            }
        });

        FirebaseDatabase database;
        DatabaseReference myRef;

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Events");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Event> markersArray;

                ej = new EventJson();

                markersArray = ej.checkCurrentDate(ej.getAllEvents(dataSnapshot,getActivity()));

                //this is used to put all the markers on the map
                for(Event e : markersArray)
                {
                    createMarker(Double.parseDouble(e.longitude),Double.parseDouble((e.latitude)),e.name,e.type,e.description,e);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //this block of code adds a marker and moves the camera over UCSD
        LatLng ucsd = new LatLng(32.8801, -117.2340);
        mMap.addMarker(new MarkerOptions().position(ucsd).title("UCSD"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ucsd, 18));


    }

    /** Method to create the marker using various properties */
    private void createMarker(double longitude, double latitude, String name, String type, String description, Event e)
    {
        Drawable circleDrawable;
        if (type.equals(types[0]))
            circleDrawable = getResources().getDrawable(R.drawable.athletics);
        else if (type.equals(types[1]))
            circleDrawable = getResources().getDrawable(R.drawable.food);
        else if (type.equals(types[2]))
            circleDrawable = getResources().getDrawable(R.drawable.music);
        else if (type.equals(types[3]))
            circleDrawable = getResources().getDrawable(R.drawable.family);
        else if (type.equals(types[4]))
            circleDrawable = getResources().getDrawable(R.drawable.pet);
        else if (type.equals(types[5]))
            circleDrawable = getResources().getDrawable(R.drawable.workshop);
        else if (type.equals(types[6]))
            circleDrawable = getResources().getDrawable(R.drawable.party);
        else
            circleDrawable = getResources().getDrawable(R.drawable.others);

        BitmapDescriptor markerIcon = getMarkerIconFromDrawable(circleDrawable);
        Marker m = mMap.addMarker(new MarkerOptions().position(new LatLng(longitude, latitude)).title(name).snippet(description).icon(markerIcon));
        hmap.put(m, e);
    }

    /** this is a code segment that was inspired from the Google documentation */
    private void createMarkerByAddress(String address, String name, Float colorVal, String description){
        if (getLocationFromAddress(getContext(),address) != null)
            mMap.addMarker(new MarkerOptions().position(getLocationFromAddress(getContext(),address)).title(name).alpha(0.7f).icon(BitmapDescriptorFactory.defaultMarker(colorVal)).snippet(description));
    }


    /** this block of code is required to the latitudes and longitudes of a location based on the entered address */
    public LatLng getLocationFromAddress(Context context, String strAddress)
    {
        System.out.println("Get Location Function: Address is: "+strAddress);
        Geocoder coder= new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try
        {
            address = coder.getFromLocationName(strAddress, 5);


            if(address==null)
            {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return p1;

    }

    /** This function replaces the traditional marker given by Android with a different marker style */
    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        double a = 0.35;
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap((int)(drawable.getIntrinsicWidth()*a), (int)(drawable.getIntrinsicHeight()*a), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, (int)(drawable.getIntrinsicWidth()*a), (int)(drawable.getIntrinsicWidth()*a));
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
