package com.example.akujobijoshua.StudentBuddy.library;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Akujobi Joshua on 7/12/2016.
 */
public class PagerAdapter extends FragmentPagerAdapter {
    List<Fragment> listfragments;

    public PagerAdapter(FragmentManager fm, List<Fragment> listfragments){
        super(fm);
        this.listfragments =listfragments;
    }
    @Override
    public Fragment getItem(int i){
        return   listfragments.get(i);
    }
    @Override
    public int getCount(){
        return  listfragments.size();
    }
}
