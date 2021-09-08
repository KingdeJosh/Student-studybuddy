package com.example.akujobijoshua.StudentBuddy.Timetable;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.akujobijoshua.StudentBuddy.R;
import com.example.akujobijoshua.StudentBuddy.library.DividerItemDecoration;
import com.example.akujobijoshua.StudentBuddy.library.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

public class ListBatchesActivity  extends Fragment {

	private View v;
	private List<Batch> batchesList = new ArrayList<>();
	private RecyclerView recyclerView;
	private BatchesAdapter mAdapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		v=inflater.inflate(R.layout.listbatches, container, false);


		// Inflate the layout for this fragment
		return v;
	}


	@Override 
	public void onStart() {
		super.onStart();
		FloatingActionButton button=(FloatingActionButton) v.findViewById(R.id.butAddBatch) ;
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				Intent intent = new Intent(getContext(), AddBatchActivity.class);

				startActivity(intent);
			}
		});
		batchesList = Database.getBatches(getContext());
		recyclerView = (RecyclerView) v.findViewById(R.id.batchRecycler_view);
if(!batchesList.isEmpty()){
		mAdapter = new BatchesAdapter(batchesList);

		recyclerView.setHasFixedSize(true);
		RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
		recyclerView.setLayoutManager(mLayoutManager);
		recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		recyclerView.setAdapter(mAdapter);

		recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
			@Override
			public void onClick(View view, int position) {
				/*Batch batch = batchesList.get(position);
				Intent intent = new Intent(getContext(), ListClassesActivity.class);
				intent.putExtra("batchcode",batch.getCode());
				startActivity(intent);*/
			}

			@Override
			public void onLongClick(View view, int position) {
                Batch batch = batchesList.get(position);
                Toast.makeText(getContext(), batch.getCourse() + " is selected!", Toast.LENGTH_SHORT).show();
				createDeleteAllCardsDialog(batch);
			}
		}));

	} else if(batchesList.isEmpty()){Toast.makeText(getContext(), " Please add a time table ", Toast.LENGTH_SHORT).show();}

	}

	private AlertDialog createDeleteAllCardsDialog(final Batch batch) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		builder.setMessage("Do you want to delete current batch?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

								boolean done = Database.deleteBatch(getContext(), batch.getCode());

								if ( done ) {
									Toast.makeText(getContext(),"Deleted batch successfully!", Toast.LENGTH_LONG).show();
									ListBatchesActivity timetab= new ListBatchesActivity();
									FragmentManager manger = getFragmentManager();
									manger.beginTransaction().replace(R.id.homepage_content,
											timetab,
											timetab.getTag()).commit();


								}
								else
									Toast.makeText(getContext(),"Sorry! Could not delete batch!", Toast.LENGTH_LONG).show();
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
