package com.example.akujobijoshua.StudentBuddy.Notetaker;

import android.content.Context;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public class Note implements Serializable {

    private long mDateTime; //creation time of the note
    private String mTitle; //title of the note
    private String mContent; //content of the note
    private String course;//course id of the note

    public Note(long dateInMillis, String title, String content) {
        mDateTime = dateInMillis;
        mTitle = title;
        mContent = content;
    }
    public Note(long dateInMillis, String title, String content, String Course) {
        mDateTime = dateInMillis;
        mTitle = title;
        mContent = content;
        course = Course;
    }
    public void setcourse(String Course) {
        course = Course;
    }
    public void setDateTime(long dateTime) {
        mDateTime = dateTime;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public long getDateTime() {
        return mDateTime;
    }

    /**
     * Get date time as a formatted string
     * @param context The context is used to convert the string to user set locale
     * @return String containing the date and time of the creation of the note
     */
    public String getDateTimeFormatted(Context context) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"
                , context.getResources().getConfiguration().locale);
        formatter.setTimeZone(TimeZone.getDefault());
        return formatter.format(new Date(mDateTime));
    }

    public String getTitle() {
        return mTitle;
    }
    public String getCourse() {
        return course;
    }

    public String getContent() {
        return mContent;
    }
}
