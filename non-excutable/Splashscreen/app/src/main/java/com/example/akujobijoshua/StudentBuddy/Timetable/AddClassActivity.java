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
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.akujobijoshua.StudentBuddy.R;

import java.util.Calendar;

public class AddClassActivity extends AppCompatActivity {
	private static final int DATE_DIALOG = 1;
	private static final int TIME_DIALOG = 2;
	private int day, month, year, hours, mins;
	private EditText textClassDate, textClassTime, textBatchCode;
	private EditText editPeriod,description, textCourse,editLecturer,editRoom;
	private CheckBox chkAdjust;
	private RadioGroup radioTypeGroup;
	private RadioButton radioTimeTBButton;
    private String radiovalue;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addclass);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		textBatchCode = (EditText) this.findViewById(R.id.textBatchCode);
		textClassDate = (EditText) this.findViewById(R.id.textClassDate);
		textClassTime = (EditText) this.findViewById(R.id.textClassTime);

		radioTypeGroup = (RadioGroup) findViewById(R.id.radioType);
		editLecturer = (EditText) this.findViewById(R.id.editLecturer) ;
		editPeriod = (EditText) this.findViewById(R.id.editPeriod) ;
		description = (EditText) this.findViewById(R.id.description) ;
		textCourse = (EditText) this.findViewById(R.id.textCourse) ;
		editRoom = (EditText) this.findViewById(R.id.editRoom) ;

		chkAdjust = (CheckBox) this.findViewById(R.id.chkAdjust);

		int selectedId = radioTypeGroup.getCheckedRadioButtonId();
		radioTimeTBButton= (RadioButton) findViewById(selectedId);
        radioTypeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                                  @Override
                                                  public void onCheckedChanged(RadioGroup group, int checkedId)
                                                  {
                                                      radioTimeTBButton = (RadioButton) findViewById(checkedId);
                                                      radiovalue= radioTimeTBButton.getText().toString();
                                                      Toast.makeText(getBaseContext(), radioTimeTBButton.getText(), Toast.LENGTH_SHORT).show();
                                                  }
                                              }
        );
		textClassDate.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				//do your stuff here..
				showDialog(DATE_DIALOG);
				return false;
				}
			});
		textClassTime.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				//do your stuff here..
				showDialog(TIME_DIALOG);
				return false;
			}
		});
		textBatchCode.setText(   getIntent().getStringExtra("batchcode"));
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

	public void addClass(View v) {
		boolean done = Database.addClass(this,
				textBatchCode.getText().toString(),
				textClassDate.getText().toString(),
				textClassTime.getText().toString(),
				editPeriod.getText().toString(),
				textCourse.getText().toString(),
				description.getText().toString(),
				chkAdjust.isChecked(),
				editLecturer.getText().toString(),
				radiovalue,
				editRoom.getText().toString());

		if ( done ){
			Toast.makeText(this,"Added Class Successfully!", Toast.LENGTH_LONG).show();
			onBackPressed();}
		else
			Toast.makeText(this,"Sorry! Could not add class!", Toast.LENGTH_LONG).show();
	}


	public void showDatePicker(View v) {
		showDialog(DATE_DIALOG);
	}

	public void showTimePicker(View v) {
		showDialog(TIME_DIALOG);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		super.onCreateDialog(id);

		switch (id) {
			case DATE_DIALOG:
				return new DatePickerDialog(this, dateSetListener, year, month, day);
			case TIME_DIALOG:
				return new TimePickerDialog(this, timeSetListener, hours,mins, false);
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


	private TimePickerDialog.OnTimeSetListener timeSetListener =
			new TimePickerDialog.OnTimeSetListener() {

				@Override
				public void onTimeSet(TimePicker arg0, int pHours, int  pMins) {
					hours = pHours;
					mins = pMins;
					updateTimeDisplay();
				}

			};



	private void updateDateDisplay() {
		// Month is 0 based so add 1
		textClassDate.setText(String.format("%04d-%02d-%02d", year, month + 1,day));
	}

	private void updateTimeDisplay() {
		// Month is 0 based so add 1
		textClassTime.setText(String.format("%02d:%02d", hours,mins));
	}
}
