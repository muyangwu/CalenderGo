package com.example.apple.calendargo;

import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by apple on 10/21/16.
 */

/** This class is created to allow the user to be able to reposition the marker on their map after creating an event
 * so as to save the correct location for the event
 */

public class NewEventMapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private EventJson ej;
    String address;
    String name;
    float colorVal;
    String description;
    private Marker marker;
    private Button button;
    private LatLng position;
    String[] event;

    public void setDetails(String address, String name, float colorVal, String description){
        this.address = address;
        this.name = name;
        this.colorVal = colorVal;
        this.description = description;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceStates){
        View v = inflater.inflate(R.layout.new_event_map, null,false);

        button = (Button) v.findViewById(R.id.location_confirm);
        event = getArguments().getStringArray("currEvent");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                position = marker.getPosition();

                double lat = position.latitude;
                double ltd = position.longitude;

                Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());

                try{
                    List<Address> addresses = geocoder.getFromLocation(lat,ltd,1);
                    String address = addresses.get(0).getAddressLine(0);

                    Event current_event = new Event(event[0], event[3], address, event[4], event[1]);

                    current_event.longitude = Double.toString(lat);
                    current_event.latitude = Double.toString(ltd);

                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    EventJson.saveEventToFirebase(current_event);
                    EventJson.saveEventForSpecificUser(auth.getCurrentUser().getUid(),current_event);

                } catch(IOException e){
                    System.out.println(e);
                }

                new AlertDialog.Builder(getActivity())
                        //.setIcon(android.R.drawable.ic_dialog_info)
                        .setTitle("Success!")
                        .setMessage("Event location has been set.")
                        .setPositiveButton("Publish event", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                                // update login status
                                Bundle args = new Bundle();
                                args.putBoolean("hasLoggedIn", MainActivity.hasLoggedIn);

                                Fragment newFragment = new ListFragment();

                                fragmentTransaction.replace(R.id.frame, newFragment);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                            }

                        }).show();
            }
        });


        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);

        CoordinatorLayout coordinatorLayoutLayout = (CoordinatorLayout) v.findViewById(R.id.drag_map_layout);

        // Drag event notification
        Snackbar snack = Snackbar.make(coordinatorLayoutLayout, "To drag your event, select and hold.", Snackbar.LENGTH_INDEFINITE);
        View snackView = snack.getView();
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams)snackView.getLayoutParams();
        params.gravity = Gravity.TOP;
        snackView.setLayoutParams(params);
        snack.show();

        mapFragment.getMapAsync(this);
        return v;
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // adding marker to the app
        marker = mMap.addMarker(new MarkerOptions().position(getLocationFromAddress(getContext(), address)).title(name).icon(BitmapDescriptorFactory.defaultMarker(colorVal)).draggable(true));

        // moves the camera to the location
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(getLocationFromAddress(getContext(), address), 18));

        // enabling dragging functionality to the markers
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                position = marker.getPosition();
            }

            @Override
            public void onMarkerDrag(Marker marker) {
                position = marker.getPosition();
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                position = marker.getPosition();
            }
        });
    }

    private void createMarker( double longitude, double latitude, String name, Float colorVal, String description)
    {
        mMap.addMarker(new MarkerOptions().position(new LatLng(longitude, latitude)).title(name).alpha(0.7f).icon(BitmapDescriptorFactory.defaultMarker(colorVal)).snippet(description));
    }

    /** using Google Documentation to make createMarker events */
    private void createMarkerByAddress(String address, String name, Float colorVal, String description){
        if (getLocationFromAddress(getContext(),address) != null)
            mMap.addMarker(new MarkerOptions().position(getLocationFromAddress(getContext(),address)).title(name).alpha(0.7f).icon(BitmapDescriptorFactory.defaultMarker(colorVal)).snippet(description));
    }


    // using geocoding to extract exact coordinates of the event
    public LatLng getLocationFromAddress(Context context, String strAddress)
    {
        Geocoder coder = new Geocoder(context);
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



}
