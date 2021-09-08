package com.example.akujobijoshua.StudentBuddy.Fragments;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.akujobijoshua.StudentBuddy.R;
import com.example.akujobijoshua.StudentBuddy.library.SessionManager;
import com.example.akujobijoshua.StudentBuddy.library.User;
import com.example.akujobijoshua.StudentBuddy.library.courses;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;

public class AddCourse extends AppCompatActivity {

    private Firebase mRef= new Firebase("https://student-buddy-app.firebaseio.com/");
    private FirebaseAuth mAuth;
    SessionManager session;
    private EditText courseid, courseName, courseLec, lecPhone, lecEmail;
    private Button btnAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        //mRef = new Firebase("https://student-buddy-app.firebaseio.com/users/");
        mAuth = FirebaseAuth.getInstance();
        session=new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        final String uid = user.get(SessionManager.KEY_ID);
        courseid =(EditText) findViewById(R.id.CourseId);
        courseName =(EditText) findViewById(R.id.CourseName);
        courseLec=(EditText) findViewById(R.id.LecName);
        lecPhone=(EditText) findViewById(R.id.LectPhone);
        lecEmail=(EditText) findViewById(R.id.LecEmail);
        btnAdd=(Button) findViewById(R.id.btnAddCourse);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                saveNewCourse(uid,courseid.getText().toString()
                ,courseName.getText().toString()
                ,courseLec.getText().toString()
                ,lecEmail.getText().toString()
                ,lecPhone.getText().toString());
                Snackbar.make(view, "Course Succesfully entered", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }});

    }

    private void saveNewCourse(String Userid,String id, String name, String Lecturer, String LecturerEmail, String LecturerContact) {
        courses course = new courses(id,name,Lecturer,LecturerEmail,LecturerContact);
        mRef.child("users").child(Userid).child("Course").child(id).child("Course Details").setValue(course);
    }
}
