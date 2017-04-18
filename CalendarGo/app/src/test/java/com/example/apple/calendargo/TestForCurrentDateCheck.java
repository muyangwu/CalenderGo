package com.example.apple.calendargo;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class TestForCurrentDateCheck {
    @Test
    public void addition_isCorrect() throws Exception {
        ArrayList<Event> events = new ArrayList<Event>();

        //check for that the whether checkCurrentDate function work
        //First check if the events happen on current date, whether it will return all the events
        for (int i = 0; i < 2; i++){
            Event event = new Event();
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("M-d-yyyy");
            String currentDate = sdf.format(date);
            event.date = currentDate;

            events.add(event);
        }

        EventJson ej = new EventJson();
        events = ej.checkCurrentDate(events);

        for (Event e : events){
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("M-d-yyyy");
            String currentDate = sdf.format(date);
            assert(e.date.equals(currentDate));
        }

        //Second check if the events happen on other date, will it return empty events

        events = new ArrayList<Event>();

        for (int i = 0; i < 2; i++){
            Event event = new Event();
            event.date = "12-31-2015";
            events.add(event);
        }

        for (Event e : events){
            System.out.println(e.date);
        }

        events = ej.checkCurrentDate(events);

        for (Event e : events){
            System.out.println(e.date);
        }

        assert(events.size() == 0);
    }
}
