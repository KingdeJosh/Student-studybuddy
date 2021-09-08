package com.example.akujobijoshua.StudentBuddy;

import android.app.ProgressDialog;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.akujobijoshua.StudentBuddy.library.SessionManager;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends Activity {

    Button btnLogin;
    Button Btnregister;
    Button passreset;
    EditText inputEmail;
    EditText inputPassword;
    private TextView loginErrorMsg;
    private ProgressDialog mProgressDialog;
    private static final String TAG = "AndroidBash";
    String sessionEmail, sessionID;
    // Session Manager Class
    SessionManager session;
    /**
     * Called when the activity is first created.
     */
    private static String KEY_SUCCESS = "success";
    private static String KEY_UID = "uid";
    private static String KEY_USERNAME = "uname";
    private static String KEY_FIRSTNAME = "fname";
    private static String KEY_LASTNAME = "lname";
    private static String KEY_EMAIL = "email";
    private static String KEY_CREATED_AT = "created_at";
    private FirebaseAuth auth;
    private ProgressBar progressBar1;
    private Firebase myFirebaseRef;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(com.example.akujobijoshua.StudentBuddy.R.layout.activity_login);
        myFirebaseRef = new Firebase("https://student-buddy-app.firebaseio.com/users/");
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        inputEmail = (EditText) findViewById(com.example.akujobijoshua.StudentBuddy.R.id.email);
        inputPassword = (EditText) findViewById(com.example.akujobijoshua.StudentBuddy.R.id.pword);
        Btnregister = (Button) findViewById(com.example.akujobijoshua.StudentBuddy.R.id.registerbtn);
        btnLogin = (Button) findViewById(com.example.akujobijoshua.StudentBuddy.R.id.login);
        progressBar1 = (ProgressBar) findViewById(R.id.progressBar);


        Btnregister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), Register.class);
                startActivityForResult(myIntent, 0);
                finish();
            }});

/**
 * Login button click event
 * A Toast is set to alert when the Email and Password field is empty
 **/
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }
               signIn(email,password);

            }
        });
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }
        session = new SessionManager(getApplicationContext());
        showProgressDialog();

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            String uid =auth.getCurrentUser().getUid();

// //Referring to the name of the User who has logged in currently and adding a valueChangeListener
                            myFirebaseRef.child(uid).child("name").addValueEventListener(new ValueEventListener() {
                                //onDataChange is called every time the name of the User changes in your Firebase Database
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    //Inside onDataChange we can get the data as an Object from the dataSnapshot
                                    //getValue returns an Object. We can specify the type by passing the type expected as a parameter
                                    String data = dataSnapshot.getValue(String.class);
                                    sessionID="Hello " + data;
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
                                    sessionEmail="Hello " + data;
                                }

                                //onCancelled is called in case of any error
                                @Override
                                public void onCancelled(FirebaseError firebaseError) {
                                    Toast.makeText(getApplicationContext(), "" + firebaseError.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                            session.createLoginSession(auth.getCurrentUser().getUid(),sessionID,sessionEmail);
                            Toast.makeText(getApplicationContext(), sessionID + session.isLoggedIn(), Toast.LENGTH_LONG).show();


                            Intent intent = new Intent(getApplicationContext(), Homepage.class);
                            String Uid = auth.getCurrentUser().getUid();
                            intent.putExtra("user_id", Uid);
                            startActivity(intent);
                            finish();
                        }

                        hideProgressDialog();
                    }
                });
        //
    }
    private boolean validateForm() {
        boolean valid = true;

        String userEmail = inputEmail.getText().toString();
        if (TextUtils.isEmpty(userEmail)) {
            inputEmail.setError("Required.");
            valid = false;
        } else {
            inputEmail.setError(null);
        }

        String userPassword = inputPassword.getText().toString();
        if (TextUtils.isEmpty(userPassword)) {
            inputPassword.setError("Required.");
            valid = false;
        } else {
            inputPassword.setError(null);
        }

        return valid;
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setTitle("Checking Network");
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
        startActivity(intent);
        finish();
        System.exit(0);
    }
}