package com.example.akujobijoshua.StudentBuddy.Timetable;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.akujobijoshua.StudentBuddy.R;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Akujobi Joshua on 6/3/2017.
 */

public class classAdapter extends RecyclerView.Adapter<classAdapter.MyViewHolder> {

    private List<Class> classes;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textTopic, textvenue, textDay;
        public TextView textStartDate, textLecturer, texttype,textperiod;
        public ImageView type_color;
        public MyViewHolder(View view) {
            super(view);
            textTopic = (TextView) view.findViewById(R.id.textTopic);
            textvenue = (TextView) view.findViewById(R.id.textvenue);
            textDay = (TextView) view.findViewById(R.id.textDay);
            type_color = (ImageView) view.findViewById(R.id.type_color);

            textStartDate  = (TextView) view.findViewById(R.id.textStartDate);
            textLecturer = (TextView) view.findViewById(R.id.textLecturer);
            texttype = (TextView) view.findViewById(R.id.texttype);
            textperiod = (TextView) view.findViewById(R.id.textperiod);
        }
    }
    public classAdapter(List<Class> classes) {
        this.classes = classes;
    }
    public classAdapter(Context current){
        this.context = current;
    }
    @Override
    public classAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.classrow, parent, false);

        return new classAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Class c = classes.get(position);
        holder.textTopic.setText(c.getTopics());
        holder.textvenue.setText(c.getRemarks());
        holder.textStartDate.setText(c.getClassDate());
        holder.textLecturer.setText(c.getLecturer());
        holder.textperiod.setText(c.getPeriod()+" min");
        holder.texttype.setText(c.getTypes());
if(c.getTypes()!=null){
            if(c.getTypes().equalsIgnoreCase("Exam")){
                holder.type_color.setBackgroundColor(Color.rgb(255,76,76));
            }
            else if(c.getTypes().equalsIgnoreCase("Class")){
                holder.type_color.setBackgroundColor(Color.rgb(255,166,76));
            }
            else if(c.getTypes().equalsIgnoreCase("Reading")){
                holder.type_color.setBackgroundColor(Color.rgb(76,165,255));
            }}



    }

    @Override
    public int getItemCount() {
        return classes.size();
    }

}


