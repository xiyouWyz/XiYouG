/*
package com.example.wyz.xiyoug.View;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.wyz.xiyoug.R;

import java.util.ArrayList;
import java.util.List;

*/
/**
 * Created by Wyz on 2016/8/1.
 *//*

public class ScheduleMainFragment extends Fragment {
    private  View view;
    private ViewPager viewPager;
    private List<Fragment> fragments;
    private LinearLayout scheduleLayout,myLayout;
    private int FragmentCount=2;
    ScheduleFragment scheduleFragment;

    View splitLine;
    int screenWidth;
    int currentTab=-1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.schedule_main,container,false);
        InitViewPager();
        setupViewComponent();
        viewPager.setCurrentItem(0);
        return  view;
    }

    private void setupViewComponent() {
        scheduleLayout=(LinearLayout)view.findViewById(R.id.schedule);
        myLayout=(LinearLayout)view.findViewById(R.id.my);
        scheduleLayout.setOnClickListener(new MyOnClickListener(0));
        myLayout.setOnClickListener(new MyOnClickListener(1));
    }

    private void InitViewPager() {
        fragments=new ArrayList<>();
        scheduleFragment=new ScheduleFragment();
        fragments.add(scheduleFragment);
        screenWidth=getResources().getDisplayMetrics().widthPixels;
        splitLine=view.findViewById(R.id.split);
        viewPager=(ViewPager) view.findViewById(R.id.viewpager);
        viewPager.addOnPageChangeListener(new MyOnPageChangedListener());
        viewPager.setAdapter(new MyFrageStatePagerAdapter(getChildFragmentManager()));
    }


    private class MyOnPageChangedListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageSelected(int position) {
            currentTab=position;
            Schedule_Click(currentTab);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            RelativeLayout.LayoutParams splitParams = (RelativeLayout.LayoutParams) splitLine.getLayoutParams();
            splitParams.height = splitLine.getMeasuredHeight();
            splitParams.width = screenWidth / FragmentCount;
            //RelativeLayout.LayoutParams splitParams=new RelativeLayout.LayoutParams(screenWidth/FragmentCount,5);

            //splitParams.addRule(RelativeLayout.ALIGN_BASELINE);
            splitLine.setLayoutParams(splitParams);
            //向右滑动多个
            if (currentTab > position && (currentTab - position == 1)) {
                int xOffset = (int) (-(1 - positionOffset) * screenWidth * 1.0 / FragmentCount) + currentTab * (screenWidth / FragmentCount);
                splitLine.setX(xOffset);
            } else if (currentTab == position) {
                int xOffset = (int) (positionOffset * (screenWidth * 1.0 / FragmentCount) + currentTab * (screenWidth / FragmentCount));
                splitLine.setX(xOffset);
            }
        }

    }

    private class MyFrageStatePagerAdapter extends FragmentStatePagerAdapter {

        public MyFrageStatePagerAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
        }
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            if(fragments!=null)
            {
                return  fragments.size();
            }
            return 0;
        }
    }

    private   void Schedule_Click(int index)
    {
        ImageView schedule_view;
        ImageView schedule_my_view;
        viewPager.setCurrentItem(index);
        schedule_view=(ImageView)scheduleLayout.getChildAt(0);
        schedule_my_view=(ImageView)myLayout.getChildAt(0);
        switch (index)
        {
            case  0:
                schedule_view.setImageResource(R.drawable.schedule);
                schedule_my_view.setImageResource(R.drawable.my);
                break;
            case 1:
                schedule_view.setImageResource(R.drawable.schedule);
                schedule_my_view.setImageResource(R.drawable.my_press);
                break;
        }
    }

    private class MyOnClickListener implements View.OnClickListener {
        private  int index;
        public MyOnClickListener(int i) {
            index=i;
        }

        @Override
        public void onClick(View view) {
            Schedule_Click(index);
        }
    }
}
*/
