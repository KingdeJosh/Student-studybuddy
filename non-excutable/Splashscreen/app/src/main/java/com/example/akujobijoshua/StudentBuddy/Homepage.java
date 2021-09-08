package com.example.akujobijoshua.StudentBuddy;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import android.Manifest;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;




import com.example.akujobijoshua.StudentBuddy.Flashcard.DecksList;
import com.example.akujobijoshua.StudentBuddy.Fragments.DashboardFragment;
import com.example.akujobijoshua.StudentBuddy.Fragments.RecorderFragment;
import com.example.akujobijoshua.StudentBuddy.Fragments.courseFragment;
import com.example.akujobijoshua.StudentBuddy.GPA.GPA;
import com.example.akujobijoshua.StudentBuddy.Notetaker.NotesViewer;
import com.example.akujobijoshua.StudentBuddy.TaskCalendar.BaseActivity;
//import com.example.akujobijoshua.StudentBuddy.Timetable.ListBatchesActivity;
import com.example.akujobijoshua.StudentBuddy.Timetable.ListBatchesActivity;
import com.example.akujobijoshua.StudentBuddy.library.SessionManager;
import com.example.akujobijoshua.StudentBuddy.todolis.*;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;


public class Homepage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView Profilename, Profileemail;
    SessionManager session;
    TabHost tabHost;
   public Toolbar toolbar;
    private Firebase myFirebaseRef;
    private FirebaseAuth auth;

    static final Integer WRITE_EXST = 0x3;
    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;
    private static final int RECORD_AUDIO = 0x4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        myFirebaseRef = new Firebase("https://student-buddy-app.firebaseio.com/users/");
        auth = FirebaseAuth.getInstance();
        DashboardFragment homefragment = new DashboardFragment();
        FragmentManager manger = getSupportFragmentManager();
        manger.beginTransaction().replace(R.id.homepage_content,
                homefragment,
                homefragment.getTag()).commit();
        getSupportActionBar().setTitle("Dashboard");

        session = new SessionManager(getApplicationContext());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        Profilename = (TextView) header.findViewById(R.id.ProfileName);
        Profileemail = (TextView) header.findViewById(R.id.ProfileEmail);
        HashMap<String, String> user = session.getUserDetails();
        String uid = user.get(SessionManager.KEY_ID);

        //Referring to the name of the User who has logged in currently and adding a valueChangeListener
        myFirebaseRef.child(uid).child("name").addValueEventListener(new ValueEventListener() {
            //onDataChange is called every time the name of the User changes in your Firebase Database
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Inside onDataChange we can get the data as an Object from the dataSnapshot
                //getValue returns an Object. We can specify the type by passing the type expected as a parameter
                String data = dataSnapshot.getValue(String.class);
                Profilename.setText("Hello " + data + ", ");
            }

            //onCancelled is called in case of any error
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(getApplicationContext(), "" + firebaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        //Referring to the name of the User who has logged in currently and adding a valueChangeListener
        myFirebaseRef.child(uid).child("email").addValueEventListener(new ValueEventListener() {
            //onDataChange is called every time the name of the User changes in your Firebase Database
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Inside onDataChange we can get the data as an Object from the dataSnapshot
                //getValue returns an Object. We can specify the type by passing the type expected as a parameter
                String data = dataSnapshot.getValue(String.class);
                Profileemail.setText("Hello " + data + ", ");
            }

            //onCancelled is called in case of any error
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(getApplicationContext(), "" + firebaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,WRITE_EXST);
        //askForPermission(Manifest.permission.RECORD_AUDIO,RECORD_AUDIO);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }

    private void askForPermission(String permission, Integer requestCode) {

        if (ContextCompat.checkSelfPermission(Homepage.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(Homepage.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(Homepage.this, new String[]{permission}, requestCode);


            } else {

                ActivityCompat.requestPermissions(Homepage.this, new String[]{permission}, requestCode);
            }
        } else {
            //Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }}

    private void proceedAfterPermission() {
        //We've got the permission, now we can proceed further
        Toast.makeText(getBaseContext(), "We got the Storage Permission", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        session=new SessionManager(getApplicationContext());
        if (id == R.id.nav_Dashboard) {
            DashboardFragment homefragment= new DashboardFragment();
            FragmentManager manger = getSupportFragmentManager();
            manger.beginTransaction().replace(R.id.homepage_content,
                    homefragment,
                    homefragment.getTag()).commit();
            toolbar.setTitle("Dashboard");

        }  else if (id == R.id.nav_todolist) {
            Intent myIntent = new Intent(getApplicationContext(), MainAct.class);
            startActivity(myIntent);
        }
        else if (id == R.id.nav_Timetable) {
            ListBatchesActivity timetab= new ListBatchesActivity();
            FragmentManager manger = getSupportFragmentManager();
            manger.beginTransaction().replace(R.id.homepage_content,
                    timetab,
                    timetab.getTag()).commit();

            toolbar.setTitle("Timetable");

        }
        else if (id == R.id.nav_Calender) {
            BaseActivity basicActivity= new BaseActivity();
            FragmentManager manger = getSupportFragmentManager();
            manger.beginTransaction().replace(R.id.homepage_content,
                    basicActivity,
                    basicActivity.getTag()).commit();

            toolbar.setTitle("Calender");

        }
        else if (id == R.id.nav_Chat) {
            courseFragment courseFragment= new courseFragment();
            FragmentManager manger = getSupportFragmentManager();
            manger.beginTransaction().replace(R.id.homepage_content,
                    courseFragment,
                    courseFragment.getTag()).commit();

            toolbar.setTitle("courses");


        } else if (id == R.id.nav_Rec) {
            Intent myIntent = new Intent(getApplicationContext(), RecorderFragment.class);
            startActivity(myIntent);

        } else if (id == R.id.nav_Notes) {
            NotesViewer notesViewer= new NotesViewer();
            FragmentManager manger = getSupportFragmentManager();
            manger.beginTransaction().replace(R.id.homepage_content,
                    notesViewer,
                    notesViewer.getTag()).commit();
            toolbar.setTitle("Notes");


        } else if (id == R.id.nav_flashcard) {

            DecksList decksList= new DecksList();
            FragmentManager manger = getSupportFragmentManager();
            manger.beginTransaction().replace(R.id.homepage_content,
                    decksList,
                    decksList.getTag()).commit();
            toolbar.setTitle("Flashcards");

        }else if (id == R.id.nav_GPA) {
            GPA decksList= new GPA();
            FragmentManager manger = getSupportFragmentManager();
            manger.beginTransaction().replace(R.id.homepage_content,
                    decksList,
                    decksList.getTag()).commit();
            toolbar.setTitle("GPA");


        } else if (id == R.id.nav_send) {
        session.logoutUser();
                auth.signOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
