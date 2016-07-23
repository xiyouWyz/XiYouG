package com.example.wyz.xiyoug;

import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wyz.xiyoug.Model.News;
import com.example.wyz.xiyoug.Util.OkHttpUtil;
import com.example.wyz.xiyoug.View.InfoFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Wyz on 2016/7/22.
 */
public class InfoDetail_Activity extends AppCompatActivity{

    private Toolbar toolbar;
    private TextView title_view;
    private  TextView publisher_view;
    private  TextView date_view;
    private ImageView backView;
    private WebView webView;
    private  MyThread myThread;
    private  MyHandler myHandler=new MyHandler();
    String url;

    private  final  String TAG="infoDetail_Activity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_details_page);
        setupViewComponent();
        getData();
    }

    private void getData() {
        Bundle bundle=getIntent().getExtras();
        String type= bundle.getString("type");
        String format=bundle.getString("format");
        int id=bundle.getInt("id");
        url="https://api.xiyoumobile.com/xiyoulibv2/news/getDetail/"+type+"/"+format+"/"+id;
        myThread=new MyThread();
        new Thread(myThread).start();
    }

    private void setupViewComponent() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //backView=(ImageView)findViewById(R.id.back);
        title_view=(TextView) findViewById(R.id.title);
        publisher_view=(TextView) findViewById(R.id.publisher);
        date_view=(TextView) findViewById(R.id.date);
        webView=(WebView) findViewById(R.id.webView);
       /* backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               InfoDetail_Activity.this.finish();
            }
        });*/
    }
    public   class  MyThread implements  Runnable
    {
        @Override
        public void run() {
            try {
                String info_result= OkHttpUtil.getStringFromServer(url);
                Message message=new Message();
                Bundle bundle=new Bundle();
                bundle.putString("info_result",info_result);
                message.setData(bundle);
                myHandler.sendMessage(message);
            } catch (IOException e) {
                Log.d("False","新闻数据请求出错");
            }

        }
    }
    private  class  MyHandler extends Handler
    {
        private  String info_detail;
        @Override
        public void handleMessage(Message msg) {
            info_detail = msg.getData().getString("info_result");
            if(!info_detail.equals(""))
            {
                try {
                    boolean result=new JSONObject(info_detail).getBoolean("Result");
                    if(result==true)
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
                    }
                    else
                    {
                        Toast.makeText(InfoDetail_Activity.this,"获取失败",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
}
