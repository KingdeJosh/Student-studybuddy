package com.example.akujobijoshua.StudentBuddy.library;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.example.akujobijoshua.StudentBuddy.R;
import com.example.akujobijoshua.StudentBuddy.Timetable.Class;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class classesAdapter extends RecyclerView.Adapter<classesAdapter.MyViewHolder> {

    private List<Class> classesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, year, genre;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.textNo);
            genre = (TextView) view.findViewById(R.id.textDate);
            year = (TextView) view.findViewById(R.id.textTime);
        }
    }


    public classesAdapter(List<Class> classesList) {
        this.classesList = classesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_licenses, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Class c = classesList.get(position);

        holder.title.setText(c.getTopics());
        holder.genre.setText(c.getRemarks());
        holder.year.setText(c.getClassTime());
    }

//convert string date to date formart

    @Override
    public int getItemCount() {
        return classesList.size();
    }
}

