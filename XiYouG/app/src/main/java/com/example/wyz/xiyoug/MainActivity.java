package com.example.wyz.xiyoug;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wyz.xiyoug.view.HomeFragment;
import com.example.wyz.xiyoug.view.InfoFrament;
import com.example.wyz.xiyoug.view.MyFragment;
import com.example.wyz.xiyoug.view.SlideMenu;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static SlideMenu slideMenu;
    private RelativeLayout library_layout, score_layout, our_layout, feedback_layout;

    private ViewPager viewPager;
    private List<Fragment> fragmentList;
    private LinearLayout homepage, mypage, infopage;

    int FragmentCount = 3;
    HomeFragment homeFragment;
    InfoFrament infoFrament;
    MyFragment myFragment;

    View splitline;
    int screenWidth;
    int currentTab = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitViewPager();
        setUpViewComponent();


    }

    private void InitViewPager() {

        slideMenu = (SlideMenu) findViewById(R.id.id_menu);


        fragmentList = new ArrayList<android.support.v4.app.Fragment>();
        homeFragment = new HomeFragment();
        infoFrament = new InfoFrament();
        myFragment = new MyFragment();
        fragmentList.add(homeFragment);
        fragmentList.add(infoFrament);
        fragmentList.add(myFragment);

        screenWidth = getResources().getDisplayMetrics().widthPixels;

        splitline = (View) slideMenu.getContentView().findViewById(R.id.split);

        viewPager = (ViewPager) slideMenu.getContentView().findViewById(R.id.viewpager);
        //viewPager.setOverScrollMode(View.OVER_SCROLL_NEVER);
        viewPager.setAdapter(new MyFrageStatePagerAdapter(getSupportFragmentManager()));
        viewPager.setOnPageChangeListener(new MyOnPageChangedListener());


    }

    private void setUpViewComponent() {
        library_layout = (RelativeLayout) slideMenu.getMenuView().findViewById(R.id.library);
        score_layout = (RelativeLayout) slideMenu.getMenuView().findViewById(R.id.score);
        our_layout = (RelativeLayout) slideMenu.getMenuView().findViewById(R.id.our);
        feedback_layout = (RelativeLayout) slideMenu.getMenuView().findViewById(R.id.feedback);
        Log.d("slideMenu", "ok");
        library_layout.setOnClickListener(new MySlideMenuOnClickListener(0));
        score_layout.setOnClickListener(new MySlideMenuOnClickListener(1));
        our_layout.setOnClickListener(new MySlideMenuOnClickListener(2));
        feedback_layout.setOnClickListener(new MySlideMenuOnClickListener(3));

        homepage = (LinearLayout) slideMenu.getContentView().findViewById(R.id.homepage);
        infopage = (LinearLayout) slideMenu.getContentView().findViewById(R.id.infoPage);
        mypage = (LinearLayout) slideMenu.getContentView().findViewById(R.id.myPage);

        homepage.setOnClickListener(new MyOnClickListener(0));
        infopage.setOnClickListener(new MyOnClickListener(1));
        mypage.setOnClickListener(new MyOnClickListener(2));
    }

    public static void MenuClick() {
        slideMenu.toggle();
    }

    //viewpager的适配器
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

    //手势左右滑动
    private class MyOnPageChangedListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            RelativeLayout.LayoutParams splitParams = (RelativeLayout.LayoutParams) splitline.getLayoutParams();
            splitParams.height = splitline.getMeasuredHeight();
            splitParams.width = screenWidth / FragmentCount;
            //RelativeLayout.LayoutParams splitParams=new RelativeLayout.LayoutParams(screenWidth/FragmentCount,5);

            //splitParams.addRule(RelativeLayout.ALIGN_BASELINE);
            splitline.setLayoutParams(splitParams);
            //向右滑动多个
            if (currentTab > position && (currentTab - position == 1)) {
                int xOffset = (int) (-(1 - positionOffset) * screenWidth * 1.0 / FragmentCount) + currentTab * (screenWidth / FragmentCount);
                splitline.setX(xOffset);
            } else if (currentTab == position) {
                int xOffset = (int) (positionOffset * (screenWidth * 1.0 / FragmentCount) + currentTab * (screenWidth / FragmentCount));
                splitline.setX(xOffset);
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
                home_textView.setTextColor(getResources().getColor(R.color.pressBottom));
                info_textView.setTextColor(getResources().getColor(R.color.normalBottom));
                my_textView.setTextColor(getResources().getColor(R.color.normalBottom));
                home_imageView.setImageResource(R.drawable.home_press);
                info_imageView.setImageResource(R.drawable.info);
                my_imageView.setImageResource(R.drawable.my);
                break;
            case 1:
                home_textView.setTextColor(getResources().getColor(R.color.normalBottom));
                info_textView.setTextColor(getResources().getColor(R.color.pressBottom));
                my_textView.setTextColor(getResources().getColor(R.color.normalBottom));
                home_imageView.setImageResource(R.drawable.home);
                info_imageView.setImageResource(R.drawable.info_press);
                my_imageView.setImageResource(R.drawable.my);
                break;
            case 2:
                home_textView.setTextColor(getResources().getColor(R.color.normalBottom));
                info_textView.setTextColor(getResources().getColor(R.color.normalBottom));
                my_textView.setTextColor(getResources().getColor(R.color.pressBottom));
                home_imageView.setImageResource(R.drawable.home);
                info_imageView.setImageResource(R.drawable.info);
                my_imageView.setImageResource(R.drawable.my_press);
                break;
        }
    }

    //点击下方三个按钮的触发事件
    private class MyOnClickListener implements View.OnClickListener {
        private int index = 0;
        private TextView home_textView;
        private TextView info_textView;
        private TextView my_textView;
        private ImageView home_imageView;
        private ImageView info_imageView;
        private ImageView my_imageView;

        public MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View view) {
            PageClick(index);
        }


    }

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
