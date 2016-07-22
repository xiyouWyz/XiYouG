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
public class NoticeFragment  extends Fragment{

    private  View view;
    private PullListView pullListView;

    //下拉刷新处理
    private Handler handler_pull=new Handler();
    //上拉加载处理
    private  Handler handler_up=new Handler();
    //新闻listview的适配器
    private MyAdapter adapter;
    //新闻信息存储集合
    private List<News> notices=new ArrayList<>();
    //获取新闻的线程
    private Notice_Thread notice_thread;
    //获取新闻信息后对listview界面的更新
    private Notice_Handler notice_handler;

    private  int notice_page=1;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.info_notice_page,container,false);
        notice_handler=new Notice_Handler();
        initData();
        setupViewComponent();
        return view;
    }
    private  void initData()
    {
        notice_thread=new Notice_Thread();
        new Thread(notice_thread).start();
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
    private void getUpData() {

        initData();
        pullListView.setAdapter(new MyAdapter());
        Toast.makeText(getContext(),"刷新成功",Toast.LENGTH_SHORT).show();
        adapter.notifyDataSetChanged();
        pullListView.refreshComplete();
        pullListView.getMoreComplete();
    }
    private  void getUpLoadData()
    {
        notice_page += 1;
        getUpData();

    }
    private void getPullDownData() {
        notice_page = 1;
        getUpData();

    }
    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return notices.size();
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
            myViewHolder.news_view.setText(notices.get(i).getTitle());
            myViewHolder.date_view.setText(notices.get(i).getDate());
            return  view;
        }
        private  class  MyViewHolder
        {
            public  TextView news_view;
            public  TextView date_view;
        }
    }


    public   class  Notice_Thread implements  Runnable
    {

        private  String url="";
        @Override
        public void run() {
            url="http://api.xiyoumobile.com/xiyoulibv2/news/getList/announce/"+notice_page;
            try {
                String info_result= OkHttpUtil.getStringFromServer(url);
                Log.d("info_result",info_result);
                Message message=new Message();
                Bundle bundle=new Bundle();
                bundle.putString("info_result",info_result);
                message.setData(bundle);
                notice_handler.sendMessage(message);
            } catch (IOException e) {
                Log.d("False","新闻数据请求出错");
            }

        }
    }
    private  class  Notice_Handler extends Handler
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
                        JSONArray jsonArray= detail.getJSONArray("Data");
                        if(type.equals("公告"))
                        {
                            //下拉刷新，否则上拉加载
                            if(notice_page==1)
                            {
                                notices=new ArrayList<>();
                            }
                            for(int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject=(JSONObject)jsonArray.get(i);
                                News news=new News(
                                        jsonObject.getInt("ID"),
                                        jsonObject.getString("Title"),
                                        jsonObject.getString("Date")
                                );
                                notices.add(news);
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
