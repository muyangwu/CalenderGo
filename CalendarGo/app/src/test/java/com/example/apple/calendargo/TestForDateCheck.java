package com.example.apple.calendargo;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by apple on 11/30/16.
 */

public class TestForDateCheck {
    @Test
    public void addition_isCorrect() throws Exception {
        ArrayList<Event> events = new ArrayList<Event>();

        String date = "11-30-2015";
        String notDate = "11-29-2015";

        //check for that the whether checkCurrentDate function work
        //First check if the events happen on current date, whether it will return all the events
        for (int i = 0; i < 2; i++){
            Event event = new Event();
            event.date = date;

            events.add(event);
        }

        EventJson ej = new EventJson();
        events = ej.checkDate(date,events);

        for (Event e : events){
            assert(e.date.equals(date));
        }

        //Second check if the events happen on other date, will it return empty events

        events = new ArrayList<Event>();

        for (int i = 0; i < 2; i++){
            Event event = new Event();
            event.date = date;
            events.add(event);
        }

        for (Event e : events){
            System.out.println(e.date);
        }

        events = ej.checkCurrentDate(events);

        for (Event e : events){
            System.out.println(e.date);
        }

        for (Event e : events){
            assert(!e.date.equals(notDate));
        }
    }

}
