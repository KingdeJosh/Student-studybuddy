package com.example.akujobijoshua.StudentBuddy.Timetable;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.akujobijoshua.StudentBuddy.R;
import com.example.akujobijoshua.StudentBuddy.library.DividerItemDecoration;
import com.example.akujobijoshua.StudentBuddy.library.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;


public class ListClassesActivity extends AppCompatActivity {

	private List<Class> classList = new ArrayList<>();
	private RecyclerView recyclerView;
	private classAdapter mAdapter;
	String batchcode;
	//TableLayout tableClasses;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listclasses);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		// get batchcode using intent
		batchcode = getIntent().getStringExtra("batchcode");
        FloatingActionButton button=(FloatingActionButton) findViewById(R.id.butAddclass) ;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), AddClassActivity.class);
                intent.putExtra("batchcode",batchcode);
                startActivity(intent);
            }
        });

	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

	@Override
	public void onStart() {
		super.onStart();
        mAdapter = new classAdapter(getApplicationContext());
		recyclerView = (RecyclerView) findViewById(R.id.batchRecycler_view);
		classList = Database.getClasses(this, batchcode);
		if(!classList.isEmpty()){
			mAdapter = new classAdapter(classList);

			recyclerView.setHasFixedSize(true);
			RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
			recyclerView.setLayoutManager(mLayoutManager);
			recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
			recyclerView.setItemAnimator(new DefaultItemAnimator());
			recyclerView.setAdapter(mAdapter);

			recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
				@Override
				public void onClick(View view, int position) {
				Class C = classList.get(position);
				Intent intent = new Intent( ListClassesActivity.this,UpdateClassActivity.class);
					intent.putExtra("classid", C.getClassId());
					startActivity(intent);
				}

				@Override
				public void onLongClick(View view, int position) {
					Class c = classList.get(position);
					Toast.makeText(getApplicationContext(), c.getClassDate() + " is selected!", Toast.LENGTH_SHORT).show();
					createDeleteAllCardsDialog(c);
				}
			}));

		} else if(classList.isEmpty()){Toast.makeText(getApplicationContext(), " Please, add timetable activity ", Toast.LENGTH_SHORT).show();}

	}

	private AlertDialog createDeleteAllCardsDialog(final Class C) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ListClassesActivity.this);
		builder.setMessage("Do you want to delete current '"+C.getTopics()+"' activity?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
                                boolean done = Database.deleteClass(ListClassesActivity.this, C.getClassId());

								if ( done ) {
									Toast.makeText(ListClassesActivity.this,"Deleted class successfully!", Toast.LENGTH_LONG).show();
                                    finish();
                                    startActivity(getIntent());
								}
								else
									Toast.makeText(ListClassesActivity.this,"Sorry! Could not delete class!", Toast.LENGTH_LONG).show();
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		return builder.show();
	}




}

