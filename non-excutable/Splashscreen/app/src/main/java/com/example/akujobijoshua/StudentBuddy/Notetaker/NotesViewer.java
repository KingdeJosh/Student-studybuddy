package com.example.akujobijoshua.StudentBuddy.Notetaker;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.akujobijoshua.StudentBuddy.R;
import com.example.akujobijoshua.StudentBuddy.library.SessionManager;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.zip.Inflater;

public class NotesViewer extends Fragment {
    private ListView mListNotes;
private FloatingActionButton Fab_NoteAdd;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.activity_notes_viewer, container, false);
        mListNotes = (ListView) v.findViewById(R.id.main_listview);
        Fab_NoteAdd=(FloatingActionButton) v.findViewById(R.id.Notes_add);
        Fab_NoteAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), NoteActivity.class);
                startActivity(intent);
                //finish();
            }
        });
        return v;
    }
    private Firebase myFirebaseRef;
    SessionManager session;


    @Override
    public void onResume() {
        super.onResume();
        myFirebaseRef = new Firebase("https://student-buddy-app.firebaseio.com/users/");
        session=new SessionManager(getContext());
        HashMap<String, String> user = session.getUserDetails();
        String uid = user.get(SessionManager.KEY_ID);
        //load saved notes into the listview
        //first, reset the listview
        mListNotes.setAdapter(null);
        final ArrayList<Note> notes =Utilities.getAllSavedNotes(getContext());

   /*     myFirebaseRef.child(uid).child("Course").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snap:dataSnapshot.getChildren()){
                    Note note=snap.child("Note").child("test").child("content").getValue(String.class);
                    //notes.add(new Note(note.getDateTime(),note.getTitle(),note.getContent()));
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(getContext(), "" + firebaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });*/
        //sort notes from new to old
        Collections.sort(notes, new Comparator<Note>() {
            @Override
            public int compare(Note lhs, Note rhs) {
                if(lhs.getDateTime() > rhs.getDateTime()) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });

        if(notes != null && notes.size() > 0) { //check if we have any notes!
            final NoteAdapter na = new NoteAdapter(getContext(), R.layout.view_note_item, notes);
            mListNotes.setAdapter(na);

            //set click listener for items in the list, by clicking each item the note should be loaded into NoteActivity
            mListNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //run the NoteActivity in view/edit mode
                    String fileName = ((Note) mListNotes.getItemAtPosition(position)).getDateTime()
                            + Utilities.FILE_EXTENSION;
                    Intent viewNoteIntent = new Intent(getContext(), NoteActivity.class);
                    viewNoteIntent.putExtra(Utilities.EXTRAS_NOTE_FILENAME, fileName);
                    startActivity(viewNoteIntent);
                }
            });
        } else { //remind user that we have no notes!
            Toast.makeText(getContext(), "you have no saved notes!\ncreate some new notes :)"
                    , Toast.LENGTH_SHORT).show();
        }
    }

}


