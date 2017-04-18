package com.example.apple.calendargo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.*;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.*;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.phenotype.Configuration;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabSelectedListener;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by apple on 11/6/16.
 * Define the main page of the app
 */

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    //define the variable and interface element.
    private CoordinatorLayout coordinatorLayout;
    private Fragment f;
    private SearchView mSearchView;
    private TextView mStatusView;
    public static boolean debugEnabled; //control the show for debug message
    public static boolean focusEnabled;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ImageView imageView;
    private MenuItem log_in;
    private MenuItem log_out;

    //define the private file
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    public static boolean hasLoggedIn;

    //define the database related variables
    private FirebaseAuth auth;
    private ProgressBar progressBar;

    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initialize the database auth
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        hasLoggedIn = false;

        //get the password and email address from the preference file
        pref = getSharedPreferences("login.conf", Context.MODE_PRIVATE);
        editor = pref.edit();
        String emailAddress = pref.getString("emailAddress","");
        String password = pref.getString("password","");

        //DEBUG MESSAGE
        //System.out.println("The log in email address is: "+pref.getString("emailAddress",""));
        //System.out.println(pref.getString("password",""));
        //System.out.println("What the heck!!!");

        //Do the automatic log in
        if (!emailAddress.isEmpty() && !password.isEmpty()) {
            hasLoggedIn = true;
            auth.signInWithEmailAndPassword(emailAddress, password)
                    .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.

                            //System.out.println("Inside sending password and email");
                            if (!task.isSuccessful()) {
                                // there was an error
                                Toast.makeText(MainActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                            } else {

                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                String uid = user.getUid();
                                myRef = database.getReference("Users").child(uid);

                                //Toast.makeText(MainActivity.this, "Logged in", Toast.LENGTH_LONG).show();

                                final TextView email = (TextView) findViewById(R.id.email);


                                //System.out.println("Change the has logged in state");
                                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot == null && dataSnapshot.getValue() == null) {
                                            Toast.makeText(MainActivity.this, "No Record", Toast.LENGTH_SHORT).show();
                                        } else {
                                            //System.out.println("LogIn Successfully\n");
                                            List<String> profile = (List<String>) dataSnapshot.getValue();
                                            Toast.makeText(MainActivity.this, "Logged in as " + profile.toString().substring(1, profile.toString().length()-1), Toast.LENGTH_SHORT).show();

                                            email.setText(profile.get(profile.size()-1));
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }

                                });
                            }
                        }
                    });
            }
            else{
            //System.out.println("No log in");
            if (debugEnabled)
                Toast.makeText(MainActivity.this,"User is not logged in",Toast.LENGTH_SHORT).show();
            }




        // disable the title of the toolbar
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // show the list fragment as the main page
        f = new ListFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.frame,f).commit();

        // initialize the bottom bar
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.four_buttons_activity);
        BottomBar bottomBar = BottomBar.attach(this, savedInstanceState);
        bottomBar.setItemsFromMenu(R.menu.four_bottons_menu, new OnMenuTabSelectedListener() {
            @Override
            public void onMenuItemSelected(int itemId) {

                //close the drawer when clicking the bottom bar
                drawerLayout.closeDrawers();

                switch (itemId) {
                    case R.id.list_item:
                        Snackbar.make(coordinatorLayout, "List", Snackbar.LENGTH_SHORT).show();
                        f = new ListFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame,f).commit();
                        break;
                    case R.id.map_item:
                        f = new MapFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame,f).commit();
                        Snackbar.make(coordinatorLayout, "Map", Snackbar.LENGTH_SHORT).show();
                        break;
                    case R.id.calendar_item:
                        f = new CalendarFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame,f).commit();
                        Snackbar.make(coordinatorLayout, "Calendar", Snackbar.LENGTH_SHORT).show();
                        break;
                    case R.id.more_item:
                        Bundle args = new Bundle();
                        args.putBoolean("hasLoggedIn", hasLoggedIn);
                        f = new MoreFragment();
                        f.setArguments(args);
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame,f).commit();
                        Snackbar.make(coordinatorLayout, "Settings", Snackbar.LENGTH_LONG).show();
                        break;
                }
            }
        });


        bottomBar.setActiveTabColor("#C2185B");


        // initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        log_in = navigationView.getMenu().findItem(R.id.log_in);
        log_out = navigationView.getMenu().findItem(R.id.log_out);

        //DEBUG MESSAGE
        //System.out.println(hasLoggedIn);

        // show different layout for the side drawer for different log in mode
       if (hasLoggedIn) {
            System.out.println("Has Logged In");
            log_in.setVisible(false);
            log_out.setVisible(true);
        }
        else{
            System.out.println("Has not Logged In");
            log_in.setVisible(true);
            log_out.setVisible(false);
        }


        // set Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {


                // check if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                // close drawer on item click
                drawerLayout.closeDrawers();

                // check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {


                    // replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.log_in:
                        //Toast.makeText(getApplicationContext(), "Log in", Toast.LENGTH_SHORT).show();
                        Intent logInIntent = new Intent(MainActivity.this, LoginActivity.class);
                        MainActivity.this.startActivity(logInIntent);
                        return true;

                    case R.id.log_out:
                        auth.signOut();
                        editor.remove("emailAddress");
                        editor.remove("password");
                        editor.apply();
                        if (debugEnabled)
                            Toast.makeText(MainActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this,LoginActivity.class));
                        return true;

                    default:
                        if (debugEnabled)
                            Toast.makeText(getApplicationContext(),"Somethings Wrong",Toast.LENGTH_SHORT).show();
                        return true;
                }


            }
        });





        // initialize Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        android.support.v7.app.ActionBarDrawerToggle actionBarDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this,drawerLayout,null,R.string.openDrawer, R.string.closeDrawer){

            @Override
            public void onDrawerClosed(View drawerView) {
                // code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);

            }

            @Override
            public void onDrawerOpened(View drawerView) {

                super.onDrawerOpened(drawerView);

            }
        };

        // set the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        imageView = (ImageView) findViewById(R.id.profile_image);
        //final DrawerLayout mDrawer = (DrawerLayout) findViewById(R.id.drawer);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }


        });


        // calling sync state is necessay or else your hamburger icon wont show up
        //  actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //handle the search bar
    @Override
    public boolean onQueryTextSubmit(String query) {
        mStatusView = (TextView) findViewById(R.id.searchShow);
        f = new SearchFragment();
        Bundle args = new Bundle();
        args.putString("query_string", query);
        f.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame,f).commit();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }


}
