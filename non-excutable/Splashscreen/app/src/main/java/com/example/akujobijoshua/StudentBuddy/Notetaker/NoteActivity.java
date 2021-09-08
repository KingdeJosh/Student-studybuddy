package com.example.akujobijoshua.StudentBuddy.Notetaker;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.akujobijoshua.StudentBuddy.R;
import com.example.akujobijoshua.StudentBuddy.library.SessionManager;
import com.example.akujobijoshua.StudentBuddy.library.courses;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class NoteActivity extends AppCompatActivity {

    private boolean mIsViewingOrUpdating; //state of the activity
    private long mNoteCreationTime;
    private String mFileName;
    private Note mLoadedNote = null;
    private Firebase myFirebaseRef;
    SessionManager session;
    private EditText mEtTitle;
    private EditText mEtContent;
    private Spinner mCourseDropdown;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> CourseCodes;
    private Firebase mRef= new Firebase("https://student-buddy-app.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        myFirebaseRef = new Firebase("https://student-buddy-app.firebaseio.com/users/");
        session=new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        String uid = user.get(SessionManager.KEY_ID);
        CourseCodes= new ArrayList<String>();
        mEtTitle = (EditText) findViewById(R.id.note_et_title);
        mEtContent = (EditText) findViewById(R.id.note_et_content);
        mCourseDropdown=(Spinner) findViewById(R.id.course_Dropdown);




        //check if view/edit note bundle is set, otherwise user wants to create new note
        mFileName = getIntent().getStringExtra(Utilities.EXTRAS_NOTE_FILENAME);
        if(mFileName != null && !mFileName.isEmpty() && mFileName.endsWith(Utilities.FILE_EXTENSION)) {
            mLoadedNote = Utilities.getNoteByFileName(getApplicationContext(), mFileName);
            if (mLoadedNote != null) {
                //update the widgets from the loaded note
                mEtTitle.setText(mLoadedNote.getTitle());
                mEtTitle.setEnabled(false);
                mEtContent.setText(mLoadedNote.getContent());
                mNoteCreationTime = mLoadedNote.getDateTime();
                mIsViewingOrUpdating = true;
                myFirebaseRef.child(uid).child("Course").addValueEventListener(new ValueEventListener() {
                    //onDataChange is called every time the name of the User changes in your Firebase Database
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Inside onDataChange we can get the data as an Object from the dataSnapshot
                        //getValue returns an Object. We can specify the type by passing the type expected as a parameter
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            //test.add(snapshot.getKey().toString());

                            CourseCodes.add( snapshot.getKey());

                        }
                        //dataSnapshot.getChildren().getValue().toString();
                        arrayAdapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_dropdown_item_1line,CourseCodes);
                        mCourseDropdown.setAdapter(arrayAdapter);

                    }
                    //onCancelled is called in case of any error
                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        Toast.makeText(getApplicationContext(), "" + firebaseError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        } else { //user wants to create a new note

            mNoteCreationTime = System.currentTimeMillis();
            mIsViewingOrUpdating = true;
            myFirebaseRef.child(uid).child("Course").addValueEventListener(new ValueEventListener() {
                //onDataChange is called every time the name of the User changes in your Firebase Database
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //Inside onDataChange we can get the data as an Object from the dataSnapshot
                    //getValue returns an Object. We can specify the type by passing the type expected as a parameter
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        //test.add(snapshot.getKey().toString());

                        CourseCodes.add( snapshot.getKey());

                    }
                    //dataSnapshot.getChildren().getValue().toString();
                    arrayAdapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_dropdown_item_1line,CourseCodes);
                    mCourseDropdown.setAdapter(arrayAdapter);

                }
                //onCancelled is called in case of any error
                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    Toast.makeText(getApplicationContext(), "" + firebaseError.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //load menu based on the state we are in (new, view/update/delete)
        if(mIsViewingOrUpdating) { //user is viewing or updating a note
            getMenuInflater().inflate(R.menu.menu_note_view, menu);
        } else { //user wants to create a new note
            getMenuInflater().inflate(R.menu.menu_note_add, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_save_note: //save the note
            case R.id.action_update: //or update :P
                validateAndSaveNote();
                break;

            case R.id.action_delete:
                actionDelete();
                break;

            case R.id.action_cancel: //cancel the note
                actionCancel();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Back button press is same as cancel action...so should be handled in the same manner!
     */
    @Override
    public void onBackPressed() {
        actionCancel();
    }

    /**
     * Handle delete action
     */
    private void actionDelete() {
        //ask user if he really wants to delete the note!
        AlertDialog.Builder dialogDelete = new AlertDialog.Builder(this)
                .setTitle("delete note")
                .setMessage("really delete the note?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(mLoadedNote != null && Utilities.deleteFile(getApplicationContext(), mFileName)) {
                            Toast.makeText(NoteActivity.this, mLoadedNote.getTitle() + " is deleted"
                                    , Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(NoteActivity.this, "can not delete the note '" + mLoadedNote.getTitle() + "'"
                                    , Toast.LENGTH_SHORT).show();
                        }
                        finish();
                    }
                })
                .setNegativeButton("NO", null); //do nothing on clicking NO button :P

        dialogDelete.show();
    }

    /**
     * Handle cancel action
     */
    private void actionCancel() {

        if(!checkNoteAltred()) { //if note is not altered by user (user only viewed the note/or did not write anything)
            finish(); //just exit the activity and go back to MainActivity
        } else { //we want to remind user to decide about saving the changes or not, by showing a dialog
            AlertDialog.Builder dialogCancel = new AlertDialog.Builder(this)
                    .setTitle("discard changes...")
                    .setMessage("are you sure you do not want to save changes to this note?")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish(); //just go back to main activity
                        }
                    })
                    .setNegativeButton("NO", null); //null = stay in the activity!
            dialogCancel.show();
        }
    }

    /**
     * Check to see if a loaded note/new note has been changed by user or not
     * @return true if note is changed, otherwise false
     */
    private boolean checkNoteAltred() {
        if(mIsViewingOrUpdating) { //if in view/update mode
            return mLoadedNote != null && (!mEtTitle.getText().toString().equalsIgnoreCase(mLoadedNote.getTitle())
                    || !mEtContent.getText().toString().equalsIgnoreCase(mLoadedNote.getContent()));
        } else { //if in new note mode
            return !mEtTitle.getText().toString().isEmpty() || !mEtContent.getText().toString().isEmpty();
        }
    }

    /**
     * Validate the title and content and save the note and finally exit the activity and go back to MainActivity
     */
    private void validateAndSaveNote() {

        //get the content of widgets to make a note object
        String title = mEtTitle.getText().toString();
        String content = mEtContent.getText().toString();
        String courseid=null;
        if(mCourseDropdown.getSelectedItem() != null){courseid=mCourseDropdown.getSelectedItem().toString();}
        else if(mCourseDropdown.getSelectedItem() == null){courseid="Empty ID";}
        session=new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        String uid = user.get(SessionManager.KEY_ID);

        //see if user has entered anything :D lol
        if(title.isEmpty()) { //title
            Toast.makeText(NoteActivity.this, "please enter a title!"
                    , Toast.LENGTH_SHORT).show();
            return;
        }

        if(content.isEmpty()) { //content
            Toast.makeText(NoteActivity.this, "please enter a content for your note!"
                    , Toast.LENGTH_SHORT).show();
            return;
        }
        if(courseid.isEmpty()) { //content
            Toast.makeText(NoteActivity.this, "please select a course for your note!"
                    , Toast.LENGTH_SHORT).show();
            return;
        }
        //set the creation time, if new note, now, otherwise the loaded note's creation time
        if(mLoadedNote != null) {
            mNoteCreationTime = mLoadedNote.getDateTime();
        } else {
            mNoteCreationTime = System.currentTimeMillis();
        }

        //finally save the note!
        if(Utilities.saveNote(this, new Note(mNoteCreationTime, title, content))) { //success!
            //tell user the note was saved!
            if (!courseid.equalsIgnoreCase("Empty ID")){
            saveNote(uid,courseid,title,content,mNoteCreationTime);}
            Toast.makeText(this, "note has been saved", Toast.LENGTH_SHORT).show();
        } else { //failed to save the note! but this should not really happen :P :D :|
            Toast.makeText(this, "can not save the note. make sure you have enough space " +
                    "on your device", Toast.LENGTH_SHORT).show();
        }

        finish(); //exit the activity, should return us to MainActivity
    }
    // sync data to online datebase
    private void saveNote(String Userid,String CourseID, String Title, String Note, long Creation_Date) {
        Note note=new Note(Creation_Date,Title, Note,CourseID);
        mRef.child("users").child(Userid).child("Course").child(CourseID).child("Note").child(Title).setValue(note);
        Toast.makeText(this, "note has been saved online", Toast.LENGTH_SHORT).show();
    }
}
