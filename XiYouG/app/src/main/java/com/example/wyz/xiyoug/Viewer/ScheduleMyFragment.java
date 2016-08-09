/*
package com.example.wyz.xiyoug.View;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.PermissionChecker;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wyz.xiyoug.Model.HttpLinkHeader;
import com.example.wyz.xiyoug.R;
import com.example.wyz.xiyoug.Util.JsonHandle;
import com.example.wyz.xiyoug.Util.OkHttpUtil;
import com.example.wyz.xiyoug.Util.ScheduleOkHttp;
import com.example.wyz.xiyoug.Util.SerializableMap;

import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

*
 * Created by Wyz on 2016/8/2.


public class ScheduleMyFragment extends Fragment{
    private  View view;
    private TextView name_view;
    private  LinearLayout my_login;
    private  LinearLayout my_exit;
    private  final String TAG="ScheduleMyFragment";
    private  LoginWindow loginWindow;
    private SharedPreferences pref;
    private  SharedPreferences.Editor editor;
    private MyThread myThread;
    private  MyHandler myHandler=new MyHandler();
    public  static   Map<String,List<String>> stringListMap=null;

    public static String sessionId="";
    public  static   String schedule_url;
    public   static boolean isLogin;
    public   static String account="",password="";
    public   static String name="";
    public static boolean isRemember,isAuto=false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.schedule_my,container,false);
        name_view=(TextView)view.findViewById(R.id.studyNumber);
        setupViewComponent();
        return  view;

    }

    private void setupViewComponent() {

        my_login=(LinearLayout)view.findViewById(R.id.login);
        my_exit=(LinearLayout)view.findViewById(R.id.exit);
        my_exit.setOnClickListener(new MyOnClickListener());
        my_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pref= PreferenceManager.getDefaultSharedPreferences(getContext());
                boolean remember=pref.getBoolean("isRemember",false);
                boolean auto=pref.getBoolean("isAuto",false);
                String account=pref.getString("account","");
                String password=pref.getString("password","");
                loginWindow=new LoginWindow(getContext(),new MyLoginOnClickListener(),account,password,isRemember,1);
                loginWindow.showAtLocation(view, Gravity.CENTER,0,0);
            }
        });
    }
    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId())
            {
                case R.id.exit:
                    if(isLogin)
                    {

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
                sessionId=ScheduleOkHttp.postGetSessionFromServer(HttpLinkHeader.Schedule_Login,account,password);
                String info=ScheduleOkHttp.getGetInfoFromServer(HttpLinkHeader.Schedule_Main,sessionId);
                name= JsonHandle.getName(info);
                Message message=new Message();
                message.what=1;
                myHandler.sendMessage(message);
            } catch (Exception e) {
                Log.d(TAG,e.toString());
                Message message=Message.obtain();
                message.what=2;
                myHandler.sendMessage(message);
            }
        }
    }


    private class MyHandler  extends Handler {
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
                name_view.setText(name);
                isLogin=true;
                loginWindow.dismiss();
            }
        }
    }
}
*/
