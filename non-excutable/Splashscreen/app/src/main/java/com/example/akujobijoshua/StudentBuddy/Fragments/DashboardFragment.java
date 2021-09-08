package com.example.akujobijoshua.StudentBuddy.Fragments;


import java.util.List;
import java.util.Vector;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.HorizontalScrollView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TextView;

import com.example.akujobijoshua.StudentBuddy.R;
import com.example.akujobijoshua.StudentBuddy.library.PagerAdapter;



/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment implements OnTabChangeListener,
        OnPageChangeListener  {


    private TabHost tabHost;
    private ViewPager viewPager;
    private PagerAdapter myViewPagerAdapter;
    int i = 0;
    View v;
    FloatingActionButton fab_plus,fab_course,fab_class;
    Animation fab_open,fab_close,fab_Clockwise,Fab_antiClockwise;
    boolean isOpen=true;
    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_dashboard, container, false);

        // We put TabHostView Pager here:
		/*
		 * part1*****************************************************************
		 * ********  DONE! !!!!! :))
		 */

        i++;

        // init tabhost
        this.initializeTabHost(savedInstanceState);

        // init ViewPager
        this.initializeViewPager();

       // fab_class =(FloatingActionButton) v.findViewById(R.id.fab_Class);
       // fab_course=(FloatingActionButton) v.findViewById(R.id.fab_Course);
        fab_plus=(FloatingActionButton) v.findViewById(R.id.fab_plus);
        fab_open= AnimationUtils.loadAnimation(getContext(),R.anim.fab_open);
        fab_close= AnimationUtils.loadAnimation(getContext(),R.anim.fab_close);
        fab_Clockwise= AnimationUtils.loadAnimation(getContext(),R.anim.rotate_clockwise);
        Fab_antiClockwise= AnimationUtils.loadAnimation(getContext(),R.anim.rotate_anticlockwise);
        fab_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddCourse.class);
                startActivity(intent);
                //finish();
            }
        });

		/*
		 * part1*****************************************************************
		 * ********
		 */
        return v;
    }
    // fake content for tabhost
    class FakeContent implements TabContentFactory {
        private final Context mContext;

        public FakeContent(Context context) {
            mContext = context;
        }

        @Override
        public View createTabContent(String tag) {
            View v = new View(mContext);
            v.setMinimumHeight(0);
            v.setMinimumWidth(0);
            return v;
        }
    }

    private void initializeViewPager() {
        List<Fragment> fragments = new Vector<Fragment>();

        fragments.add(new TodayFragment());
        fragments.add(new TaskDueFragment());
        fragments.add(new Schedulefragment());

        this.myViewPagerAdapter = new PagerAdapter(
                getChildFragmentManager(), fragments);
        this.viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        this.viewPager.setAdapter(this.myViewPagerAdapter);
        this.viewPager.setOnPageChangeListener(this);

    }

    private void initializeTabHost(Bundle args) {

        tabHost = (TabHost) v.findViewById(android.R.id.tabhost);
        tabHost.setup();
        String[] tabNames= {"Classes","Reading","Exams"};
        for (int i = 0; i < tabNames.length; i++) {

            TabHost.TabSpec tabSpec;
            tabSpec = tabHost.newTabSpec(tabNames[i]);
            tabSpec.setIndicator(tabNames[i]);
            tabSpec.setContent(new FakeContent(getActivity()));
            tabHost.addTab(tabSpec);
        }

        tabHost.setOnTabChangedListener(this);
    }

    @Override
    public void onTabChanged(String tabId) {
        int pos = this.tabHost.getCurrentTab();
        this.viewPager.setCurrentItem(pos);

        HorizontalScrollView hScrollView = (HorizontalScrollView) v
                .findViewById(R.id.h_scrool_view);
        View tabView = tabHost.getCurrentTabView();
        int scrollPos = tabView.getLeft()
                - (hScrollView.getWidth() - tabView.getWidth()) / 2;
        hScrollView.smoothScrollTo(scrollPos, 0);

    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override
    public void onPageSelected(int position) {
        this.tabHost.setCurrentTab(position);
    }
}
