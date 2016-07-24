package com.example.wyz.xiyoug.View;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wyz.xiyoug.Model.HttpLinkHeader;
import com.example.wyz.xiyoug.Model.User;
import com.example.wyz.xiyoug.MyBorrowActivity;
import com.example.wyz.xiyoug.MyCollectionActivity;
import com.example.wyz.xiyoug.MyHistoryBorActivity;
import com.example.wyz.xiyoug.R;
import com.example.wyz.xiyoug.Util.OkHttpUtil;
import com.squareup.okhttp.OkHttpClient;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wyz on 2016/7/17.
 */
public class MyFragment extends Fragment {
    private  View view;
    private  TextView major_view;
    private  TextView name_view;
    private  TextView  studyNumber_view;
    private  LinearLayout my_login;
    private  LinearLayout my_bor;
    private  LinearLayout my_col;
    private  LinearLayout my_history;
    private  final  String TAG="MyFragment";
    private  LoginWindow loginWindow;
    public   static  String SESSIONID;
    private  String login_url;
    private  MyThread myThread;
    private  MyHandler myHandler=new MyHandler();

    private  String studyNumber="点击登录",major="",name="",debt="";
    private  boolean isLogin=false;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.my_page,container,false);

        setupViewComponent();
        return  view;
    }

    /*@Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG,"注册了一个广播");
         broadcastManager=LocalBroadcastManager.getInstance(getActivity());
        //注册广播
        intentFilter=new IntentFilter();
        intentFilter.addAction("android.GET_USER_INFO");
        mReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals("android.GET_USER_INFO"))
                {
                    String id=intent.getStringExtra("ID");
                    String name=intent.getStringExtra("Name");
                    String department=intent.getStringExtra("Department");
                    String debt=intent.getStringExtra("Debt");
                    studyNumber_view.setText(id);
                    name_view.setText(name);
                    major_view.setText(department);

                }
            }
        };

        broadcastManager.registerReceiver(mReceiver,intentFilter);
    }*/
    private void setupViewComponent() {
        major_view=(TextView)view.findViewById(R.id.major);
        name_view=(TextView) view.findViewById(R.id.name);
        studyNumber_view=(TextView) view.findViewById(R.id.studyNumber);
        if(!studyNumber.equals(""))
        {
            studyNumber_view.setText(studyNumber);
        }
        if(!major.equals(""))
        {
            major_view.setText(major);
        }
        if(!name.equals(""))
        {
            name_view.setText(name);
        }
        my_bor=(LinearLayout)view.findViewById(R.id.my_bor);
        my_col=(LinearLayout)view.findViewById(R.id.my_col);
        my_history=(LinearLayout)view.findViewById(R.id.my_history);
        my_bor.setOnClickListener(new MyOnClickListener());
        my_col.setOnClickListener(new MyOnClickListener());
        my_history.setOnClickListener(new MyOnClickListener());
        my_login=(LinearLayout)view.findViewById(R.id.login);
        my_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isLogin==false)
                {
                    loginWindow=new LoginWindow(getContext(),new MyLoginOnClickListener());
                    Log.d(TAG,"loginWindows建立完毕");
                    loginWindow.showAtLocation(view,Gravity.CENTER,0,0);
                }
            }
        });
    }

    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Log.d(TAG,String.valueOf(view.getId()));
            switch (view.getId())
            {
                case  R.id.my_bor:
                    if(isLogin==true)
                    {
                        Intent intent=new Intent();
                        intent.setClass(getContext(), MyBorrowActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(getContext(),"请先登录",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case  R.id.my_col:
                    if(isLogin==true)
                    {
                        Intent intent=new Intent();
                        intent.setClass(getContext(), MyCollectionActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(getContext(),"请先登录",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case  R.id.my_history:
                    if(isLogin==true)
                    {
                        Intent intent=new Intent();
                        intent.setClass(getContext(), MyHistoryBorActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(getContext(),"请先登录",Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }

    private class MyLoginOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId())
            {
                case R.id.login_btn:
                    String account=loginWindow.getAccount();
                    String password=loginWindow.getPassword();
                    if(account.equals(""))
                    {
                        Toast.makeText(getContext(),"用户名不能为空",Toast.LENGTH_SHORT).show();
                    }
                    else if(password.equals(""))
                    {
                        Toast.makeText(getContext(),"密码不能为空",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        List<BasicNameValuePair> basicNameValuePairs=new ArrayList<>();
                        basicNameValuePairs.add(new BasicNameValuePair("username",account));
                        basicNameValuePairs.add(new BasicNameValuePair("password",password));
                        login_url= OkHttpUtil.attachHttpGetParams(HttpLinkHeader.LOGIN,basicNameValuePairs);
                        myThread=new MyThread();
                        new Thread(myThread).start();
                    }
                    break;
                case R.id.close:
                    loginWindow.dismiss();
                    break;
            }
        }
    }
    private class   MyThread implements Runnable
    {
        @Override
        public void run() {
            try {
                String login_result= OkHttpUtil.getStringFromServer(login_url);
                boolean isSuccess=new JSONObject(login_result).getBoolean("Result");
                if(isSuccess==true)
                {
                    SESSIONID=new JSONObject(login_result).getString("Detail");

                    String user_url=OkHttpUtil.attachHttpGetParam(HttpLinkHeader.USER_INFO,"session",SESSIONID);
                    String userInfo_result=OkHttpUtil.getStringFromServer(user_url);
                    boolean isGetInfoSuccess=new JSONObject(userInfo_result).getBoolean("Result");
                    if(isGetInfoSuccess==true)
                    {
                        JSONObject jsonObject=new JSONObject(userInfo_result).getJSONObject("Detail");
                        String id=jsonObject.getString("ID");
                        String name=jsonObject.getString("Name");
                        String department=jsonObject.getString("Department");
                        String debt=jsonObject.getString("Debt");
                        Message message=new Message();
                        Bundle bundle=new Bundle();
                        bundle.putString("ID",id);
                        bundle.putString("Name",name);
                        bundle.putString("Department",department);
                        bundle.putString("Debt",debt);
                        message.setData(bundle);
                        myHandler.sendMessage(message);
                    }
                    else
                    {
                        Toast.makeText(getContext(),"获取个人信息失败",Toast.LENGTH_SHORT).show();
                    }

                }
                else
                {
                    Toast.makeText(getContext(),"账号错误，密码错误或账户不存在",Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class MyHandler  extends Handler{
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle=msg.getData();
            studyNumber=bundle.getString("ID");
            name=bundle.getString("Name");
            major=bundle.getString("Department");
            debt=bundle.getString("Debt");
            studyNumber_view.setText(studyNumber);
            name_view.setText(name);
            major_view.setText(major);

            User.getInstance(studyNumber,name,major,debt);
            isLogin=true;
            loginWindow.dismiss();


        }
    }
}
