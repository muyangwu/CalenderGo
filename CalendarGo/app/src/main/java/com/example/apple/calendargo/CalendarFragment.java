package com.example.apple.calendargo;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by apple on 10/21/16.
 */

public class CalendarFragment extends Fragment implements GestureDetector.OnGestureListener {
    @Nullable

    private ViewFlipper flipper1 = null;
    // private ViewFlipper flipper2 = null;
    private static String TAG = "ZzL";
    private GridView gridView = null;
    private GestureDetector gestureDetector = null;
    private int year_c = 0;
    private int month_c = 0;
    private int day_c = 0;
    private int week_c = 0;
    private int week_num = 0;
    private String currentDate = "";
    private static int jumpWeek = 0;
    private static int jumpMonth = 0;
    private static int jumpYear = 0;
    private DateAdapter dateAdapter;
    private int daysOfMonth = 0;
    private int dayOfWeek = 0;
    private int weeksOfMonth = 0;
    private SpecialCalendar sc = null;
    private boolean isLeapyear = false;
    private int selectPostion = 0;
    private String dayNumbers[] = new String[7];
    private TextView tvDate;
    private int currentYear;
    private int currentMonth;
    private int currentWeek;
    private int currentDay;
    private int currentNum;
    private boolean isStart;
    private int isBefore=0;
    private ListView mListView;
    private EventJson ej;
    private String date;
    private TextView emptyListText;

    private FirebaseDatabase database;
    private DatabaseReference myRef, typeRef;


    public CalendarFragment() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
        currentDate = sdf.format(date);
        year_c = Integer.parseInt(currentDate.split("-")[0]);
        month_c = Integer.parseInt(currentDate.split("-")[1]);
        day_c = Integer.parseInt(currentDate.split("-")[2]);
        currentYear = year_c;
        currentMonth = month_c;
        currentDay = day_c;
        sc = new SpecialCalendar();
        getCalendar(year_c, month_c);
        week_num = getWeeksOfMonth();
        currentNum = week_num;
        if (dayOfWeek == 7) {
            week_c = day_c / 7 + 1;
        } else {
            if (day_c <= (7 - dayOfWeek)) {
                week_c = 1;
            } else {
                if ((day_c - (7 - dayOfWeek)) % 7 == 0) {
                    week_c = (day_c - (7 - dayOfWeek)) / 7 + 1;
                } else {
                    week_c = (day_c - (7 - dayOfWeek)) / 7 + 2;
                }
            }
        }
        currentWeek = week_c;
        getCurrent();

    }


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceStates) {
        View v = inflater.inflate(R.layout.calendar, container, false);
        tvDate = (TextView) v.findViewById(R.id.showDate );
        tvDate.setText( "Events on " + month_c + "/" + day_c + "/" + year_c);
        gestureDetector = new GestureDetector(this);
        flipper1 = (ViewFlipper) v.findViewById(R.id.flipper1);
        dateAdapter = new DateAdapter(getActivity(), getResources(), currentYear,
                currentMonth, currentWeek, currentNum, selectPostion,
                currentWeek == 1 ? true : false);
        addGridView();
        dayNumbers = dateAdapter.getDayNumbers();
        gridView.setAdapter(dateAdapter);
        selectPostion = dateAdapter.getTodayPosition();
        gridView.setSelection(selectPostion);
        flipper1.addView(gridView, 0);
        mListView = (ListView) v.findViewById(R.id.calendar_list);
        emptyListText = (TextView)v.findViewById(R.id.empty_list);


        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Events");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ej = new EventJson();

                ArrayList<Event> events = ej.getAllEvents(dataSnapshot,getActivity());
                events = ej.checkCurrentDate(events);
                final popAdapter events_by_date = new popAdapter(getContext(),events);

                if (events.isEmpty())
                    emptyListText.setVisibility(View.VISIBLE);
                else
                    emptyListText.setVisibility(View.INVISIBLE);

                mListView.setAdapter(events_by_date);

                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        Event event_to_be_edited = (Event) events_by_date.getItem(i);
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

                        Intent detail = new Intent(getActivity(), DetailTypeActivity.class);
                        detail.putExtras(bundle);

                        startActivity(detail);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return v;
    }

    private void addGridView() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                AbsListView.LayoutParams.FILL_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
        gridView = new GridView(getActivity());
        gridView.setNumColumns(7);
        gridView.setGravity(Gravity.CENTER_VERTICAL);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gridView.setVerticalSpacing(1);
        gridView.setHorizontalSpacing(1);
        gridView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return CalendarFragment.this.gestureDetector.onTouchEvent(event);
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Log.i(TAG, "day:" + dayNumbers[position]);
                selectPostion = position;
                dateAdapter.setSeclection(position);
                dateAdapter.notifyDataSetChanged();
                date = dateAdapter.getCurrentMonth(selectPostion)+"-"+dayNumbers[position] + "-"+dateAdapter.getCurrentYear(selectPostion);
                System.out.println("date"+date);
                ej = new EventJson();



                database = FirebaseDatabase.getInstance();
                myRef = database.getReference("Events");

                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        ArrayList<Event> events = ej.getAllEvents(dataSnapshot,getActivity());
                        events = ej.checkDate(date,events);
                        final popAdapter events_by_date = new popAdapter(getContext(),events);

                        if (events.isEmpty())
                            emptyListText.setVisibility(View.VISIBLE);
                        else
                            emptyListText.setVisibility(View.INVISIBLE);

                        mListView.setAdapter(events_by_date);

                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                Event event_to_be_edited = (Event) events_by_date.getItem(i);
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

                                Intent detail = new Intent(getActivity(), DetailTypeActivity.class);
                                detail.putExtras(bundle);

                                startActivity(detail);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                tvDate.setText("Events on " + dateAdapter.getCurrentMonth(selectPostion) + "/"
                        + dayNumbers[position] + "/"+dateAdapter.getCurrentYear(selectPostion));
            }
        });
        gridView.setLayoutParams(params);
    }

    @Override
    public void onPause() {
        super.onPause();
        jumpWeek = 0;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    /**
     *
     */
    public void getCurrent() {
        if (currentWeek > currentNum) {
            if (currentMonth + 1 <= 12) {
                currentMonth++;
            } else {
                currentMonth = 1;
                currentYear++;
            }
            currentWeek = 1;
            currentNum = getWeeksOfMonth(currentYear, currentMonth);
        } else if (currentWeek == currentNum) {
            if (getLastDayOfWeek(currentYear, currentMonth) == 6) {
            } else {
                if (currentMonth + 1 <= 12) {
                    currentMonth++;
                } else {
                    currentMonth = 1;
                    currentYear++;
                }
                currentWeek = 1;
                currentNum = getWeeksOfMonth(currentYear, currentMonth);
            }

        } else if (currentWeek < 1) {
            if (currentMonth - 1 >= 1) {
                currentMonth--;
            } else {
                currentMonth = 12;
                currentYear--;
            }
            currentNum = getWeeksOfMonth(currentYear, currentMonth);
            currentWeek = currentNum - 1;
        }
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        int gvFlag = 0;
        if (e1.getX() - e2.getX() > 80) {
            //
            isBefore++;
            addGridView();
            currentWeek++;
            getCurrent();
            dateAdapter = new DateAdapter(this.getActivity(), getResources(), currentYear,
                    currentMonth, currentWeek, currentNum, selectPostion,
                    currentWeek == 1 ? true : false);
            dayNumbers = dateAdapter.getDayNumbers();
            gridView.setAdapter(dateAdapter);
            tvDate.setText("Events on " + dateAdapter.getCurrentMonth(selectPostion) + "/"
                    + dayNumbers[selectPostion] + "/"+dateAdapter.getCurrentYear(selectPostion));
            gvFlag++;
            flipper1.addView(gridView, gvFlag);
            dateAdapter.setSeclection(selectPostion);
            this.flipper1.setInAnimation(AnimationUtils.loadAnimation(this.getActivity(),
                    R.anim.push_left_in));
            this.flipper1.setOutAnimation(AnimationUtils.loadAnimation(this.getActivity(),
                    R.anim.push_left_out));
            this.flipper1.showNext();
            flipper1.removeViewAt(0);
            return true;

        } else if (e1.getX() - e2.getX() < -80) {
            addGridView();

            if(isBefore>0) {
                isBefore--;
                currentWeek--;
                getCurrent();
                System.out.println("CalendarFragmenet: "+currentMonth+" , "+currentDay);

                dateAdapter = new DateAdapter(this.getActivity(), getResources(), currentYear,
                        currentMonth, currentWeek, currentNum, selectPostion,
                        currentWeek == 1 ? true : false);
                dayNumbers = dateAdapter.getDayNumbers();
                gridView.setAdapter(dateAdapter);
                tvDate.setText("Events on " + dateAdapter.getCurrentMonth(selectPostion) + "/"
                        + dayNumbers[selectPostion] + "/"+dateAdapter.getCurrentYear(selectPostion));
                gvFlag++;
                flipper1.addView(gridView, gvFlag);
                dateAdapter.setSeclection(selectPostion);
                this.flipper1.setInAnimation(AnimationUtils.loadAnimation(this.getActivity(),
                        R.anim.push_right_in));
                this.flipper1.setOutAnimation(AnimationUtils.loadAnimation(this.getActivity(),
                        R.anim.push_right_out));
                this.flipper1.showPrevious();
                flipper1.removeViewAt(0);
                return true;
                // }
            }else{
                Log.i("TAG","no before============");
            }
        }
        return false;
    }

    public boolean onTouchEvent(MotionEvent event) {
        return this.gestureDetector.onTouchEvent(event);
    }

    /**
     *
     *
     * @param year
     * @param month
     */
    public int getWeeksOfMonth(int year, int month) {
        //
        int preMonthRelax = 0;
        int dayFirst = getWhichDayOfWeek(year, month);
        int days = sc.getDaysOfMonth(sc.isLeapYear(year), month);
        if (dayFirst != 7) {
            preMonthRelax = dayFirst;
        }
        if ((days + preMonthRelax) % 7 == 0) {
            weeksOfMonth = (days + preMonthRelax) / 7;
        } else {
            weeksOfMonth = (days + preMonthRelax) / 7 + 1;
        }
        return weeksOfMonth;

    }

    /**
     *
     *
     * @param year
     * @param month
     * @return
     */
    public int getWhichDayOfWeek(int year, int month) {
        return sc.getWeekdayOfMonth(year, month);

    }

    /**
     * @param year
     * @param month
     */
    public int getLastDayOfWeek(int year, int month) {
        return sc.getWeekDayOfLastMonth(year, month,
                sc.getDaysOfMonth(isLeapyear, month));
    }

    public void getCalendar(int year, int month) {
        isLeapyear = sc.isLeapYear(year);
        daysOfMonth = sc.getDaysOfMonth(isLeapyear, month);
        dayOfWeek = sc.getWeekdayOfMonth(year, month);
    }

    public int getWeeksOfMonth() {
        // getCalendar(year, month);
        int preMonthRelax = 0;
        if (dayOfWeek != 7) {
            preMonthRelax = dayOfWeek;
        }
        if ((daysOfMonth + preMonthRelax) % 7 == 0) {
            weeksOfMonth = (daysOfMonth + preMonthRelax) / 7;
        } else {
            weeksOfMonth = (daysOfMonth + preMonthRelax) / 7 + 1;
        }
        return weeksOfMonth;
    }


}
