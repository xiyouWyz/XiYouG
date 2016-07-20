package com.example.wyz.xiyoug.view;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wyz.xiyoug.MainActivity;
import com.example.wyz.xiyoug.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wyz on 2016/7/17.
 */
public class HomeFragment extends Fragment {
    //轮播图
    private ViewPager slide_viewPager;
    //排行榜
    private ViewPager rank_viewPager;
    //home_page页面
    private View view;
    //轮播图View集合
    private List<View> slide_viewList;
    //排行榜View集合
    private List<View> rank_viewList;
    //侧边栏按钮
    private ImageView menuView;
    //是否第一次加载
    private boolean isFirst = true;
    //轮播图下标
    int slide_currentTab = 0;
    //排行榜下标
    int rank_currentTab = 0;
    //三个排行榜
    private LinearLayout rank_collection, rank_borrow, rank_look;
   //三个排行榜圆形视图
    private  RelativeLayout rank_collection_img,rank_borrow_img,rank_look_img;


    private Handler myhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            slide_currentTab = msg.what;
            slide_viewPager.setCurrentItem(slide_currentTab, true);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_page, container, false);
        InitSlideViewPager();
        InitRankViewPager();
        setupViewCompent();
        return view;
    }

    private void setupViewCompent() {
        menuView = (ImageView) view.findViewById(R.id.menu);
        Log.d("menu", "ok");
        menuView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("menuClick", "ok");
                MainActivity.MenuClick();
            }

        });

        rank_collection = (LinearLayout) view.findViewById(R.id.rank_collection_btn);
        rank_borrow = (LinearLayout) view.findViewById(R.id.rank_borrow_btn);
        rank_look = (LinearLayout) view.findViewById(R.id.rank_look_btn);
        rank_collection_img=(RelativeLayout)view.findViewById(R.id.rank_collection_img);
        rank_borrow_img=(RelativeLayout)view.findViewById(R.id.rank_borrow_img);
        rank_look_img=(RelativeLayout)view.findViewById(R.id.rank_look_img);

        rank_collection.setOnClickListener(new MyRankOnClickListener(0));
        rank_borrow.setOnClickListener(new MyRankOnClickListener(1));
        rank_look.setOnClickListener(new MyRankOnClickListener(2));
        rank_collection_img.setOnClickListener(new MyRankOnClickListener(0));
        rank_borrow_img.setOnClickListener(new MyRankOnClickListener(1));
        rank_look_img.setOnClickListener(new MyRankOnClickListener(2));


    }

    private void InitRankViewPager() {
        rank_viewPager = (ViewPager) view.findViewById(R.id.viewpager_rank);

        View rank_view1 = LayoutInflater.from(getContext()).inflate(R.layout.rank_collection_recyclerview, null);
        View rank_view2 = LayoutInflater.from(getContext()).inflate(R.layout.rank_borrow_recyclerview, null);
        View rank_view3 = LayoutInflater.from(getContext()).inflate(R.layout.rank_look_recyclerview, null);
        rank_viewList = new ArrayList<>();
        rank_viewList.add(rank_view1);
        rank_viewList.add(rank_view2);
        rank_viewList.add(rank_view3);

        rank_viewPager.setAdapter(new MyRankFrageStatePagerAdapter());
        rank_viewPager.setCurrentItem(0);
        rank_viewPager.setOnPageChangeListener(new MyRankOnPageChangedListener());

    }

    private void InitSlideViewPager() {
        slide_viewPager = (ViewPager) view.findViewById(R.id.slideshow);


        //View slide_view_begin=LayoutInflater.from(getContext()).inflate(R.layout.slide_show_three,null);
        View slide_view0 = LayoutInflater.from(getContext()).inflate(R.layout.slide_show_one, null);
        slide_view0.setOnClickListener(new MySlideShowOnClickListener(0));
        Log.d("slideshow", "0");
        View slide_view1 = LayoutInflater.from(getContext()).inflate(R.layout.slide_show_two, null);
        slide_view1.setOnClickListener(new MySlideShowOnClickListener(1));
        Log.d("slideshow", "1");
        View slide_view2 = LayoutInflater.from(getContext()).inflate(R.layout.slide_show_three, null);
        slide_view2.setOnClickListener(new MySlideShowOnClickListener(2));
        Log.d("slideshow", "2");
        //View slide_view_end=LayoutInflater.from(getContext()).inflate(R.layout.slide_show_one,null);

        slide_viewList = new ArrayList<>();
        slide_viewList.add(slide_view0);
        slide_viewList.add(slide_view1);
        slide_viewList.add(slide_view2);

        slide_viewPager.setAdapter(new MyFrageStatePagerAdapter());
        slide_viewPager.setCurrentItem(1, true);
        slide_viewPager.setOnPageChangeListener(new MyOnPageChangedListener());

        MyThread th = new MyThread();
        Thread t = new Thread(th);
        if (isFirst) {
            t.start();
            isFirst = false;
        } else if (t == null) {

            t.start();
        }

    }


    public class MyThread implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(5000);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("slide", "Slide Error");
                }
                slide_currentTab += 1;
                if (slide_currentTab == 3) {
                    slide_currentTab = 0;
                    myhandler.sendEmptyMessage(slide_currentTab);
                } else {
                    myhandler.sendEmptyMessage(slide_currentTab);
                }
            }

        }
    }


    private class MySlideShowOnClickListener implements View.OnClickListener {
        private int index;
        private String url;

        public MySlideShowOnClickListener(int i) {
            index = i;
        }

        @Override

        public void onClick(View view) {
            switch (index) {
                case 0:
                    url = "http://www.xiyou.edu.cn/";
                    break;
                case 1:
                    url = "http://jyc.xupt.edu.cn/";
                    break;
                case 2:
                    url = "http://www.lib.xiyou.edu.cn/";
                    break;
            }
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            getContext().startActivity(intent);
        }
    }

    private class MyRankOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyRankOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View view) {
            Rank_Click(index);
        }
    }

    private class MyFrageStatePagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return slide_viewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        /**
         * 获得实例化对象
         *
         * @param container
         * @param position
         * @return
         */

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(slide_viewList.get(position));
            return slide_viewList.get(position);

        }

        /**
         * 销毁对象
         *
         * @param container
         * @param position
         * @param object
         */

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(slide_viewList.get(position));

        }
    }

    private class MyRankFrageStatePagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return rank_viewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(rank_viewList.get(position));
            return rank_viewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(rank_viewList.get(position));

        }
    }

    private class MyOnPageChangedListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            slide_currentTab = position;
            slide_viewPager.setCurrentItem(slide_currentTab, true);
            //Log.d("slideShowItemPageSelect",String.valueOf(position));
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private class MyRankOnPageChangedListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }


        @Override
        public void onPageSelected(int position) {
            rank_currentTab = position;
            Rank_Click(rank_currentTab);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private  void Rank_Click(int index)
    {

        TextView rank_collection_text,rank_borrow_text,rank_look_text;
        rank_collection_text=(TextView)view.findViewById(R.id.rank_collection_text);
        rank_borrow_text=(TextView)view.findViewById(R.id.rank_borrow_text);
        rank_look_text=(TextView)view.findViewById(R.id.rank_look_text);
        rank_viewPager.setCurrentItem(index,true);
        switch (index)
        {
            case 0:
                rank_collection_text.setTextColor(getResources().getColor(R.color.rank_text_press));
                rank_borrow_text.setTextColor(getResources().getColor(R.color.rank_text));
                rank_look_text.setTextColor(getResources().getColor(R.color.rank_text));
                break;
            case 1:
                rank_collection_text.setTextColor(getResources().getColor(R.color.rank_text));
                rank_borrow_text.setTextColor(getResources().getColor(R.color.rank_text_press));
                rank_look_text.setTextColor(getResources().getColor(R.color.rank_text));
                break;
            case  2:
                rank_collection_text.setTextColor(getResources().getColor(R.color.rank_text));
                rank_borrow_text.setTextColor(getResources().getColor(R.color.rank_text));
                rank_look_text.setTextColor(getResources().getColor(R.color.rank_text_press));
                break;
        }
    }


}
