package com.example.wyz.xiyoug.View;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wyz.xiyoug.Activity.MainActivity;
import com.example.wyz.xiyoug.Model.HttpLinkHeader;
import com.example.wyz.xiyoug.R;
import com.example.wyz.xiyoug.Util.JsonHandle;
import com.example.wyz.xiyoug.Util.MyAnimation;
import com.example.wyz.xiyoug.Util.OkHttpUtil;
import com.example.wyz.xiyoug.Util.ReadFile;
import com.example.wyz.xiyoug.Util.SaveFile;
import com.example.wyz.xiyoug.Util.ScheduleOkHttp;
import com.example.wyz.xiyoug.Util.SerializableMap;

import org.apache.http.ConnectionClosedException;
import org.apache.http.message.BasicNameValuePair;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Wyz on 2016/7/31.
 */
public class ScheduleFragment  extends Fragment{
    private  View view;
    private  TextView semester;
    private TextView day1_one_view;
    private TextView day1_two_view;
    private TextView day1_three_view;
    private TextView day1_four_view;
    private TextView day2_one_view;
    private TextView day2_two_view;
    private TextView day2_three_view;
    private TextView day2_four_view;
    private TextView day3_one_view;
    private TextView day3_two_view;
    private TextView day3_three_view;
    private TextView day3_four_view;
    private TextView day4_one_view;
    private TextView day4_two_view;
    private TextView day4_three_view;
    private TextView day4_four_view;
    private TextView day5_one_view;
    private TextView day5_two_view;
    private TextView day5_three_view;
    private TextView day5_four_view;
    private Button import_btn;
    private  Map<String,List<String>> stringListMap;
    private  MyThread myThread;
    private  MyHandler myHandler=new MyHandler();
    private  LoginWindow loginWindow;
    private SharedPreferences pref;
    private  SharedPreferences.Editor editor;
    private LinearLayout content;
    private RelativeLayout load_view;
    private  final String TAG="ScheduleFragment";
    private  String schedule;
    private static String sessionId="";
    private  static   String schedule_url;
    private   static boolean isLogin;
    private   static String account="",password="";
    private   static String name="";
    private static boolean isRemember=false;
    private  static  String semester_title;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.schedule,container,false);
        setupViewComponent();
        initSchedule();
        return  view;
    }

    private void setupViewComponent() {
        semester=(TextView)view.findViewById(R.id.semester);
        day1_one_view=(TextView)view.findViewById(R.id.day1_one);
        day1_two_view=(TextView)view.findViewById(R.id.day1_two);
        day1_three_view=(TextView)view.findViewById(R.id.day1_three);
        day1_four_view=(TextView)view.findViewById(R.id.day1_four);
        day2_one_view=(TextView)view.findViewById(R.id.day2_one);
        day2_two_view=(TextView)view.findViewById(R.id.day2_two);
        day2_three_view=(TextView)view.findViewById(R.id.day2_three);
        day2_four_view=(TextView)view.findViewById(R.id.day2_four);
        day3_one_view=(TextView)view.findViewById(R.id.day3_one);
        day3_two_view=(TextView)view.findViewById(R.id.day3_two);
        day3_three_view=(TextView)view.findViewById(R.id.day3_three);
        day3_four_view=(TextView)view.findViewById(R.id.day3_four);
        day4_one_view=(TextView)view.findViewById(R.id.day4_one);
        day4_two_view=(TextView)view.findViewById(R.id.day4_two);
        day4_three_view=(TextView)view.findViewById(R.id.day4_three);
        day4_four_view=(TextView)view.findViewById(R.id.day4_four);
        day5_one_view=(TextView)view.findViewById(R.id.day5_one);
        day5_two_view=(TextView)view.findViewById(R.id.day5_two);
        day5_three_view=(TextView)view.findViewById(R.id.day5_three);
        day5_four_view=(TextView)view.findViewById(R.id.day5_four);
        import_btn=(Button)view.findViewById(R.id.import_schedule);
        import_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //pref= PreferenceManager.getDefaultSharedPreferences(getContext());
                pref=getContext().getSharedPreferences("schedule_info", Context.MODE_PRIVATE);
                boolean remember=pref.getBoolean("isRemember",false);
                String account=pref.getString("account","");
                String password=pref.getString("password","");
                loginWindow=new LoginWindow(getContext(),new MyLoginOnClickListener(),account,password,remember,1);
                loginWindow.showAtLocation(view, Gravity.CENTER,0,0);
            }
        });
        content=(LinearLayout)view.findViewById(R.id.content);
        load_view=(RelativeLayout)view.findViewById(R.id.loading);
        load_view.setVisibility(View.INVISIBLE);
    }
    private void initSchedule() {
            String schedule= ReadFile.readSchedule(getContext());
            if(!schedule.equals("scheduleInfo"))
            {
                stringListMap=JsonHandle.getSchedule(schedule);
                setSchedule();
            }
    }
    private void setSchedule() {
        semester.setText(semester_title);
       day1_one_view.setText(stringListMap.get("time1").get(0));
       day2_one_view.setText(stringListMap.get("time1").get(1));
       day3_one_view.setText(stringListMap.get("time1").get(2));
       day4_one_view.setText(stringListMap.get("time1").get(3));
       day5_one_view.setText(stringListMap.get("time1").get(4));
       day1_two_view.setText(stringListMap.get("time2").get(0));
       day2_two_view.setText(stringListMap.get("time2").get(1));
       day3_two_view.setText(stringListMap.get("time2").get(2));
       day4_two_view.setText(stringListMap.get("time2").get(3));
       day5_two_view.setText(stringListMap.get("time2").get(4));
       day1_three_view.setText(stringListMap.get("time3").get(0));
       day2_three_view.setText(stringListMap.get("time3").get(1));
       day3_three_view.setText(stringListMap.get("time3").get(2));
       day4_three_view.setText(stringListMap.get("time3").get(3));
       day5_three_view.setText(stringListMap.get("time3").get(4));
       day1_four_view.setText(stringListMap.get("time4").get(0));
       day2_four_view.setText(stringListMap.get("time4").get(1));
       day3_four_view.setText(stringListMap.get("time4").get(2));
       day4_four_view.setText(stringListMap.get("time4").get(3));
       day5_four_view.setText(stringListMap.get("time4").get(4));
    }
    private class MyHandler  extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==0)
            {
                Toast.makeText(getContext(),"账号错误，密码错误或账户不存在",Toast.LENGTH_SHORT).show();
                load_view.setVisibility(View.INVISIBLE);
                content.setVisibility(View.VISIBLE);
                loginWindow.showAtLocation(view,Gravity.CENTER,0,0);
            }
             if(msg.what==2)
            {
                Toast.makeText(getContext(),"网络超时",Toast.LENGTH_SHORT).show();
                load_view.setVisibility(View.INVISIBLE);
                content.setVisibility(View.VISIBLE);
                loginWindow.showAtLocation(view,Gravity.CENTER,0,0);
            }
            else if(msg.what==3)
            {
                //Toast.makeText(getContext(),"导入成功",Toast.LENGTH_SHORT).show();
                SerializableMap map=(SerializableMap)msg.getData().get("schedule");
                stringListMap= map.getMap();
                load_view.setVisibility(View.INVISIBLE);
                content.setVisibility(View.VISIBLE);

                setSchedule();
            }
             else if(msg.what==1)
             {
                 isLogin=true;
                // loginWindow.dismiss();

                 editor=pref.edit();
                 editor.putString("account",account);
                 editor.putString("password",password);
                 editor.putBoolean("isRemember",isRemember);
                 editor.commit();


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


                List<BasicNameValuePair> basicNameValuePairs=new ArrayList<>();
                basicNameValuePairs.add(new BasicNameValuePair("xh",account));
                basicNameValuePairs.add(new BasicNameValuePair("xm",name));
                basicNameValuePairs.add(new BasicNameValuePair("gnmkdm","N121603"));
                schedule_url=OkHttpUtil.attachHttpGetParams(HttpLinkHeader.Schedule_KB,basicNameValuePairs);
                try {
                    schedule = ScheduleOkHttp.getGetSchedule(schedule_url,sessionId);
                    semester_title=JsonHandle.getSemesterSchedule(schedule);
                    SaveFile.saveSchedule(getContext(),schedule);
                    Map<String,List<String>> listMap=JsonHandle.getSchedule(schedule);
                    Message message1=new Message();
                    Bundle bundle=new Bundle();
                    SerializableMap serializableMap=new SerializableMap();
                    serializableMap.setMap(listMap);
                    bundle.putSerializable("schedule",serializableMap);
                    message1.what=3;
                    message1.setData(bundle);
                    myHandler.sendMessage(message1);
                } catch (Exception e) {
                    Log.d(TAG,e.toString());
                    Message message1=new Message();
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
                        loginWindow.dismiss();
                        new MyAnimation(getContext(),"胖萌正在努力为您登录...",R.drawable.loading,load_view);
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
}
