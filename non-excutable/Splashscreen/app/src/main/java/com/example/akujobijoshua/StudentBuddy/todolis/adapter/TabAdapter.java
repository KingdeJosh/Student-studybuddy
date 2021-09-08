package com.example.akujobijoshua.StudentBuddy.todolis.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

import com.example.akujobijoshua.StudentBuddy.todolis.fragment.CurrentTaskFragment;
import com.example.akujobijoshua.StudentBuddy.todolis.fragment.DoneTaskFragment;


/**
 * Created by akujobijoshua on 01.02.2017.
 */
public class TabAdapter extends FragmentStatePagerAdapter {

    public static final int CURRENT_TASK_FRAGMENT_POSITION = 0;
    public static final int DONE_TASK_FRAGMENT_POSITION = 1;

    private Fragment[] mFragments = {
            new CurrentTaskFragment(),
            new DoneTaskFragment(),
    };

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments[position];
    }

    @Override
    public int getCount() {
        return mFragments.length;
    }

}
