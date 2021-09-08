package com.example.akujobijoshua.StudentBuddy.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.akujobijoshua.StudentBuddy.R;
import com.example.akujobijoshua.StudentBuddy.library.SessionManager;
import com.example.akujobijoshua.StudentBuddy.library.courses;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class courseFragment extends Fragment {


    public courseFragment() {
        // Required empty public constructor
    }
    private ArrayAdapter<String> adapter;
   // private Firebase mRef= new Firebase("https://student-buddy-app.firebaseio.com/");
    private Firebase myFirebaseRef;
    SessionManager session;
    ListView list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_course, container, false);

        list = (ListView) v.findViewById(R.id.lists);
        myFirebaseRef = new Firebase("https://student-buddy-app.firebaseio.com/users/");
        session=new SessionManager(getContext());
        HashMap<String, String> user = session.getUserDetails();
        String uid = user.get(SessionManager.KEY_ID);
        final ArrayList<String> test=new ArrayList<String>();
        final ArrayList<String> test1=new ArrayList<String>();
        final List<String> coursesNames=new ArrayList<String>();
        final List<String> coursesCodes=new ArrayList<String>();
        //DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("Course");
        myFirebaseRef.child(uid).child("Course").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snap:dataSnapshot.getChildren()){
                    coursesCodes.add(snap.getKey());
                    coursesNames.add(snap.child("Course Details").child("courseName").getValue(String.class).toString());
                }
                adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, coursesNames);

                // Here, you set the data in your ListView
                list.setAdapter(adapter);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(getContext(), "" + firebaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        // Inflate the layout for this fragment
        //Referring to the name of the User who has logged in currently and adding a valueChangeListener
       /* myFirebaseRef.child(uid).child("Course").addValueEventListener(new ValueEventListener() {
            //onDataChange is called every time the name of the User changes in your Firebase Database
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Inside onDataChange we can get the data as an Object from the dataSnapshot
                //getValue returns an Object. We can specify the type by passing the type expected as a parameter
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    //test.add(snapshot.getKey().toString());
                    courses ok=snapshot.getValue(courses.class);
                test.add(ok.getCourseName());

                }
                //dataSnapshot.getChildren().getValue().toString();
                adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, test);

                // Here, you set the data in your ListView
                list.setAdapter(adapter);
            }

            //onCancelled is called in case of any error
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(getContext(), "" + firebaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

*/


        return v;
    }

}
