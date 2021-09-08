package com.example.akujobijoshua.StudentBuddy.Notetaker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.akujobijoshua.StudentBuddy.R;

import java.util.List;


public class NoteAdapter extends ArrayAdapter<Note> {

    public NoteAdapter(Context context, int resource, List<Note> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.view_note_item, null);
        }

        Note note = getItem(position);

        if(note != null) {
            TextView title = (TextView) convertView.findViewById(R.id.list_note_title);
            TextView date = (TextView) convertView.findViewById(R.id.list_note_date);
            TextView content = (TextView) convertView.findViewById(R.id.list_note_content_preview);

            title.setText(note.getTitle());
            date.setText(note.getDateTimeFormatted(getContext()));

            //if content is big...we take only the first 50 characters!
            if(note.getContent().length() > 20) {
                content.setText(note.getContent().substring(0,20) + "...");
            } else { //if less than 50 chars...leave it as is :P
                content.setText(note.getContent());
            }
        }

        return convertView;
    }

}
