package com.example.wyz.xiyoug.View;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleCursorTreeAdapter;
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
    private  TextView debtLabel_view;
    private  TextView debt_view;
    private  LinearLayout my_login;
    private  LinearLayout my_bor;
    private  LinearLayout my_col;
    private  LinearLayout my_history;
    private  LinearLayout my_exit;
    private  final  String TAG="MyFragment";
    private  LoginWindow loginWindow;

    private  String login_url;
    private  MyThread myThread;
    private  MyHandler myHandler=new MyHandler();

    private  String studyNumber="点击登录",major="",name="",debt="";
    public   static  String SESSIONID;
    public   static boolean isLogin=false;

    private  String account="",password="";
    private boolean isRemember;
    private  SharedPreferences pref;
    private  SharedPreferences.Editor editor;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.my_page,container,false);
        Log.d(TAG,"MyFragment碎片正在加载");
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
        debtLabel_view=(TextView)view.findViewById(R.id.debtLabel);
        debt_view=(TextView)view.findViewById(R.id.debt);


        studyNumber_view.setText(studyNumber);
        major_view.setText(major);
        name_view.setText(name);
        debt_view.setText(debt);

        my_bor=(LinearLayout)view.findViewById(R.id.my_bor);
        my_col=(LinearLayout)view.findViewById(R.id.my_col);
        my_history=(LinearLayout)view.findViewById(R.id.my_history);
        my_exit=(LinearLayout)view.findViewById(R.id.my_exit);
        my_exit.setOnClickListener(new MyOnClickListener());
        my_bor.setOnClickListener(new MyOnClickListener());
        my_col.setOnClickListener(new MyOnClickListener());
        my_history.setOnClickListener(new MyOnClickListener());
        my_login=(LinearLayout)view.findViewById(R.id.login);
        my_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isLogin)
                {
                    pref= PreferenceManager.getDefaultSharedPreferences(getContext());
                    boolean remember=pref.getBoolean("isRemember",false);
                    String account=pref.getString("account","");
                    String password=pref.getString("password","");
                    loginWindow=new LoginWindow(getContext(),new MyLoginOnClickListener(),account,password,remember);
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
                    if(isLogin)
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
                    if(isLogin)
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
                    if(isLogin)
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
                case R.id.my_exit:
                    if(isLogin)
                    {
                        isLogin=false;
                        if(!User.getInstance().getId().equals(""))
                        {
                            User.Clear();
                        }
                        studyNumber_view.setText("点击登录");
                        name_view.setText("");
                        major_view.setText("");
                        debtLabel_view.setText("");
                        debt_view.setText("");
                        studyNumber="点击登录";
                        major="";
                        name="";
                        debt="";
                        SESSIONID="";

                    }
                    else
                    {
                        Toast.makeText(getContext(),"请先登录",Toast.LENGTH_SHORT).show();
                    }
            }
        }
    }

    private class MyLoginOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId())
            {
                case R.id.login_btn:
                    account=loginWindow.getAccount();
                    password=loginWindow.getPassword();
                    isRemember=loginWindow.getRemember();
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
                if(isSuccess)
                {
                    SESSIONID=new JSONObject(login_result).getString("Detail");

                    String user_url=OkHttpUtil.attachHttpGetParam(HttpLinkHeader.USER_INFO,"session",SESSIONID);
                    String userInfo_result=OkHttpUtil.getStringFromServer(user_url);
                    boolean isGetInfoSuccess=new JSONObject(userInfo_result).getBoolean("Result");
                    if(isGetInfoSuccess)
                    {
                        JSONObject jsonObject=new JSONObject(userInfo_result).getJSONObject("Detail");
                        String id=jsonObject.getString("ID");
                        String name=jsonObject.getString("Name");
                        String department=jsonObject.getString("Department");
                        String debt=jsonObject.getString("Debt");
                        Message message=Message.obtain();
                        message.what=1;
                        Bundle bundle=new Bundle();
                        bundle.putString("ID",id);
                        bundle.putString("Name",name);
                        bundle.putString("Department",department);
                        bundle.putString("Debt",debt);
                        message.setData(bundle);
                        myHandler.sendMessage(message);
                    }
                }
                else
                {
                    Message message=Message.obtain();
                    message.what=0;
                    myHandler.sendMessage(message);

                }
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
            if(msg.what==0)
            {
                Toast.makeText(getContext(),"账号错误，密码错误或账户不存在",Toast.LENGTH_SHORT).show();
            }
            else if(msg.what==2)
            {
                Toast.makeText(getContext(),"请检查网络连接",Toast.LENGTH_SHORT).show();
            }
            else if(msg.what==1)
            {
                Bundle bundle=msg.getData();
                studyNumber=bundle.getString("ID");
                name=bundle.getString("Name");
                major=bundle.getString("Department");
                debt=bundle.getString("Debt");

                studyNumber_view.setText(studyNumber);
                name_view.setText(name);
                major_view.setText(major);
                debtLabel_view.setText("欠费情况");
                debt_view.setText(debt);

                editor=pref.edit();
                editor.putString("account",account);
                editor.putString("password",password);
                editor.putBoolean("isRemember",isRemember);

                editor.commit();

                User.getInstance(studyNumber,name,major,debt);
                isLogin=true;
                loginWindow.dismiss();
            }




        }
    }
}
