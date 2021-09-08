package com.example.akujobijoshua.StudentBuddy.TaskCalendar;

import android.content.Context;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.example.akujobijoshua.StudentBuddy.Homepage;
import com.example.akujobijoshua.StudentBuddy.R;
import com.example.akujobijoshua.StudentBuddy.TaskCalendar.lib.DateTimeInterpreter;
import com.example.akujobijoshua.StudentBuddy.TaskCalendar.lib.MonthLoader;
import com.example.akujobijoshua.StudentBuddy.TaskCalendar.lib.WeekView;
import com.example.akujobijoshua.StudentBuddy.TaskCalendar.lib.WeekViewEvent;
import com.example.akujobijoshua.StudentBuddy.Timetable.Batch;
import com.example.akujobijoshua.StudentBuddy.Timetable.Class;
import com.example.akujobijoshua.StudentBuddy.Timetable.Database;
import com.example.akujobijoshua.StudentBuddy.todolis.database.DBHelper;
import com.example.akujobijoshua.StudentBuddy.todolis.model.ModelTask;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.zip.Inflater;

/**
 * This is a base fragment which contains week view and all the codes necessary to initialize the
 * week view.
 * Created by akujobijoshua on 01.02.2017.
 */
public class BaseActivity extends Fragment implements WeekView.EventClickListener, MonthLoader.MonthChangeListener, WeekView.EventLongPressListener, WeekView.EmptyViewLongPressListener {
    private static final int TYPE_DAY_VIEW = 1;
    private static final int TYPE_THREE_DAY_VIEW = 2;
    private static final int TYPE_WEEK_VIEW = 3;
    private int mWeekViewType = TYPE_THREE_DAY_VIEW;
    private WeekView mWeekView;


    DBHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.activity_base, container, false);
        // Inflate the layout for this fragment
        dbHelper = new DBHelper(getContext());
        Toolbar toolbar = ((Homepage) getActivity()).toolbar;
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        // Get a reference for the week view in the layout.
        mWeekView = (WeekView) v.findViewById(R.id.weekView);

        // Show a toast message about the touched event.
        mWeekView.setOnEventClickListener(this);

        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        mWeekView.setMonthChangeListener(this);

        // Set long press listener for events.
        mWeekView.setEventLongPressListener(this);

        // Set long press listener for empty view
        mWeekView.setEmptyViewLongPressListener(this);

        // Set up a date time interpreter to interpret how the date and time will be formatted in
        // the week view. This is optional.
        setupDateTimeInterpreter(false);
        return v;
    }
    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        // Populate the week view with some events.

        List<ModelTask> tasks = new ArrayList<>();
        tasks.addAll(dbHelper.query().getTasks(DBHelper.SELECTION_STATUS + " OR "
                + DBHelper.SELECTION_STATUS, new String[]{Integer.toString(ModelTask.STATUS_CURRENT),
                Integer.toString(ModelTask.STATUS_OVERDUE)}, DBHelper.TASK_DATE_COLUMN));

        List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
        int k=1;

        for (ModelTask task : tasks){
            Calendar startTime = Calendar.getInstance();
            Calendar time = Calendar.getInstance();
            time.setTimeInMillis(task.getDate());
            startTime.set(Calendar.DAY_OF_MONTH, time.get(Calendar.DAY_OF_MONTH));
            startTime.set(Calendar.HOUR_OF_DAY, time.get(Calendar.HOUR_OF_DAY));
            startTime.set(Calendar.MINUTE, time.get(Calendar.MINUTE));
            startTime.set(Calendar.MONTH, newMonth -1);
            startTime.set(Calendar.YEAR, time.get(Calendar.YEAR));
            Calendar endTime = (Calendar) startTime.clone();
            endTime.add(Calendar.HOUR, 1);
            endTime.set(Calendar.MONTH, newMonth - 1);
            WeekViewEvent event = new WeekViewEvent(k, String.format(task.getTitle()+" of %02d:%02d %s/%d", startTime.get(Calendar.HOUR_OF_DAY), startTime.get(Calendar.MINUTE), startTime.get(Calendar.MONTH)+1, startTime.get(Calendar.DAY_OF_MONTH)), startTime, endTime);
            event.setColor(getResources().getColor(task.getPriorityColor()));
            events.add(event);
            k++;

            String.format("Event of %02d:%02d %s/%d", startTime.get(Calendar.HOUR_OF_DAY), startTime.get(Calendar.MINUTE), startTime.get(Calendar.MONTH)+1, startTime.get(Calendar.DAY_OF_MONTH));

        }
        ArrayList<Class> classList = Database.getAllClasses(getContext());
        k=1;
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        try {
        for(final Class c : classList) {
            Calendar startTime = Calendar.getInstance();
            Calendar time = Calendar.getInstance();
            Calendar time1 = Calendar.getInstance();

//convert string date to date formart
            Date d = f.parse(c.getClassDate());
            long milliseconds = d.getTime();
            time.setTimeInMillis(milliseconds);

//convert string time to time formart
            DateFormat sdf = new SimpleDateFormat("hh:mm");
            Date date = sdf.parse(c.getClassTime());
            long milliseconds1 = date.getTime();
            time1.setTimeInMillis(milliseconds1);

            startTime.set(Calendar.DAY_OF_MONTH, time.get(Calendar.DAY_OF_MONTH));
            startTime.set(Calendar.HOUR_OF_DAY, time1.get(Calendar.HOUR_OF_DAY));
            startTime.set(Calendar.MINUTE, time1.get(Calendar.MINUTE));
            startTime.set(Calendar.MONTH, newMonth -1);
            startTime.set(Calendar.YEAR, time.get(Calendar.YEAR));

            int hours =Integer.parseInt( c.getPeriod()) / 60;
            int minutes = Integer.parseInt( c.getPeriod()) % 60;

            Calendar endTime = (Calendar) startTime.clone();
            endTime.add(Calendar.HOUR, 1);
            endTime.set(Calendar.MONTH, newMonth - 1);

            WeekViewEvent event = new WeekViewEvent(k, String.format(c.getTopics()+" of %02d:%02d %s/%d", startTime.get(Calendar.HOUR_OF_DAY), startTime.get(Calendar.MINUTE), startTime.get(Calendar.MONTH)+1, startTime.get(Calendar.DAY_OF_MONTH)), startTime, endTime);
            event.setColor(getResources().getColor(R.color.event_color_04));
            events.add(event);
            k++;
        }}catch (ParseException e) {
            e.printStackTrace();
        }

        return events;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.homepage, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        setupDateTimeInterpreter(id == R.id.action_week_view);
        switch (id){
            case R.id.action_today:
                mWeekView.goToToday();
                return true;
            case R.id.action_day_view:
                if (mWeekViewType != TYPE_DAY_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_DAY_VIEW;
                    mWeekView.setNumberOfVisibleDays(1);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                }
                return true;
            case R.id.action_three_day_view:
                if (mWeekViewType != TYPE_THREE_DAY_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_THREE_DAY_VIEW;
                    mWeekView.setNumberOfVisibleDays(3);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                }
                return true;
            case R.id.action_week_view:
                if (mWeekViewType != TYPE_WEEK_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_WEEK_VIEW;
                    mWeekView.setNumberOfVisibleDays(7);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Set up a date time interpreter which will show short date values when in week view and long
     * date values otherwise.
     * @param shortDate True if the date values should be short.
     */
    private void setupDateTimeInterpreter(final boolean shortDate) {
        mWeekView.setDateTimeInterpreter(new DateTimeInterpreter() {
            @Override
            public String interpretDate(Calendar date) {
                SimpleDateFormat weekdayNameFormat = new SimpleDateFormat("EEE", Locale.getDefault());
                String weekday = weekdayNameFormat.format(date.getTime());
                SimpleDateFormat format = new SimpleDateFormat(" M/d", Locale.getDefault());

                // All android api level do not have a standard way of getting the first letter of
                // the week day name. Hence we get the first char programmatically.
                // Details: http://stackoverflow.com/questions/16959502/get-one-letter-abbreviation-of-week-day-of-a-date-in-java#answer-16959657
                if (shortDate)
                    weekday = String.valueOf(weekday.charAt(0));
                return weekday.toUpperCase() + format.format(date.getTime());
            }

            @Override
            public String interpretTime(int hour) {
                return hour > 11 ? (hour - 12) + " PM" : (hour == 0 ? "12 AM" : hour + " AM");
            }
        });
    }

    protected String getEventTitle(Calendar time) {
        return String.format("Event of %02d:%02d %s/%d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), time.get(Calendar.MONTH)+1, time.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        Toast.makeText(getContext(), "Clicked " + event.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
        Toast.makeText(getContext(), "Long pressed event: " + event.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEmptyViewLongPress(Calendar time) {
        Toast.makeText(getContext(), "Empty view long pressed: " + getEventTitle(time), Toast.LENGTH_SHORT).show();
    }

    public WeekView getWeekView() {
        return mWeekView;
    }
}
