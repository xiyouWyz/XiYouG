package com.example.wyz.xiyoug.Viewer;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wyz.xiyoug.Activity.BookDetailActivity;
import com.example.wyz.xiyoug.Model.Book_Rank;
import com.example.wyz.xiyoug.Model.HttpLinkHeader;
import com.example.wyz.xiyoug.R;
import com.example.wyz.xiyoug.Util.OkHttpUtil;
import com.example.wyz.xiyoug.Util.ReadFile;
import com.example.wyz.xiyoug.Util.SaveFile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wyz on 2016/7/17.
 */
public class HomeFragment extends Fragment {
    //轮播图
    private ViewPager slide_viewPager;
    //home_page页面
    private View view;
    //轮播图View集合
    private List<View> slide_viewList;
    //是否第一次加载
    private boolean isFirst = true;
    //轮播图下标
    int slide_currentTab = 0;

    //排行榜下标
    private int rank_index;
    //三个排行榜
    private LinearLayout rank_collection, rank_borrow, rank_look;
    //三个排行榜圆形视图
    private RelativeLayout rank_collection_img, rank_borrow_img, rank_look_img;

    //排行榜数据显示listview
    private ListView pullListView;
    //收藏排行榜信息存储
    private List<Book_Rank> book_col_ranks = new ArrayList<>();
    //借阅排行榜信息存储
    private List<Book_Rank> book_bor_ranks = new ArrayList<>();
    //查看排行榜信息存储
    private List<Book_Rank> book_look_ranks = new ArrayList<>();
    //排行榜listview的适配器
    private MyAdapter adapter;
    //下拉刷新控件
    private SwipeRefreshLayout swipeRefreshLayout;
    //下拉刷新处理
    private Handler handler_pull = new Handler();
    //排行榜信息获取线程
    private Rank_Thread thread_rankInfo;
    //排行榜信息获取后更新界面处理
    private Rank_Handler handler_rankInfo = new Rank_Handler();
    //轮播图界面更新处理
    private Handler myhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            slide_currentTab = msg.what;
            slide_viewPager.setCurrentItem(slide_currentTab, true);
        }
    };
    private String TAG = "HomeFragment";

    private  View itemView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_page, container, false);
        InitSlideViewPager();

        //initData(0);
        setupViewCompent();
        getDataFromFile(0);
        return view;
    }

    private void getDataFromFile(int i) {
        String colRankInfo = ReadFile.readColRank(getContext());
        if (colRankInfo != null) {
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONObject(colRankInfo).getJSONArray("Detail");
                book_col_ranks=jsonArrayToList(jsonArray,book_col_ranks);
                setupListView(book_col_ranks);
                Log.d(TAG,"从文件中加载数据");
            } catch (JSONException e) {
                Log.d(TAG, e.toString());
                initData(i);
            }

        }
        else
        {
            //adapter.notifyDataSetChanged();
            initData(i);
        }
    }

    private void initData(int index) {
        thread_rankInfo = new Rank_Thread(index);
        new Thread(thread_rankInfo).start();
    }

    private void setupViewCompent() {
        rank_collection = (LinearLayout) view.findViewById(R.id.rank_collection_btn);
        rank_borrow = (LinearLayout) view.findViewById(R.id.rank_borrow_btn);
        rank_look = (LinearLayout) view.findViewById(R.id.rank_look_btn);
        rank_collection_img = (RelativeLayout) view.findViewById(R.id.rank_collection_img);
        rank_borrow_img = (RelativeLayout) view.findViewById(R.id.rank_borrow_img);
        rank_look_img = (RelativeLayout) view.findViewById(R.id.rank_look_img);

        rank_collection.setOnClickListener(new MyRankOnClickListener(0));
        rank_borrow.setOnClickListener(new MyRankOnClickListener(1));
        rank_look.setOnClickListener(new MyRankOnClickListener(2));
        /*rank_collection_img.setOnClickListener(new MyRankOnClickListener(0));
        rank_borrow_img.setOnClickListener(new MyRankOnClickListener(1));
        rank_look_img.setOnClickListener(new MyRankOnClickListener(2));*/

        pullListView = (ListView) view.findViewById(R.id.plv_data);
        pullListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        pullListView.setAdapter(adapter);

        pullListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (rank_index) {
                    case 0:
                        Log.d(TAG, "点击了收藏的第" + i + "个item" + book_col_ranks.get(i).getTitle());
                        String url = HttpLinkHeader.BOOK_DETAIL_ID + book_col_ranks.get(i).getId();
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putString("url", url);
                        intent.putExtras(bundle);
                        intent.setClass(getActivity(), BookDetailActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        Log.d(TAG, "点击了借阅的第" + i + "个item" + book_bor_ranks.get(i).getTitle());
                        String url1 = HttpLinkHeader.BOOK_DETAIL_ID + book_bor_ranks.get(i).getId();
                        Intent intent1 = new Intent();
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("url", url1);
                        intent1.putExtras(bundle1);
                        intent1.setClass(getActivity(), BookDetailActivity.class);
                        startActivity(intent1);
                        break;
                    case 2:
                        Log.d(TAG, "点击了查看的第" + i + "个item" + book_look_ranks.get(i).getTitle());
                        String url2 = HttpLinkHeader.BOOK_DETAIL_ID + book_look_ranks.get(i).getId();
                        Intent intent2 = new Intent();
                        Bundle bundle2 = new Bundle();
                        bundle2.putString("url", url2);
                        intent2.putExtras(bundle2);
                        intent2.setClass(getActivity(), BookDetailActivity.class);
                        startActivity(intent2);
                        break;
                }
            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                handler_pull.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getData();
                    }
                }, 2000);
            }
        });


    }

    private void setupListView(List<Book_Rank> ranks) {
        adapter = new MyAdapter(ranks);
        pullListView.setAdapter(adapter);
        //Toast.makeText(getContext(),"刷新成功",Toast.LENGTH_SHORT).show();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void getData() {
       /* if(adapter!=null)
        {*/
        initData(rank_index);
       /* }*/


    }

    /**
     * 初始化轮播图
     */
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

        slide_viewPager.setAdapter(new MySlideShowFrageStatePagerAdapter());
        slide_viewPager.setCurrentItem(0, true);
        slide_viewPager.setOnPageChangeListener(new MySlideShowOnPageChangedListener());

        MyThread th = new MyThread();
        Thread t = new Thread(th);
        if (isFirst) {
            t.start();
            isFirst = false;
        } else if (t == null) {

            t.start();
        }

    }

    /**
     * 通过线程让轮播图轮播起来
     */
    private class MyThread implements Runnable {
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

    /**
     * 轮播图的点击事件
     */
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

    /**
     * 轮播图的页面改变监听事件
     */
    private class MySlideShowOnPageChangedListener implements ViewPager.OnPageChangeListener {
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

    /**
     * 轮播图的适配器
     */
    private class MySlideShowFrageStatePagerAdapter extends PagerAdapter {
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

    /**
     * 切换排行榜页面
     *
     * @param index 第index个界面
     */
    private void Rank_Click(int index) {

        TextView rank_collection_text, rank_borrow_text, rank_look_text;
        rank_collection_text = (TextView) view.findViewById(R.id.rank_collection_text);
        rank_borrow_text = (TextView) view.findViewById(R.id.rank_borrow_text);
        rank_look_text = (TextView) view.findViewById(R.id.rank_look_text);
        swipeRefreshLayout.setRefreshing(false);
        switch (index) {
            case 0:
                rank_collection_text.setTextColor(getResources().getColor(R.color.rank_text_press));
                rank_borrow_text.setTextColor(getResources().getColor(R.color.rank_text));
                rank_look_text.setTextColor(getResources().getColor(R.color.rank_text));
                if (book_col_ranks.size() == 0) {
                    String colRankInfo = ReadFile.readColRank(getContext());
                    if (colRankInfo != null) {
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONObject(colRankInfo).getJSONArray("Detail");
                            book_col_ranks=jsonArrayToList(jsonArray,book_col_ranks);
                            setupListView(book_col_ranks);
                            Log.d(TAG,"从文件中加载数据");
                        } catch (JSONException e) {
                            Log.d(TAG, e.toString());
                            initData(index);
                        }

                    }
                    else
                    {
                        //adapter.notifyDataSetChanged();
                        initData(index);
                    }

                } else {
                    setupListView(book_col_ranks);
                }

                break;
            case 1:
                rank_collection_text.setTextColor(getResources().getColor(R.color.rank_text));
                rank_borrow_text.setTextColor(getResources().getColor(R.color.rank_text_press));
                rank_look_text.setTextColor(getResources().getColor(R.color.rank_text));
                if (book_bor_ranks.size() == 0) {
                    String borRankInfo = ReadFile.readBorRank(getContext());
                    if (borRankInfo != null) {
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONObject(borRankInfo).getJSONArray("Detail");
                            book_bor_ranks=jsonArrayToList(jsonArray,book_bor_ranks);
                            setupListView(book_bor_ranks);
                            Log.d(TAG,"从文件中加载数据");
                        } catch (JSONException e) {
                            Log.d(TAG, e.toString());
                            initData(index);
                        }
                    }
                    else
                    {
                        // adapter.notifyDataSetChanged();
                        initData(index);
                    }

                } else {
                    //adapter.notifyDataSetChanged();
                    setupListView(book_bor_ranks);
                }
                break;
            case 2:
                rank_collection_text.setTextColor(getResources().getColor(R.color.rank_text));
                rank_borrow_text.setTextColor(getResources().getColor(R.color.rank_text));
                rank_look_text.setTextColor(getResources().getColor(R.color.rank_text_press));
                if (book_look_ranks.size() == 0) {
                    String lookRankInfo = ReadFile.readLookRank(getContext());
                    if (lookRankInfo != null) {
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONObject(lookRankInfo).getJSONArray("Detail");
                            book_look_ranks=jsonArrayToList(jsonArray,book_look_ranks);
                            setupListView(book_look_ranks);
                            Log.d(TAG,"从文件中加载数据");
                        } catch (JSONException e) {
                            Log.d(TAG, e.toString());
                            initData(index);
                        }

                    }
                    else
                    {
                        // adapter.notifyDataSetChanged();
                        initData(index);
                    }
                } else {
                    //adapter.notifyDataSetChanged();
                    setupListView(book_look_ranks);
                }
                break;
        }
    }

    /**
     * 排行榜按钮是图的点击事件，用来切换页面
     */
    private class MyRankOnClickListener implements View.OnClickListener {
        int index = 0;

        public MyRankOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View view) {
            Rank_Click(index);
            rank_index = index;
            Log.d("rank_click", "" + rank_index);
        }
    }


    private class MyAdapter extends BaseAdapter {
        private List<Book_Rank> bRanks;

        public MyAdapter(List<Book_Rank> objects) {
            bRanks = objects;
        }

        @Override
        public int getCount() {
            return bRanks.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }


        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            MyViewHolder holder = null;
            if (view == null) {

                holder = new MyViewHolder();
                view = LayoutInflater.from(getContext()).inflate(R.layout.rank_list_view_item, null);

                holder.rank_book_name = (TextView) view.findViewById(R.id.rank_item_book_name);
                holder.rank_count = (TextView) view.findViewById(R.id.rank_item_count);
                view.setTag(holder);
            } else {
                holder = (MyViewHolder) view.getTag();
            }
            holder.rank_book_name.setText(bRanks.get(i).getTitle());
            holder.rank_count.setText(bRanks.get(i).getBorNum());
            return view;
        }

        private class MyViewHolder {
            public TextView rank_book_name;
            public TextView rank_count;
        }
    }


    public class Rank_Thread implements Runnable {
        private int index = 0;
        private String url = "";

        public Rank_Thread(int i) {
            index = i;
        }

        @Override
        public void run() {

            switch (index) {
                case 0:
                    url = HttpLinkHeader.Rank_COL;
                    break;
                case 1:
                    url = HttpLinkHeader.Rank_BOR;
                    break;
                case 2:
                    url = HttpLinkHeader.Rank_LOOK;
                    break;
            }
            try {
                String rank_result = OkHttpUtil.getStringFromServer(url);
                Log.d("rank_Result", rank_result.toString());
                Message msg = new Message();
                msg.what = 1;
                Bundle bundle = new Bundle();
                bundle.putString("rank_result", rank_result);
                bundle.putInt("rank_type", index);
                msg.setData(bundle);
                handler_rankInfo.sendMessage(msg);
            } catch (Exception e) {

                Log.d(TAG, "排行榜数据请求出错" + e.toString());
                Message message = Message.obtain();
                message.what = 2;
                handler_rankInfo.sendMessage(message);
            }
        }
    }

    private class Rank_Handler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 2) {
                Toast.makeText(getContext(), "网络超时", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
                Log.d(TAG, "已停止刷新");
            } else if (msg.what == 1) {
                String rank_info = msg.getData().getString("rank_result");
                int rank_type = msg.getData().getInt("rank_type");
                if (rank_info != null && !rank_info.equals("")) {
                    try {
                        boolean result = new JSONObject(rank_info).getBoolean("Result");
                        if (result) {
                            JSONArray jsonArray = new JSONObject(rank_info).getJSONArray("Detail");
                            switch (rank_type) {
                                case 0:
                                    book_col_ranks = new ArrayList<>();
                                    book_col_ranks=jsonArrayToList(jsonArray,book_col_ranks);
                                    setupListView(book_col_ranks);
                                    SaveFile.saveColRank(getContext(), rank_info);
                                    break;
                                case 1:
                                    book_bor_ranks = new ArrayList<>();
                                    book_bor_ranks=jsonArrayToList(jsonArray,book_bor_ranks);
                                    setupListView(book_bor_ranks);
                                    SaveFile.saveBorRank(getContext(), rank_info);
                                    break;
                                case 2:
                                    book_look_ranks = new ArrayList<>();
                                    book_look_ranks=jsonArrayToList(jsonArray,book_look_ranks);
                                    setupListView(book_look_ranks);
                                    SaveFile.saveLookRank(getContext(), rank_info);
                                    break;
                            }
                            Log.d(TAG,"从网络中加载数据");
                            //Toast.makeText(getContext(), "刷新成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "刷新失败", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        Log.d(TAG, e.toString());
                    }

                }
            }
        }
    }
    private  List<Book_Rank> jsonArrayToList(JSONArray jsonArray,List<Book_Rank> book_ranks) throws JSONException {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
            Book_Rank book_rank = new Book_Rank(
                    jsonObject.getInt("Rank"),
                    jsonObject.getString("Title"),
                    jsonObject.getString("BorNum"),
                    jsonObject.getString("ID")
            );
            book_ranks.add(book_rank);
        }
        return  book_ranks;
    }
}
    /**
     * 排行榜三个页面的的适配器
     */
    /*
    private class MyRankFrageStatePagerAdapter extends FragmentStatePagerAdapter {
        public MyRankFrageStatePagerAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            return  rank_fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return rank_fragmentList.size();
        }
    }
    */
    /**
     *  排行榜三个界面的滑动手势监听事件
     */
/*
    private class MyRankOnPageChangedListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            rank_currentTab = position;
            Log.d("G_homepage_slide", "success");
            Rank_Click(rank_currentTab);

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
*/

