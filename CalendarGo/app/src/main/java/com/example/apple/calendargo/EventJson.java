package com.example.apple.calendargo;

/**
 * Created by apple on 11/11/16.
 * Define the method to extract event object from the Firabase
 */

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.awareness.snapshot.internal.Snapshot;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EventJson {

    private String name;
    private String description;
    private String time;
    private double longitude;
    private double latitude;
    private int persons;
    private String url;
    private String image;
    private ArrayList<Event> events;


    // get events from file
    public static ArrayList<Event> getEventsFromFile(String filename, Context context){
        final ArrayList<Event> eventList = new ArrayList<Event>();

        try{
            String jsonString = loadJsonFromAsset(filename,context);
            JSONObject json = new JSONObject(jsonString);
            JSONArray events = json.getJSONArray("mostPop");

            for (int i = 0; i < events.length(); i++){
                Event event = new Event();
                JSONObject curr = events.getJSONObject(i);
                event.name =curr.getString("name");
                event.description = curr.getString("description");
                event.date = curr.getString("date");
                event.image = curr.getString("image");
                event.persons = curr.getInt("persons");
                event.longitude = curr.getString("longitude");
                event.latitude = curr.getString("latitude");
                event.type = curr.getString("type");
                event.address = curr.getString("address");

                eventList.add(event);
            }



        } catch (JSONException e){
            e.printStackTrace();
        }

        return eventList;
    }

    //get events from databasae for certain date
    public static ArrayList<Event> getEventsFromFileDate(String filename, String date, Context context){
        ArrayList<Event> temp_events;
        temp_events = getEventsFromFile(filename,context);
        ArrayList<Event> events = new ArrayList<Event>();
        for (Event e : temp_events){
            System.out.println("date is: "+date);
            System.out.println("event date is: "+e.date);
            if (date.equals(e.date)){
                System.out.println("EventJson - find events in the date");
                events.add(e);
            }
        }

        return events;
    }

    //load json file from the asset folder
    private static String loadJsonFromAsset(String filename, Context context){
        String json = null;

        try {
            InputStream is = context.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            System.out.println(json);
        }
        catch (java.io.IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return json;
    }

    //save certain event to firebase
    public static void saveEventToFirebase(Event event){

        FirebaseDatabase database;
        DatabaseReference myRef;

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Events");

        Map<String,Event> map = new HashMap<String,Event>();

        map.put(event.name,event);

        myRef.child(event.type).child(event.name).setValue(map);

    }

    //save certain event for its creator
    public static void saveEventForSpecificUser(String UserId, Event event){
        FirebaseDatabase database;
        DatabaseReference myRef;

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("UserEvents");

        Map<String,Event> map = new HashMap<String,Event>();

        map.put(event.name,event);

        myRef.child(UserId).child(event.name).setValue(map);


    }


    //get events from firebase for specific type
    public static ArrayList<Event> getEventFromFirebaseByType(final String type){

        final ArrayList<Event> events = new ArrayList<Event>();

        FirebaseDatabase database;
        DatabaseReference myRef, typeRef;

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Events");

        typeRef = myRef.child(type);

        typeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot == null && dataSnapshot.getValue() == null) {
                    System.out.println("No records");
                } else {

                    //System.out.println("LogIn Successfully\n");
                    Map<String,Map<String,Map<String,Object>>> type_events = (Map<String,Map<String,Map<String,Object>>>) dataSnapshot.getValue();

                    if (type_events == null) return;

                    Collection<Map<String,Map<String,Object>>> string_events = type_events.values();

                    if (string_events == null) return;

                    for (Map<String,Map<String,Object>> map : string_events){
                        for (Map<String,Object> event : map.values()){
                            Event new_event = new Event();

                            //System.out.println(event.get("longitude"));

                            new_event.address = (String)event.get("address");
                            new_event.date = (String)event.get("date");
                            new_event.longitude = (String) event.get("longitude");
                            new_event.latitude = (String) event.get("latitude");
                            new_event.name = (String)event.get("name");
                            new_event.image = (String) event.get("image");
                            new_event.organizer = (String) event.get("organizer");
                            new_event.description = (String) event.get("description");

                            System.out.println(new_event.toString());

                            events.add(new_event);
                        }
                    }
                    /*for(Map<String,Event> map:profile){
                        Set<Map.Entry<String,Event>> set = map.entrySet();
                        for (Map.Entry<String,Event> e:set){
                            events.add(e.getValue());
                        }
                    }*/

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        System.out.println("EventJson - getEventsByType: "+events);


        return events;
    }

    //get events
    public ArrayList<Event>  getEventsSpecificDate(String date, Activity activity){
        ArrayList<Event> events = new ArrayList<Event>();
        ArrayList<Event> temp;
        String[] types;
        types = activity.getResources().getStringArray(R.array.category_array);

        for (String type : types){
            System.out.println("EventJson - type: "+type);
            temp = getEventFromFirebaseByType(type);

            for (Event e : temp){
                System.out.println("date is: "+date);
                System.out.println("event date is: "+e.date);
                if (date.equals(e.date)){
                    System.out.println("EventJson - find events in the date");
                    events.add(e);
                }
            }
        }

        return events;
    }
    public static Event getEventFromFirebaseByName(String name){
        Event event = new Event();
        return event;
    }

    //get events from specific data snapshot
    public ArrayList<Event> getEventsFromDataSnapShot(DataSnapshot dataSnapshot){
        events = new ArrayList<Event>();

        if (dataSnapshot == null && dataSnapshot.getValue() == null) {
            System.out.println("No records");
        } else {

            //System.out.println("LogIn Successfully\n");
            Map<String,Map<String,Map<String,Object>>> type_events = (Map<String,Map<String,Map<String,Object>>>) dataSnapshot.getValue();

            if (type_events == null) return events;

            Collection<Map<String,Map<String,Object>>> string_events = type_events.values();

            if (string_events == null) return events;

            for (Map<String,Map<String,Object>> map : string_events){
                for (Map<String,Object> event : map.values()){
                    Event new_event = new Event();

                    //System.out.println(event.get("longitude"));

                    new_event.address = (String)event.get("address");
                    new_event.date = (String)event.get("date");
                    new_event.longitude = (String)event.get("longitude");
                    new_event.latitude = (String) event.get("latitude");
                    new_event.name = (String)event.get("name");
                    new_event.image = (String) event.get("image");
                    new_event.type = (String) event.get("type");
                    new_event.organizer = (String) event.get("organizer");
                    new_event.description = (String) event.get("description");

                    System.out.println(new_event.toString());

                    events.add(new_event);
                }
            }
                    /*for(Map<String,Event> map:profile){
                        Set<Map.Entry<String,Event>> set = map.entrySet();
                        for (Map.Entry<String,Event> e:set){
                            events.add(e.getValue());
                        }
                    }*/

        }

        return events;
    }

    //get all events from the firebase
    public ArrayList<Event> getAllEvents(DataSnapshot dataSnapshot,Activity activity){
        events = new ArrayList<Event>();

        if (dataSnapshot == null || dataSnapshot.getValue() == null) {
            System.out.println("No records");
        }else{

            for (Map<String,Map<String,Map<String,Object>>> type_events : (((Map<String,Map<String,Map<String,Map<String,Object>>>>)dataSnapshot.getValue()).values())){

                if (type_events == null) return events;

                Collection<Map<String,Map<String,Object>>> string_events = type_events.values();

                if (string_events == null) return events;

                for (Map<String,Map<String,Object>> map : string_events){
                    for (Map<String,Object> event : map.values()){
                        Event new_event = new Event();

                        //System.out.println("longitude: "+event.get("longitude"));

                        new_event.address = (String)event.get("address");
                        new_event.date = (String)event.get("date");
                        new_event.longitude = (String) event.get("longitude");
                        new_event.latitude = (String) event.get("latitude");
                        new_event.name = (String)event.get("name");
                        new_event.image = (String) event.get("image");
                        new_event.type = (String) event.get("type");
                        new_event.organizer = (String) event.get("organizer");
                        new_event.description = (String) event.get("description");
                        //new_event.longitude = (double) event.get("longitude");
                        //new_event.latitude = (double) event.get("latitude");

                        //System.out.println(new_event.toString());

                        events.add(new_event);
                    }
                }
            }
        }

        return events;

    }

    //filter a list of events by date
    public ArrayList<Event> checkDate(String date, ArrayList<Event> events){
        ArrayList<Event> new_events = new ArrayList<Event>();

        for (Event e : events){
            if (date.equals(e.date)){
                new_events.add(e);
            }
        }

        return new_events;

    }

    //filter a list of events by current date
    public ArrayList<Event> checkCurrentDate(ArrayList<Event> events){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("M-d-yyyy");
        String currentDate = sdf.format(date);

        return checkDate(currentDate, events);
    }

    //get events created by specific user
    public ArrayList<Event> getEventForSpecificUser(DataSnapshot dataSnapshot){
        ArrayList<Event> events = new ArrayList<Event>();

        if (dataSnapshot != null && dataSnapshot.getValue()!= null) {
            Map<String,Map<String,Map<String,Object>>> name_name_events = (Map<String,Map<String,Map<String,Object>>>) dataSnapshot.getValue();
            //System.out.println("EventJson name_events: "+name_name_events);

            Collection<Map<String,Map<String,Object>>> name_events = name_name_events.values();
            for (Map<String,Map<String,Object>> map : name_events){
                for (Map<String,Object> event: map.values()) {
                    Event new_event = new Event();

                    //System.out.println("longitude: "+event.get("longitude"));

                    new_event.address = (String) event.get("address");
                    new_event.date = (String) event.get("date");
                    new_event.longitude = (String) event.get("longitude");
                    new_event.latitude = (String) event.get("latitude");
                    new_event.name = (String) event.get("name");
                    new_event.image = (String) event.get("image");
                    new_event.type = (String) event.get("type");
                    new_event.organizer = (String) event.get("organizer");
                    new_event.description = (String) event.get("description");

                    //new_event.longitude = (double) event.get("longitude");
                    //new_event.latitude = (double) event.get("latitude");

                    //System.out.println(new_event.toString());

                    events.add(new_event);
                }
            }
        }

        return events;
    }
}
