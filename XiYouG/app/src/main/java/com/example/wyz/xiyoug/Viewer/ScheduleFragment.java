package com.example.wyz.xiyoug.Viewer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wyz.xiyoug.Activity.QuestionActivity;
import com.example.wyz.xiyoug.Model.HttpLinkHeader;
import com.example.wyz.xiyoug.R;
import com.example.wyz.xiyoug.Util.AnnotateUtils;
import com.example.wyz.xiyoug.Util.IsNetworkConnected;
import com.example.wyz.xiyoug.Util.JsonHandle;
import com.example.wyz.xiyoug.Util.MyAnimation;
import com.example.wyz.xiyoug.Util.OkHttpUtil;
import com.example.wyz.xiyoug.Util.ReadFile;
import com.example.wyz.xiyoug.Util.SaveFile;
import com.example.wyz.xiyoug.Util.ScheduleOkHttp;
import com.example.wyz.xiyoug.Util.SerializableMap;

import org.apache.http.message.BasicNameValuePair;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by Wyz on 2016/7/31.
 */
public class ScheduleFragment  extends Fragment{

    private static final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE= 2;
    public   View view;
    private  TextView semester;

    //@BindView(R.id.day1_one)
    @AnnotateUtils.ViewInject(id=R.id.day1_one)
    private TextView day1_one_view;
    //@BindView( R.id.day1_two)
    @AnnotateUtils.ViewInject(id=R.id.day1_two)
    private TextView day1_two_view;
    //@BindView( R.id.day1_three)
    @AnnotateUtils.ViewInject(id=R.id.day1_three)
    private TextView day1_three_view;
   // @BindView( R.id.day1_four)
   @AnnotateUtils.ViewInject(id=R.id.day1_four)
     TextView day1_four_view;
   // @BindView( R.id.day2_one)
   @AnnotateUtils.ViewInject(id=R.id.day2_one)
     TextView day2_one_view;
    //@BindView( R.id.day2_two)
    @AnnotateUtils.ViewInject(id=R.id.day2_two)
     TextView day2_two_view;
   // @BindView( R.id.day2_three)
   @AnnotateUtils.ViewInject(id=R.id.day2_three)
     TextView day2_three_view;
   // @BindView( R.id.day2_four)
   @AnnotateUtils.ViewInject(id=R.id.day2_four)
     TextView day2_four_view;
    //@BindView( R.id.day3_one)
    @AnnotateUtils.ViewInject(id=R.id.day3_one)
     TextView day3_one_view;
   // @BindView( R.id.day3_two)
   @AnnotateUtils.ViewInject(id=R.id.day3_two)
     TextView day3_two_view;
   // @BindView( R.id.day3_three)
   @AnnotateUtils.ViewInject(id=R.id.day3_three)
     TextView day3_three_view;
   // @BindView( R.id.day3_four)
   @AnnotateUtils.ViewInject(id=R.id.day3_four)
     TextView day3_four_view;
    // @BindView(R.id.day4_one)
    @AnnotateUtils.ViewInject(id=R.id.day4_one)
     TextView day4_one_view;
    // @BindView( R.id.day4_two)
    @AnnotateUtils.ViewInject(id=R.id.day4_two)
     TextView day4_two_view;
    // @BindView(R.id.day4_three)
    @AnnotateUtils.ViewInject(id=R.id.day4_three)
     TextView day4_three_view;
    //   @BindView( R.id.day4_four)
    @AnnotateUtils.ViewInject(id=R.id.day4_four)
     TextView day4_four_view;
    //  @BindView( R.id.day5_one)
    @AnnotateUtils.ViewInject(id=R.id.day5_one)
     TextView day5_one_view;
   //  @BindView(R.id.day5_two)
   @AnnotateUtils.ViewInject(id=R.id.day5_two)
     TextView day5_two_view;
   //  @BindView( R.id.day5_three)
   @AnnotateUtils.ViewInject(id=R.id.day5_three)
     TextView day5_three_view;
   //  @BindView( R.id.day5_four)
   @AnnotateUtils.ViewInject(id=R.id.day5_four)
     TextView day5_four_view;
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
    public static String sessionId="";
    private     String schedule_url;
    private    String account="",password="",valiCode="";
    private    String name="";
    private  boolean isRemember=false;
    private    String semester_title;
    private    boolean remember;
    private    String filePath;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.schedule,container,false);
        setupViewComponent();
        initSchedule();
        return  view;
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.d("ScheduleFragment", "onPause");
    }
    public class GetCheckCodeThread implements  Runnable
    {
        @Override
        public void run() {
            try {
                filePath=ScheduleOkHttp.GetRequestCheckCodeByOkHttp(HttpLinkHeader.CHECKCODE_URL);
                Message message=Message.obtain();
                message.what=6;
                myHandler.sendMessage(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void setupViewComponent() {
        semester=(TextView)view.findViewById(R.id.semester);
        AnnotateUtils.injectViews(ScheduleFragment.this);
        FloatingActionButton import_btn = (FloatingActionButton) view.findViewById(R.id.import_schedule);
        FloatingActionButton update_btn = (FloatingActionButton) view.findViewById(R.id.update_schedule);
        import_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!IsNetworkConnected.isNetworkConnected(getContext()))
                {
                    Message message=Message.obtain();
                    message.what=4;
                    myHandler.sendMessage(message);
                }
                else
                {
                    //检查读写权限
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED&&
                            ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        //没有权限，请求开启权限
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
                    }
                    //具有权限
                    else {
                        //pref= PreferenceManager.getDefaultSharedPreferences(getContext());
                        pref=getContext().getSharedPreferences("schedule_info", Context.MODE_PRIVATE);
                        remember=pref.getBoolean("isRemember",false);
                        account=pref.getString("account","");
                        password=pref.getString("password","");
                        GetCheckCodeThread thread=new GetCheckCodeThread();
                        new Thread(thread).start();
                    }
                }
            }
        });
        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            /*    if(!IsNetworkConnected.isNetworkConnected(getContext()))
                {
                    Message message=Message.obtain();
                    message.what=4;
                    myHandler.sendMessage(message);
                }
                else
                {
                    //pref= PreferenceManager.getDefaultSharedPreferences(getContext());
                    pref=getContext().getSharedPreferences("schedule_info", Context.MODE_PRIVATE);
                    boolean remember=pref.getBoolean("isRemember",false);
                    String account=pref.getString("account","");
                    String password=pref.getString("password","");
                    if(account.equals("")||password.equals(""))
                    {
                        Toast.makeText(getContext(),"更新失败，请点击右方导入课表按钮",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        new MyAnimation(getContext(),"胖萌正在努力为您更新...",R.drawable.loading,load_view);
                        load_view.setVisibility(View.VISIBLE);
                        content.setVisibility(View.INVISIBLE);
                        myThread=new MyThread();
                        new Thread(myThread).start();

                    }
                }*/
                if(!IsNetworkConnected.isNetworkConnected(getContext()))
                {
                    Message message=Message.obtain();
                    message.what=4;
                    myHandler.sendMessage(message);
                }
                else
                {
                    //检查读写权限
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED&&
                            ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        //没有权限，请求开启权限
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
                    }
                    //具有权限
                    else
                    {
                        //pref= PreferenceManager.getDefaultSharedPreferences(getContext());
                        pref=getContext().getSharedPreferences("schedule_info", Context.MODE_PRIVATE);
                        remember=true;
                        account=pref.getString("account","");
                        password=pref.getString("password","");
                        GetCheckCodeThread thread=new GetCheckCodeThread();
                        new Thread(thread).start();
                    }
                }

            }
        });

        content=(LinearLayout)view.findViewById(R.id.content);
        load_view=(RelativeLayout)view.findViewById(R.id.loading);
        load_view.setVisibility(View.INVISIBLE);
        /*  day1_one_view=(TextView)view.findViewById(R.id.day1_one);
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
        day5_four_view=(TextView)view.findViewById(R.id.day5_four);*/
    }

    private void initSchedule() {
            new Thread(){
                @Override
                public void run() {
                    schedule= ReadFile.readSchedule(getContext());
                    if(!schedule.equals("scheduleInfo")&&!schedule.equals("")) {
                        stringListMap = JsonHandle.getSchedule(schedule);
                        semester_title = JsonHandle.getSemesterTitleSchedule(schedule);
                    }
                    Message message=Message.obtain();
                    message.what=7;
                    myHandler.sendMessage(message);
                }
            }.start();

    }
    private void setSchedule() {
        semester.setText(semester_title);
        List<TextView> textViews=new ArrayList<>(Arrays.asList(
                day1_one_view,day2_one_view,day3_one_view,day4_one_view,day5_one_view
            ,day1_two_view,day2_two_view,day3_two_view,day4_two_view,day5_two_view
            ,day1_three_view,day2_three_view,day3_three_view,day4_three_view,day5_three_view
            ,day1_four_view,day2_four_view,day3_four_view,day4_four_view,day5_four_view));
        List<String> times=new ArrayList<>(Arrays.asList("time1","time1","time1","time1","time1",
                "time2","time2","time2","time2","time2",
                "time3","time3","time3","time3","time3",
                "time4","time4","time4","time4","time4"
        ));



        List<Integer> days=new ArrayList<>(Arrays.asList(0,1,2,3,4,0,1,2,3,4,0,1,2,3,4,0,1,2,3,4));
        List<Integer> colors=new ArrayList<>(Arrays.asList(R.color.schedule_day1_one,R.color.schedule_day2_one,R.color.schedule_day3_one,R.color.schedule_day4_one,R.color.schedule_day5_one
            ,R.color.schedule_day1_two,R.color.schedule_day2_two,R.color.schedule_day3_two,R.color.schedule_day4_two,R.color.schedule_day5_two
                ,R.color.schedule_day1_three,R.color.schedule_day2_three,R.color.schedule_day3_three,R.color.schedule_day4_three,R.color.schedule_day5_three
            ,R.color.schedule_day1_four,R.color.schedule_day2_four,R.color.schedule_day3_four,R.color.schedule_day4_four,R.color.schedule_day5_four));

        setScheduleOneData(textViews,times,days,colors);
    }
    private  void setScheduleOneData(List<TextView> textviews,List<String> times,List<Integer> day,List<Integer> color)
    {
        int i=0;
        String str;
        for(i=0;i<textviews.size();i++)
        {
            str=stringListMap.get(times.get(i)).get(day.get(i));
            if(str.equals(""))
            {
                textviews.get(i).setBackgroundColor(getResources().getColor(R.color.schedule_none));
            }
            else
            {
                textviews.get(i).setText(str);
                textviews.get(i).setBackgroundColor(getResources().getColor(color.get(i)));
            }
        }



    }
    private class MyHandler  extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==0)
            {
                Toast.makeText(getContext(),"账号错误，密码错误或账户不存在",Toast.LENGTH_SHORT).show();
                load_view.setVisibility(View.INVISIBLE);
                content.setVisibility(View.VISIBLE);
                GetCheckCodeThread getCheckCodeThread=new GetCheckCodeThread();
                new Thread(getCheckCodeThread).start();
            }
            else if(msg.what==1)
            {
                editor=pref.edit();
                editor.putString("account",account);
                editor.putString("password",password);
                editor.putBoolean("isRemember",isRemember);
                editor.apply();
            }
             else if(msg.what==2)
            {
                Toast.makeText(getContext(),"网络超时",Toast.LENGTH_SHORT).show();
                load_view.setVisibility(View.INVISIBLE);
                content.setVisibility(View.VISIBLE);
                GetCheckCodeThread getCheckCodeThread=new GetCheckCodeThread();
                new Thread(getCheckCodeThread).start();
            }
            else if(msg.what==3)
            {
                SerializableMap map=(SerializableMap)msg.getData().get("schedule");
                if (map != null) {
                    stringListMap= map.getMap();
                }
                load_view.setVisibility(View.INVISIBLE);
                content.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(),"操作成功",Toast.LENGTH_SHORT).show();
                setSchedule();
            }
            else if(msg.what==4)
            {
                Toast.makeText(getContext(),"网络超时",Toast.LENGTH_SHORT).show();
            }
            else if(msg.what==5)
            {
                Toast.makeText(getContext(),"请先在学校官网中进行教师评价，方可查询成绩",Toast.LENGTH_SHORT).show();
                load_view.setVisibility(View.INVISIBLE);
                content.setVisibility(View.VISIBLE);
            }
            else  if(msg.what==6)
            {

                    loginWindow=new LoginWindow(getContext(),new MyLoginOnClickListener(),account,password,remember,1,filePath);
                    loginWindow.showAtLocation(view, Gravity.CENTER,0,0);

            }
            else  if(msg.what==7)
            {
                if(!schedule.equals("scheduleInfo")&&!schedule.equals("")) {
                    new MyAnimation(getContext(),"胖萌正在努力为您加载...",R.drawable.loading,load_view);
                    load_view.setVisibility(View.VISIBLE);
                    content.setVisibility(View.INVISIBLE);

                    setSchedule();
                    content.setVisibility(View.VISIBLE);
                    load_view.setVisibility(View.INVISIBLE);
                }


            }

        }
    }

    private class   MyThread implements Runnable
    {
        @Override
        public void run() {
            try {
                String info=ScheduleOkHttp.PostRequestFormByOkHttp(account,password,valiCode,HttpLinkHeader.XIYOU_Login,ScheduleFragment.sessionId);
                boolean isJudge=JsonHandle.getIsJudge(info);
                if (!isJudge)
                {
                    Message message=Message.obtain();
                    message.what=5;
                    myHandler.sendMessage(message);
                }
                else
                {
                    name= JsonHandle.getName(info);
                    Message message=Message.obtain();
                    message.what=1;
                    myHandler.sendMessage(message);

                    List<BasicNameValuePair> basicNameValuePairs=new ArrayList<>();
                    basicNameValuePairs.add(new BasicNameValuePair("xh",account));
                    basicNameValuePairs.add(new BasicNameValuePair("xm",name));
                    basicNameValuePairs.add(new BasicNameValuePair("gnmkdm","N121603"));
                    schedule_url=OkHttpUtil.attachHttpGetParams(HttpLinkHeader.Schedule_KB,basicNameValuePairs);
                    try {
                        schedule = ScheduleOkHttp.getGetSchedule(schedule_url,sessionId);
                        semester_title=JsonHandle.getSemesterTitleSchedule(schedule);
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
                    valiCode=loginWindow.getvaliCode();
                    if(account.equals(""))
                    {
                        Toast.makeText(getContext(),"用户名不能为空",Toast.LENGTH_SHORT).show();
                    }
                    else if(password.equals(""))
                    {
                        Toast.makeText(getContext(),"密码不能为空",Toast.LENGTH_SHORT).show();
                    }
                    else if(valiCode.equals(""))
                    {
                        Toast.makeText(getContext(),"验证码不能为空",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        loginWindow.dismiss();
                        new MyAnimation(getContext(),"胖萌正在努力为您加载...",R.drawable.loading,load_view);
                        load_view.setVisibility(View.VISIBLE);
                        content.setVisibility(View.INVISIBLE);
                        myThread=new MyThread();
                        new Thread(myThread).start();

                    }
                    break;

                case R.id.introduction:
                    Intent intent=new Intent();
                    intent.setClass(getContext(), QuestionActivity.class);
                    startActivity(intent);
                    break;
                case R.id.close:
                    loginWindow.dismiss();
                    break;
            }
        }
    }
}
