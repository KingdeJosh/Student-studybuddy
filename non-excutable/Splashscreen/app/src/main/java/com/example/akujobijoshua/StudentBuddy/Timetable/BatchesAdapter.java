package com.example.akujobijoshua.StudentBuddy.Timetable;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.akujobijoshua.StudentBuddy.R;

import java.util.ArrayList;
import java.util.List;

public class BatchesAdapter extends RecyclerView.Adapter<BatchesAdapter.MyViewHolder> {


	private List<Batch> batches;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textCode, textCourse, textStartDate;
        public Button btnClasses, btnUpdate, btnAddClass;
        public MyViewHolder(View view) {
            super(view);
            textCode = (TextView) view.findViewById(R.id.textCode);
            textCourse = (TextView) view.findViewById(R.id.textCourse);
            textStartDate = (TextView) view.findViewById(R.id.textStartDate);

            btnClasses  = (Button) view.findViewById(R.id.btndelete);
            btnUpdate = (Button) view.findViewById(R.id.btnUpdate);
            btnAddClass = (Button) view.findViewById(R.id.btnAddClass);
        }
    }
    public BatchesAdapter(List<Batch> batches) {
        this.batches = batches;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.batch, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Batch batch = batches.get(position);
        holder.textCode.setText(batch.getCode());
        holder.textCourse.setText(batch.getCourse());
        holder.textStartDate.setText(batch.getStartdate());
        holder.btnAddClass.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, AddClassActivity.class);
                intent.putExtra("batchcode",batch.getCode());
                context.startActivity(intent);
            }
        });
        holder.btnUpdate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                Context context = view.getContext();
                Intent intent = new Intent(context, UpdateBatchActivity.class);
                intent.putExtra("batchcode",batch.getCode());
                context.startActivity(intent);
            }
        });
        holder.btnClasses.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, ListClassesActivity.class);
                intent.putExtra("batchcode",batch.getCode());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return batches.size();
    }

}


