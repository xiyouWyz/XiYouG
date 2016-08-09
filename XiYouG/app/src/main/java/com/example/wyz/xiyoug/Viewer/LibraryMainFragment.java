package com.example.wyz.xiyoug.Viewer;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.example.wyz.xiyoug.R;

/**
 * Created by Wyz on 2016/7/20.
 */
public class LibraryMainFragment extends Fragment {
    private  View view;
    //包含三个Fragment，主界面中的首页，我的，信息
    private ViewPager viewPager;
    //存放三个页面的对应的Fragment
    private List<Fragment> fragmentList;
    //底部三个页面对应的按钮
    private LinearLayout homepage, mypage, infopage;
    //页面个数
    int FragmentCount = 3;
    //首页界面
    HomeFragment homeFragment;
    //信息界面
    InfoFragment infoFragment;
    //我的界面
    MyFragment myFragment;
    //分割线
    View splitLine;
    //屏幕宽度
    int screenWidth;
    //第几个界面
    int currentTab = -1;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.content,container,false);
        InitViewPager();
        setUpViewComponent();
        viewPager.setCurrentItem(0);
        return view;
    }
    private void InitViewPager() {
        fragmentList = new ArrayList<android.support.v4.app.Fragment>();
        homeFragment = new HomeFragment();
        infoFragment = new InfoFragment();
        myFragment = new MyFragment();
        fragmentList.add(homeFragment);
        fragmentList.add(infoFragment);
        fragmentList.add(myFragment);

        screenWidth = getResources().getDisplayMetrics().widthPixels;

        splitLine = (View) view.findViewById(R.id.split);

        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        //viewPager.setOverScrollMode(View.OVER_SCROLL_NEVER);

        viewPager.setAdapter(new MyFrageStatePagerAdapter(getChildFragmentManager()));
        viewPager.addOnPageChangeListener(new MyOnPageChangedListener());


    }

    private void setUpViewComponent() {

        homepage = (LinearLayout) view.findViewById(R.id.homepage);
        infopage = (LinearLayout) view.findViewById(R.id.infoPage);
        mypage = (LinearLayout) view.findViewById(R.id.myPage);

        homepage.setOnClickListener(new MyOnClickListener(0));
        infopage.setOnClickListener(new MyOnClickListener(1));
        mypage.setOnClickListener(new MyOnClickListener(2));
        ImageView home_imageView = (ImageView) homepage.getChildAt(0);
        home_imageView.setImageResource(R.drawable.home_press);
    }
    /**
     * 三个页面的的适配器
     */
    private class MyFrageStatePagerAdapter extends FragmentStatePagerAdapter {
        public MyFrageStatePagerAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }

    /**
     * 底部三个界面的滑动手势监听事件
     */
    private class MyOnPageChangedListener implements ViewPager.OnPageChangeListener {
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

        @Override
        public void onPageSelected(int position) {
            currentTab = position;
            Log.d("G_homepage_slide", "success");
            PageClick(currentTab);

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    /**
     * 切换首页，我的，信息页面
     * @param index 第index个界面
     */
    public void PageClick(int index) {

        TextView home_textView;
        TextView info_textView;
        TextView my_textView;
        ImageView home_imageView;
        ImageView info_imageView;
        ImageView my_imageView;
        viewPager.setCurrentItem(index, true);
        home_textView = (TextView) homepage.getChildAt(1);
        info_textView = (TextView) infopage.getChildAt(1);
        my_textView = (TextView) mypage.getChildAt(1);
        home_imageView = (ImageView) homepage.getChildAt(0);
        info_imageView = (ImageView) infopage.getChildAt(0);
        my_imageView = (ImageView) mypage.getChildAt(0);
        switch (index) {

            case 0:
             /*   home_textView.setTextColor(getResources().getColor(R.color.pressBottom));
                info_textView.setTextColor(getResources().getColor(R.color.normalBottom));
                my_textView.setTextColor(getResources().getColor(R.color.normalBottom));*/
                home_imageView.setImageResource(R.drawable.home_press);
                info_imageView.setImageResource(R.drawable.info);
                my_imageView.setImageResource(R.drawable.my);
                break;
            case 1:
                home_imageView.setImageResource(R.drawable.home);
                info_imageView.setImageResource(R.drawable.info_press);
                my_imageView.setImageResource(R.drawable.my);
                break;
            case 2:
                home_imageView.setImageResource(R.drawable.home);
                info_imageView.setImageResource(R.drawable.info);
                my_imageView.setImageResource(R.drawable.my_press);
                break;
        }
    }

    /**
     * 下方三个界面按钮的点击事件
     */
    private class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View view) {
            PageClick(index);
        }


    }

    /**
     * 侧边栏每个Item的点击事件
     */
    private class MySlideMenuOnClickListener implements View.OnClickListener {

        private int index = 0;

        public MySlideMenuOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View view) {
            Log.d("slideMenuClick", String.valueOf(index));
            switch (index) {

                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    Uri uri = Uri.parse("http://www.xiyou.edu.cn/");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                    break;
                case 3:
                    Uri uri1 = Uri.parse("mailto:745322878@qq.com");
                    Intent intent1 = new Intent(Intent.ACTION_SENDTO, uri1);
                    startActivity(intent1);
                    break;
            }
        }
    }
}
