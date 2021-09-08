package com.example.akujobijoshua.StudentBuddy.Timetable;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.akujobijoshua.StudentBuddy.R;

import java.util.Calendar;

public class AddBatchActivity extends AppCompatActivity {
	private static final int DATE_DIALOG = 1;
	private static final int TIME_DIALOG = 2;
    private int DIALOG_CONDITION = 2;
	private int day, month, year, hours, mins;
	private EditText textStartDate, textEndDate;
	private EditText editBatchcode,editCourse, editRemarks;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addbatch);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		textStartDate = (EditText) this.findViewById(R.id.textBatchDate);
		textEndDate = (EditText) this.findViewById(R.id.endBatchDate);

		editBatchcode = (EditText) this.findViewById(R.id.editBatchCode) ;
		editCourse = (EditText) this.findViewById(R.id.editCourse) ;
		editRemarks = (EditText) this.findViewById(R.id.editRemarks) ;
        textStartDate.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                //do your stuff here..
                DIALOG_CONDITION =0;
                showDialog(DATE_DIALOG);
                return false;
            }
        });
        textEndDate.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                //do your stuff here..
                DIALOG_CONDITION =1;
                showDialog(DATE_DIALOG);
                return false;
            }
        });
		setDateToSysdate();
		//updateDateDisplay();

	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case android.R.id.home:
				onBackPressed();
		}
		return super.onOptionsItemSelected(item);
	}
	private void setDateToSysdate() {
		Calendar c = Calendar.getInstance();
		day = c.get(Calendar.DAY_OF_MONTH);
		month = c.get(Calendar.MONTH);
		year = c.get(Calendar.YEAR);
	}

	public void addBatch(View v) {
		boolean done = Database.addBatch(this,
				editBatchcode.getText().toString(),
				editCourse.getText().toString(),
				textStartDate.getText().toString(),
				editRemarks.getText().toString(),
                textEndDate.getText().toString()
        );

		if ( done ){
			Toast.makeText(this,"Added batch successfully!", Toast.LENGTH_LONG).show();
            onBackPressed();}
		else
			Toast.makeText(this,"Sorry! Could not add batch!", Toast.LENGTH_LONG).show();
	}

/*

	public void showDatePicker(View v) {
		showDialog(DATE_DIALOG);
	}
*/

/*
	public void showTimePicker(View v) {
		showDialog(TIME_DIALOG);
	}
*/

	@Override
	protected Dialog onCreateDialog(int id) {
		super.onCreateDialog(id);

		switch (id) {
			case DATE_DIALOG:
				return new DatePickerDialog(this, dateSetListener, year, month, day);
			/*case TIME_DIALOG:
				return new TimePickerDialog(this, timeSetListener, hours,mins, false);*/
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int pYear, int pMonth, int pDay) {
			year = pYear;
			month = pMonth;
			day = pDay;
			updateDateDisplay();
		}
	};


	/*private TimePickerDialog.OnTimeSetListener timeSetListener =
			new TimePickerDialog.OnTimeSetListener() {

				@Override
				public void onTimeSet(TimePicker arg0, int pHours, int  pMins) {
					hours = pHours;
					mins = pMins;
					updateTimeDisplay();
				}

			};*/



	private void updateDateDisplay() {
		// Month is 0 based so add 1
        if(DIALOG_CONDITION==0)
		textStartDate.setText(String.format("%04d-%02d-%02d", year, month + 1,day));
        else if(DIALOG_CONDITION==1)
            textEndDate.setText(String.format("%04d-%02d-%02d", year, month + 1,day));
	}

	/*private void updateTimeDisplay() {
		// Month is 0 based so add 1
		textStartTime.setText(String.format("%02d:%02d", hours,mins));
	}*/

}