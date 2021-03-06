package com.example.wyz.xiyoug.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wyz.xiyoug.Model.HttpLinkHeader;
import com.example.wyz.xiyoug.R;
import com.example.wyz.xiyoug.Util.IsNetworkConnected;
import com.example.wyz.xiyoug.Util.MyAnimation;
import com.example.wyz.xiyoug.Util.OkHttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Wyz on 2016/7/22.
 */
public class InfoDetail_Activity extends AppCompatActivity{

    private Toolbar toolbar;
    private TextView title_view;
    private  TextView publisher_view;
    private  TextView date_view;
    private  TextView pubLabel_view;
    private  TextView pubDateLabel_view;
    private ImageView backView;
    private WebView webView;
    private FrameLayout mFrameLayout;
    private  MyThread myThread;
    private  MyHandler myHandler=new MyHandler();
    String url;

    private  final  String TAG="infoDetail_Activity";

    private RelativeLayout view;
    private ScrollView content;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_details_page);
        setupViewComponent();
        getData();
        System.out.println(android.os.HandlerThread.currentThread().getName());
        System.out.println(android.os.HandlerThread.currentThread().getId());
        System.out.println(android.os.Process.myPid());
    }

    private void getData() {
        Bundle bundle=getIntent().getExtras();
        String type= bundle.getString("type");
        String format=bundle.getString("format");
        int id=bundle.getInt("id");
        if(type!=null&&type.equals("news"))
        {
            url= HttpLinkHeader.NEWS_DETAIL+id;
        }
        else if(type!=null&&type.equals("announce"))
        {
            url= HttpLinkHeader.NOTICES_DETAIL+id;
        }
        if(!IsNetworkConnected.isNetworkConnected(InfoDetail_Activity.this))
        {
            Message message=Message.obtain();
            message.what=3;
            myHandler.sendMessage(message);
        }
        else
        {
            myThread=new MyThread();
            new Thread(myThread).start();
        }

    }

    private void setupViewComponent() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        title_view=(TextView) findViewById(R.id.title);
        publisher_view=(TextView) findViewById(R.id.publisher);
        date_view=(TextView) findViewById(R.id.date);
        mFrameLayout=(FrameLayout)findViewById(R.id.frameLayout);
        webView=new WebView(getApplicationContext());
        mFrameLayout.addView(webView);
        //webView=(WebView) findViewById(R.id.webView);
        pubLabel_view=(TextView)findViewById(R.id.pubLabel);
        pubDateLabel_view=(TextView)findViewById(R.id.pubDateLabel);
        view=(RelativeLayout) findViewById(R.id.loading);
        new MyAnimation(InfoDetail_Activity.this, "胖萌正在为您努力加载....", R.drawable.loading, view);
        content=(ScrollView)findViewById(R.id.content);
    }
    private    class  MyThread implements  Runnable
    {
        @Override
        public void run() {
            try {
                String info_result= OkHttpUtil.getStringFromServer(url);
                Message message=new Message();
                Bundle bundle=new Bundle();
                bundle.putString("info_result",info_result);
                message.setData(bundle);
                message.what=1;
                myHandler.sendMessage(message);
            } catch (Exception e) {
                Log.d(TAG,e.toString());
                Message message=Message.obtain();
                message.what=3;
                myHandler.sendMessage(message);
            }
        }
    }
    private  class  MyHandler extends Handler
    {

        @Override
        public void handleMessage(Message msg) {
            if(msg.what==1)
            {
                String info_detail = msg.getData().getString("info_result");
                DealWithInfo(info_detail);

            }
            if(msg.what==2)
            {

                view.setVisibility(View.INVISIBLE);
                content.setVisibility(View.VISIBLE);
            }
            else if(msg.what==3)
            {
                Toast.makeText(InfoDetail_Activity.this,"网络超时",Toast.LENGTH_SHORT).show();
                InfoDetail_Activity.this.finish();
            }
            else if(msg.what==4)
            {
                Toast.makeText(InfoDetail_Activity.this,"请求出错",Toast.LENGTH_SHORT).show();
                InfoDetail_Activity.this.finish();
            }


        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private  void DealWithInfo(String info_detail)
    {

        if(!info_detail.equals(""))
        {
            try {
                boolean result=new JSONObject(info_detail).getBoolean("Result");
                if(result)
                {
                    JSONObject jsonObject=new JSONObject(info_detail).getJSONObject("Detail");
                    String title=(String)jsonObject.get("Title");
                    String publisher=(String)jsonObject.get("Publisher");
                    String date=(String)jsonObject.get("Date");
                    String web=(String)jsonObject.get("Passage");

                    title_view.setText(title);
                    publisher_view.setText(publisher);
                    date_view.setText(date);
                    webView.loadData(web,"text/html","utf-8");
                    pubLabel_view.setText("发布单位");
                    pubDateLabel_view.setText("发布时间");
                    Message message=new Message();
                    message.what=2;
                    myHandler.sendMessage(message);

                }
                else
                {
                    Message message=Message.obtain();
                    message.what=4;
                    myHandler.sendMessage(message);
                }
            } catch (JSONException e) {
                Message message=Message.obtain();
                message.what=4;
                myHandler.sendMessage(message);
            }
        }
    }

    @Override
    protected void onDestroy() {
        deStoryWebView();
        //android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
        super.onDestroy();

    }

    private  void deStoryWebView(){
        if(webView!=null){
            webView.pauseTimers();
            webView.removeAllViews();
            webView.destroy();
            webView=null;
        }
    }
}
