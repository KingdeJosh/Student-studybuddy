package com.example.akujobijoshua.StudentBuddy.todolis;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.akujobijoshua.StudentBuddy.R;
import com.example.akujobijoshua.StudentBuddy.todolis.adapter.TabAdapter;
import com.example.akujobijoshua.StudentBuddy.todolis.alarm.AlarmHelper;
import com.example.akujobijoshua.StudentBuddy.todolis.database.DBHelper;
import com.example.akujobijoshua.StudentBuddy.todolis.dialog.AddingTaskDialogFragment;
import com.example.akujobijoshua.StudentBuddy.todolis.dialog.EditTaskDialogFragment;
import com.example.akujobijoshua.StudentBuddy.todolis.fragment.CurrentTaskFragment;
import com.example.akujobijoshua.StudentBuddy.todolis.fragment.DoneTaskFragment;
import com.example.akujobijoshua.StudentBuddy.todolis.fragment.TaskFragment;
import com.example.akujobijoshua.StudentBuddy.todolis.model.ModelTask;

//import butterknife.BindView;

//import butterknife.BindView;

public class MainAct extends AppCompatActivity
        implements AddingTaskDialogFragment.AddingTaskListener,
        CurrentTaskFragment.OnTaskDoneListener, DoneTaskFragment.OnTaskRestoreListener,
        EditTaskDialogFragment.EditingTaskListener {

    //allows to find the fragment, which is associated with Activity
    protected FragmentManager mFragmentManager;

    protected PreferenceHelper mPreferenceHelper;
    protected TabAdapter mTabAdapter;
    protected TaskFragment mCurrentTaskFragment;
    protected TaskFragment mDoneTaskFragment;
    protected DBHelper mDbHelper;

   // @BindView(R.id.search_view)
    SearchView mSearchView;
    //@BindView(R.id.toolbar)
    Toolbar mToolbar;

    public DBHelper getDbHelper() {
        return mDbHelper;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_main);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        AlarmHelper.getInstance().init(getApplicationContext());
        mSearchView=(SearchView) findViewById(R.id.search_view) ;
        mToolbar=(Toolbar) findViewById(R.id.toolbar) ;
        PreferenceHelper.getInstance().init(getApplicationContext());
        mPreferenceHelper = PreferenceHelper.getInstance();

        mDbHelper = new DBHelper(getApplicationContext());

        // Allows to find the fragment, which is associated with Activity
        mFragmentManager = getFragmentManager();

       setUI(getApplicationContext());
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
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {

    }
    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.activityResumed();
    }
    @Override
    protected void onPause() {
        super.onPause();
        MyApplication.activityPaused();
    }
    //Responsible for the user interface.
    public void setUI(Context context) {
        if (mToolbar != null) {
            mToolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.white));
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        TabLayout mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.current_task));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.done_task));

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        mTabAdapter = new TabAdapter(mFragmentManager);

        viewPager.setAdapter(mTabAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }


        });

        mCurrentTaskFragment = (CurrentTaskFragment) mTabAdapter.getItem(TabAdapter.CURRENT_TASK_FRAGMENT_POSITION);
        mDoneTaskFragment = (DoneTaskFragment) mTabAdapter.getItem(TabAdapter.DONE_TASK_FRAGMENT_POSITION);

        mSearchView = (SearchView) findViewById(R.id.search_view);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mCurrentTaskFragment.findTasks(newText);
                mDoneTaskFragment.findTasks(newText);
                return false;
            }
        });
        FloatingActionButton mFab = (FloatingActionButton) findViewById(R.id.fab);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment addingTaskDialogFragment = new AddingTaskDialogFragment();
                addingTaskDialogFragment.show(mFragmentManager, "AddingTaskDialogFragment");
            }
        });

    }


    @Override
    public void onTaskAdded(ModelTask newTask) {
        mCurrentTaskFragment.addTask(newTask, true);
    }

    @Override
    public void onTaskAddingCancel() {
        Toast.makeText(this, "Task adding cancel", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTaskDone(ModelTask task) {
        mDoneTaskFragment.addTask(task, false);
    }

    @Override
    public void onTaskRestore(ModelTask task) {
        mCurrentTaskFragment.addTask(task, false);
    }

    @Override
    public void onTaskEdited(ModelTask updatedTask) {
        mCurrentTaskFragment.updateTask(updatedTask);
        mDbHelper.update().task(updatedTask);
    }
}
