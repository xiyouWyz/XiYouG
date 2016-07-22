package com.example.wyz.xiyoug.View;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wyz.xiyoug.Model.News;
import com.example.wyz.xiyoug.R;
import com.example.wyz.xiyoug.Util.OkHttpUtil;
import com.example.wyz.xiyoug.pulltorefreshlistview.PullListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wyz on 2016/7/22.
 */
public class NewsFragment  extends Fragment{

    private  View view;
    private PullListView pullListView;

    //下拉刷新处理
    private Handler handler_pull=new Handler();
    //上拉加载处理
    private  Handler handler_up=new Handler();
    //新闻listview的适配器
    private MyAdapter adapter;
    //新闻信息存储集合
    private List<News> newses=new ArrayList<>();
    //获取新闻的线程
    private News_Thread news_thread;
    //获取新闻信息后对listview界面的更新
    private News_Handler news_handler;

    private  int news_page=1;

    private int total_pages;

    private final   String TAG="NewsFragment";
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.info_news_page,container,false);
        news_handler=new News_Handler();
        initData();
        setupViewComponent();
        return view;
    }
    private  void initData()
    {
        news_thread=new News_Thread();
        new Thread(news_thread).start();
    }
    private void setupViewComponent() {
        pullListView=(PullListView)view.findViewById(R.id.pullListView);

        pullListView.setOnRefreshListener(new PullListView.OnRefreshListener() {

            @Override
            public void onRefresh() {
                handler_pull.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getPullDownData();
                    }
                },2000);
            }
        });

        pullListView.setOnGetMoreListener(new PullListView.OnGetMoreListener() {

            @Override
            public void onGetMore() {

                handler_up.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getUpLoadData();
                    }
                },2000);
            }
        });
    }
    private  void setupListView( )
    {
        adapter=new MyAdapter();
        pullListView.setAdapter(adapter);
    }
    private void getUpData(boolean isRefresh) {

        initData();
        pullListView.setAdapter(new MyAdapter());
        Toast.makeText(getContext(),"刷新成功",Toast.LENGTH_SHORT).show();
        adapter.notifyDataSetChanged();
        pullListView.refreshComplete();
        pullListView.getMoreComplete();
        if(!isRefresh)
        {
            int count=newses.size();
            int trader=count/20;
            int over=count%20;
            if(trader<total_pages)
            {
                if(over==0)
                {
                    pullListView.setSelection(trader*20+1);
                    Log.d(TAG,"滑动到"+String.valueOf(trader*20+1));
                }
                else
                {
                    pullListView.setSelection(trader*20+over);
                }
            }
        }


    }
    private  void getUpLoadData()
    {
        news_page += 1;
        getUpData(false);

    }
    private void getPullDownData() {
        news_page = 1;
        getUpData(true);


    }
    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return newses.size();
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
            MyViewHolder myViewHolder=null;
            if(view==null)
            {
                myViewHolder=new MyViewHolder();
                view= LayoutInflater.from(getContext()).inflate(R.layout.info_pull_listview_item,null);
                myViewHolder.news_view=(TextView)view.findViewById(R.id.news);
                myViewHolder.date_view=(TextView)view.findViewById(R.id.date);
                view.setTag(myViewHolder);
            }
            else
            {
                myViewHolder=(MyViewHolder) view.getTag();
            }
            myViewHolder.news_view.setText(newses.get(i).getTitle());
            myViewHolder.date_view.setText(newses.get(i).getDate());
            return  view;
        }
        private  class  MyViewHolder
        {
            public  TextView news_view;
            public  TextView date_view;
        }
    }


    public   class  News_Thread implements  Runnable
    {

        private  String url="";
        @Override
        public void run() {
            url="http://api.xiyoumobile.com/xiyoulibv2/news/getList/news/"+news_page;
            try {
                String info_result= OkHttpUtil.getStringFromServer(url);
                Log.d("info_result",info_result);
                Message message=new Message();
                Bundle bundle=new Bundle();
                bundle.putString("info_result",info_result);
                message.setData(bundle);
                news_handler.sendMessage(message);
            } catch (IOException e) {
                Log.d("False","新闻数据请求出错");
            }

        }
    }
    private  class  News_Handler extends Handler
    {
        @Override
        public void handleMessage(Message msg) {
            String news_info=msg.getData().getString("info_result");
            if(!news_info.equals(""))
            {
                try {
                    boolean result=new JSONObject(news_info).getBoolean("Result");
                    if(result==true)
                    {

                        JSONObject detail=(JSONObject) new JSONObject(news_info).get("Detail");
                        String type=detail.getString("Type");
                        total_pages=detail.getInt("Pages");
                        JSONArray jsonArray= detail.getJSONArray("Data");
                        if(type.equals("新闻"))
                        {
                            //下拉刷新，否则上拉加载
                            if(news_page==1)
                            {
                                newses=new ArrayList<>();
                            }
                            for(int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject=(JSONObject)jsonArray.get(i);
                                News news=new News(
                                        jsonObject.getInt("ID"),
                                        jsonObject.getString("Title"),
                                        jsonObject.getString("Date")
                                );
                                newses.add(news);
                            }
                            setupListView();
                        }
                    }
                    else
                    {
                        Toast.makeText(getContext(),"获取失败",Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
