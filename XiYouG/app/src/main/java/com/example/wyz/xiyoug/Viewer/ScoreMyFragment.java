package com.example.wyz.xiyoug.Viewer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wyz.xiyoug.Activity.ScoreActivity;
import com.example.wyz.xiyoug.Activity.ScoreFailedActivity;
import com.example.wyz.xiyoug.Model.HttpLinkHeader;
import com.example.wyz.xiyoug.Model.ScoreFailedModel;
import com.example.wyz.xiyoug.Model.ScoreUser;
import com.example.wyz.xiyoug.R;
import com.example.wyz.xiyoug.Util.IsNetworkConnected;
import com.example.wyz.xiyoug.Util.JsonHandle;
import com.example.wyz.xiyoug.Util.MyAnimation;
import com.example.wyz.xiyoug.Util.OkHttpUtil;
import com.example.wyz.xiyoug.Util.ScheduleOkHttp;
import com.example.wyz.xiyoug.Util.SerializableList;

import org.apache.http.message.BasicNameValuePair;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wyz on 2016/8/4.
 */
public class ScoreMyFragment extends Fragment implements View.OnClickListener{
    private  View view;
    private  LinearLayout alreadyLogin_view;
    private  LinearLayout notLogin_view;
    private  LinearLayout login_view;
    private TextView name_view;
    private  TextView studyNumber_view;
    private  TextView college_view;
    private  TextView department_view;
    private LinearLayout score_query;
    private  LinearLayout level_query;
    private  LinearLayout faied_query;
    private  LinearLayout exit;
    private  boolean isLogin=false;
    private SharedPreferences pref;
    private  SharedPreferences.Editor editor;
    private  String account="",password="";
    private boolean isRemember;
    private  LoginWindow loginWindow;
    private RelativeLayout load_view;
    private LinearLayout content;
    private  MyThread myThread;
    private  MyHandler myHandler=new MyHandler();
    private  String sessionId;
    private  String score_url;
    private  String score_html;
    private  String name;
    private  List<String> score_userInfo;



    private  final String TAG="ScoreMyFragment";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.score_my,container,false);
        Log.d(TAG,"加载ScoreMyFragment");
        setupViewComponent();
        return view;
    }

    private void setupViewComponent() {
        alreadyLogin_view=(LinearLayout)view.findViewById(R.id.alreadyLogin);
        notLogin_view=(LinearLayout)view.findViewById(R.id.notLogin);
        login_view=(LinearLayout)view.findViewById(R.id.login);
        name_view=(TextView)view.findViewById(R.id.name);
        studyNumber_view=(TextView)view.findViewById(R.id.studyNumber);
        college_view=(TextView)view.findViewById(R.id.college);
        department_view=(TextView)view.findViewById(R.id.department);
        score_query=(LinearLayout)view.findViewById(R.id.score_query);
        level_query=(LinearLayout)view.findViewById(R.id.level_query);
        faied_query=(LinearLayout)view.findViewById(R.id.failed_query);
        exit=(LinearLayout)view.findViewById(R.id.exit);
        load_view=(RelativeLayout)view.findViewById(R.id.loading);
        content=(LinearLayout)view.findViewById(R.id.content);
        load_view.setVisibility(View.INVISIBLE);

        login_view.setOnClickListener(this);
        score_query.setOnClickListener(this);
        level_query.setOnClickListener(this);
        faied_query.setOnClickListener(this);
        exit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.score_query:
                if(!IsNetworkConnected.isNetworkConnected(getContext()))
                {
                    Toast.makeText(getContext(),"网络超时",Toast.LENGTH_SHORT).show();
                }
                else if(!isLogin)
                {
                    Toast.makeText(getContext(),"请先登录",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent intent=new Intent();
                    Bundle bundle=new Bundle();
                    bundle.putString("score_html",score_html);
                    bundle.putString("score_url",score_url);
                    bundle.putString("sessionId",sessionId);
                    intent.putExtras(bundle);
                    intent.setClass(getContext(), ScoreActivity.class);
                    startActivity(intent);
                }
                break;
            case  R.id.level_query:
                if(!IsNetworkConnected.isNetworkConnected(getContext()))
                {
                    Toast.makeText(getContext(),"网络超时",Toast.LENGTH_SHORT).show();
                }
                else if(!isLogin)
                {
                    Toast.makeText(getContext(),"请先登录",Toast.LENGTH_SHORT).show();
                }
                else
                {

                }
                break;
            case  R.id.failed_query:
                if(!IsNetworkConnected.isNetworkConnected(getContext()))
                {
                    Toast.makeText(getContext(),"网络超时",Toast.LENGTH_SHORT).show();
                }
                else if(!isLogin)
                {
                    Toast.makeText(getContext(),"请先登录",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent intent=new Intent();
                    Bundle bundle=new Bundle();
                    bundle.putString("score_html",score_html);
                    intent.putExtras(bundle);
                    intent.setClass(getContext(), ScoreFailedActivity.class);
                    startActivity(intent);
                }
                break;
            case  R.id.exit:
                if(!isLogin)
                {
                    Toast.makeText(getContext(),"未登录",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    isLogin=false;
                    if(!ScoreUser.getInstance().getId().equals(""))
                    {
                        ScoreUser.Clear();
                    }
                    alreadyLogin_view.setVisibility(View.INVISIBLE);
                    notLogin_view.setVisibility(View.VISIBLE);
                    studyNumber_view.setText("");
                    name_view.setText("");
                    college_view.setText("");
                    department_view.setText("");
                    //loginWindow.showAtLocation(view,Gravity.CENTER,0,0);

                }
                break;
            case  R.id.login:

                    if(!IsNetworkConnected.isNetworkConnected(getContext()))
                    {
                        Toast.makeText(getContext(),"网络超时",Toast.LENGTH_SHORT).show();
                    }
                    else if(!isLogin)
                    {
                            pref=getContext().getSharedPreferences("score_info", Context.MODE_PRIVATE);
                            isRemember=pref.getBoolean("isRemember",false);
                            account=pref.getString("account","");
                            password=pref.getString("password","");
                            loginWindow=new LoginWindow(getContext(),new MyLoginOnClickListener(),account,password,isRemember,1);
                            loginWindow.showAtLocation(view, Gravity.CENTER,0,0);
                    }
                break;
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
                        new MyAnimation(getContext(),"胖萌正在努力为您加载...",R.drawable.loading,load_view);
                        loginWindow.dismiss();
                        load_view.setVisibility(View.VISIBLE);
                        content.setVisibility(View.INVISIBLE);
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
                if(ScheduleFragment.sessionId.equals(""))
                {
                    sessionId= ScheduleOkHttp.postGetSessionFromServer(HttpLinkHeader.XIYOU_Login,account,password);
                }
                else
                {
                    sessionId=ScheduleFragment.sessionId;
                }

                String main_url= OkHttpUtil.attachHttpGetParam(HttpLinkHeader.XIYOU_Main,"xh",account);
                String info=ScheduleOkHttp.getGetInfoFromServer(main_url,sessionId);
                name= JsonHandle.getName(info);
                Message message=Message.obtain();
                message.what=1;
                myHandler.sendMessage(message);

                List<BasicNameValuePair> basicNameValuePairs=new ArrayList<>();
                basicNameValuePairs.add(new BasicNameValuePair("xh",account));
                basicNameValuePairs.add(new BasicNameValuePair("xm",name));
                basicNameValuePairs.add(new BasicNameValuePair("gnmkdm","N121605"));
                score_url=OkHttpUtil.attachHttpGetParams(HttpLinkHeader.SCORE,basicNameValuePairs);
                try {
                    score_html = ScheduleOkHttp.getGetScore(score_url,sessionId);
                    score_userInfo=JsonHandle.getScoreUserInfo(score_html);
                    Log.d(TAG,score_userInfo.toString());

                    Message message1=Message.obtain();
                    Bundle bundle=new Bundle();
                    SerializableList serializableList=new SerializableList();
                    serializableList.setStringList(score_userInfo);
                    bundle.putSerializable("userinfo",serializableList);
                    message1.setData(bundle);
                    message1.what=3;
                    myHandler.sendMessage(message1);
                } catch (Exception e) {
                    Log.d(TAG,e.toString());
                    Message message1=Message.obtain();
                    message1.what=2;
                    myHandler.sendMessage(message1);
                }
            }
            catch (ConnectException e)
            {
                Log.d(TAG,e.toString());
                Message message1=Message.obtain();
                message1.what=2;
                myHandler.sendMessage(message1);
            }
            catch (IllegalArgumentException e)
            {
                Log.d(TAG,e.toString());
                Message message1=Message.obtain();
                message1.what=0;
                myHandler.sendMessage(message1);
            }
            catch (Exception e) {
                Log.d(TAG,e.toString());
                Message message1=Message.obtain();
                message1.what=2;
                myHandler.sendMessage(message1);
            }
        }
    }


    private class MyHandler  extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==0)
            {
                loginWindow.showAtLocation(view,Gravity.CENTER,0,0);
                Toast.makeText(getContext(),"账号错误，密码错误或账户不存在",Toast.LENGTH_SHORT).show();
                load_view.setVisibility(View.INVISIBLE);
                content.setVisibility(View.VISIBLE);
            }
            if(msg.what==2)
            {
                loginWindow.showAtLocation(view,Gravity.CENTER,0,0);
                Toast.makeText(getContext(),"网络超时",Toast.LENGTH_SHORT).show();
                load_view.setVisibility(View.INVISIBLE);
                content.setVisibility(View.VISIBLE);
            }
            else if(msg.what==3)
            {
                load_view.setVisibility(View.INVISIBLE);
                content.setVisibility(View.VISIBLE);
                notLogin_view.setVisibility(View.INVISIBLE);
                alreadyLogin_view.setVisibility(View.VISIBLE);
                SerializableList serializableList=(SerializableList)msg.getData().get("userinfo");
                List<String> infos=serializableList.getStringList();
                if(infos.size()==4)
                {
                    studyNumber_view.setText(infos.get(0));
                    name_view.setText(infos.get(1));
                    college_view.setText(infos.get(2));
                    department_view.setText(infos.get(3));
                    ScoreUser.getInstance(infos.get(0),infos.get(1),infos.get(2),infos.get(3));
                }
                isLogin=true;
                loginWindow.dismiss();
            }
            else if(msg.what==1) {
                editor = pref.edit();
                editor.putString("account", account);
                editor.putString("password", password);
                editor.putBoolean("isRemember", isRemember);
                editor.commit();
            }
        }
    }
}
