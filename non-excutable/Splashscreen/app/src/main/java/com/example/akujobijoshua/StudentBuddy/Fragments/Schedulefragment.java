package com.example.akujobijoshua.StudentBuddy.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.akujobijoshua.StudentBuddy.R;
import com.example.akujobijoshua.StudentBuddy.Timetable.Class;
import com.example.akujobijoshua.StudentBuddy.Timetable.Database;
import com.example.akujobijoshua.StudentBuddy.library.DividerItemDecoration;
import com.example.akujobijoshua.StudentBuddy.library.RecyclerTouchListener;
import com.example.akujobijoshua.StudentBuddy.library.classesAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Schedulefragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    public Schedulefragment() {
        // Required empty public constructor
    }

    private List<Class> classList = new ArrayList<>();
    private RecyclerView recyclerView;
    private classesAdapter mAdapter;
    private TextView noofclasses;

    // TODO: Rename and change types and number of parameters

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_schedulefragment, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);

        // Calendar.getInstance();
        //Toast.makeText(getContext(),  Calendar.YEAR+"-"+Calendar.MONTH+"-"+Calendar.DAY_OF_MONTH, Toast.LENGTH_SHORT).show();
        Calendar today = Calendar.getInstance();
        today.get(Calendar.DAY_OF_MONTH);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        // System.out.println(dateFormat.format(cal.getTime()));


        classList = Database.getTodaysClasses(getContext(),dateFormat.format(cal.getTime()),"Exam");
        noofclasses=(TextView) v.findViewById(R.id.textnoclass);
        String no= String.valueOf(classList.size());
        noofclasses.setText(no);
        //System.out.println(classList.size());


        mAdapter = new classesAdapter(classList);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Class movie = classList.get(position);
                Toast.makeText(getContext(), movie.getTopics() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        // Inflate the layout for this fragment
        return v; }


}


