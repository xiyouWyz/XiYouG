package com.example.wyz.xiyoug.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.example.wyz.xiyoug.Model.FourLevelModel;
import com.example.wyz.xiyoug.Model.HttpLinkHeader;
import com.example.wyz.xiyoug.R;
import com.example.wyz.xiyoug.Util.IsNetworkConnected;
import com.example.wyz.xiyoug.Util.JsonHandle;
import com.example.wyz.xiyoug.Util.MyAnimation;
import com.example.wyz.xiyoug.Util.OkHttpUtil;
import com.example.wyz.xiyoug.Util.ScheduleOkHttp;
import com.example.wyz.xiyoug.VolleyUtil.GetObjectRequest;
import com.example.wyz.xiyoug.VolleyUtil.ResponseListener;
import com.example.wyz.xiyoug.VolleyUtil.VolleyUtil;
import org.apache.http.message.BasicNameValuePair;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wyz on 2016/8/14.
 */
public class FourLevelActivity extends AppCompatActivity {
    private EditText account_view;
    private  EditText name_view;
    private Button login_view;
    private TextView name;
    private  TextView school;
    private  TextView type;
    private  TextView account;
    private  TextView time;
    private  TextView total_grade;
    private  TextView listen_grade;
    private  TextView read_grade;
    private  TextView write_grade;
    private  String id,password;
    private  String url;
    private RelativeLayout load_view;
    private LinearLayout login_content;
    private  LinearLayout main_content;
    private  MyThread myThread;
    private  MyHandler myHandler=new MyHandler();
    private  final String TAG=FourLevelActivity.this.toString();
    public   static   String SESSION_ID;
    private Toolbar toolbar;
    private  TextView label_view;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fourlevelscore);
        getData();
        setupViewComponent();
    }

    private void getData() {
        String result=getIntent().getStringExtra("result");
        label_view=(TextView)findViewById(R.id.label);
        label_view.setText(result);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupViewComponent() {
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        load_view=(RelativeLayout)findViewById(R.id.loading);
        main_content=(LinearLayout)findViewById(R.id.main_content);
        login_content=(LinearLayout)findViewById(R.id.login_content);
        load_view.setVisibility(View.INVISIBLE);
        main_content.setVisibility(View.INVISIBLE);
        login_content.setVisibility(View.VISIBLE);

        account_view=(EditText)findViewById(R.id.login_account);
        name_view=(EditText)findViewById(R.id.login_name);
        login_view=(Button)findViewById(R.id.login_btn);
        name=(TextView) findViewById(R.id.name);
        school=(TextView) findViewById(R.id.school);
        type=(TextView) findViewById(R.id.type);
        account=(TextView) findViewById(R.id.account);
        time=(TextView) findViewById(R.id.time);
        total_grade=(TextView) findViewById(R.id.total_grade);
        listen_grade=(TextView) findViewById(R.id.listen_grade);
        read_grade=(TextView) findViewById(R.id.read_grade);
        write_grade=(TextView) findViewById(R.id.write_grade);
        login_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!IsNetworkConnected.isNetworkConnected(FourLevelActivity.this))
                {
                    Message message=Message.obtain();
                    message.what=2;
                    myHandler.sendMessage(message);
                }
                else
                {
                    id=account_view.getText().toString();
                    password=name_view.getText().toString();

                    if(id.equals(""))
                    {
                        Toast.makeText(FourLevelActivity.this,"准考证号不能为空",Toast.LENGTH_SHORT).show();
                    }
                    else if(password.equals(""))
                    {
                        Toast.makeText(FourLevelActivity.this,"姓名不能为空",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        List<BasicNameValuePair> basicNameValuePairs=new ArrayList<>();
                        basicNameValuePairs.add(new BasicNameValuePair("zkzh",id));
                        basicNameValuePairs.add(new BasicNameValuePair("xm",password));
                        url= OkHttpUtil.attachHttpGetParams(HttpLinkHeader.FOURLEVEL,basicNameValuePairs);
                        new MyAnimation(FourLevelActivity.this,"胖萌为您努力登录中...",R.drawable.loading,load_view);
                        load_view.setVisibility(View.VISIBLE);
                        main_content.setVisibility(View.INVISIBLE);
                        login_content.setVisibility(View.INVISIBLE);

                        VolleyUtil.initialize(FourLevelActivity.this);
                        getObjectFromServer(url, new ResponseListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                load_view.setVisibility(View.INVISIBLE);
                                login_content.setVisibility(View.VISIBLE);
                                main_content.setVisibility(View.INVISIBLE);
                                Message message=Message.obtain();
                                message.what=3;
                                myHandler.sendMessage(message);
                            }

                            @Override
                            public void onResponse(Object o) {
                                myThread=new MyThread();
                                new  Thread(myThread).start();
                            }
                        });
                    }
                }

            }
        });
    }
    /**
     * @param url 请求地址
     * @param listener 回调接口，包含错误回调和正确的数据回调
     */
    public static void getObjectFromServer(String url,ResponseListener listener){

        Request request = new GetObjectRequest(url,listener) ;
        VolleyUtil.getInstance().add(request);
    }



    private class MyThread  implements Runnable{
        @Override
        public void run() {
            try {
                String result= ScheduleOkHttp.getFourLevelScore(url,SESSION_ID);
                Message message =Message.obtain();
                message.what=1;
                Bundle bundle=new Bundle();
                bundle.putString("result",result);
                message.setData(bundle);
                myHandler.sendMessage(message);
            } catch (Exception e) {
                Log.d(TAG,e.toString());
                Message message=Message.obtain();
                message.what=2;
                myHandler.sendMessage(message);
            }
        }
    }
    private class MyHandler  extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what)
            {
                case 1:
                    FourLevelModel fourLevelModel= JsonHandle.getFourLevelScore(msg.getData().getString("result"));
                    name.setText(fourLevelModel.name);
                    school.setText(fourLevelModel.school);
                    type.setText(fourLevelModel.type);
                    account.setText(fourLevelModel.account);
                    time.setText(fourLevelModel.time);
                    total_grade.setText(fourLevelModel.total_grade);
                    String listen="听力 :"+fourLevelModel.listen_grade;
                    String read="阅读 :"+fourLevelModel.read_grade;
                    String write="写作与翻译 :"+fourLevelModel.write_grade;
                    listen_grade.setText(listen);
                    read_grade.setText(read);
                    write_grade.setText(write);
                    load_view.setVisibility(View.INVISIBLE);
                    login_content.setVisibility(View.INVISIBLE);
                    main_content.setVisibility(View.VISIBLE);
                    break;
                case  2:
                    Toast.makeText(FourLevelActivity.this,"网络超时",Toast.LENGTH_SHORT).show();
                    break;
                case  3:
                    Toast.makeText(FourLevelActivity.this,"请求出错",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }



}
