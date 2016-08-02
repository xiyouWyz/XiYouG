package com.example.wyz.xiyoug.Activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.wyz.xiyoug.Model.HttpLinkHeader;
import com.example.wyz.xiyoug.R;
import com.example.wyz.xiyoug.Util.ScheduleOkHttp;
import com.example.wyz.xiyoug.View.LibraryMainFragment;
import com.example.wyz.xiyoug.View.LoginWindow;
import com.example.wyz.xiyoug.View.ScheduleFragment;
import com.example.wyz.xiyoug.View.ScheduleMainFragment;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    private Toolbar toolbar;
    private DrawerLayout dlMain;
    private ActionBarDrawerToggle drawerToggle;
    private FrameLayout frameLayout;

    private Fragment content;

    private LinearLayout menuLayout;
    private LinearLayout login_view;
    private RelativeLayout library_view;
    private RelativeLayout score_view;
    private RelativeLayout schedule_view;
    private RelativeLayout our_view;
    private RelativeLayout feedback_view;

    private Fragment scheduleFragment;
    private Fragment libraryFragment;
    private FragmentManager fm;
    private  Menu  menu;
    private  final String TAG="MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        this.menu=menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.search:
                Intent intent=new Intent();
                intent.setClass(MainActivity.this,BookSearchActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            exitBy2Click();
        }
        return  false;
    }
    //是否点击了退出按钮
    private static Boolean isExit = false;
    private void exitBy2Click() {
        Timer tExit=null;
        if(isExit==false)
        {
            //准备退出
            isExit=true;
            Toast.makeText(MainActivity.this,"再按一次退出程序",Toast.LENGTH_SHORT).show();
            tExit=new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit=false;       //取消退出
                }
            },2000 );// 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
        }
        else
        {
            finish();
            System.exit(0);
        }
    }

    private void initView() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        dlMain = (DrawerLayout) findViewById(R.id.dl_main);
        drawerToggle = new ActionBarDrawerToggle(this, dlMain, toolbar, 0, 0);
        drawerToggle.syncState();
        dlMain.setDrawerListener(drawerToggle);


        login_view = (LinearLayout) findViewById(R.id.login);
        library_view = (RelativeLayout) findViewById(R.id.library);
        score_view = (RelativeLayout) findViewById(R.id.score);
        schedule_view=(RelativeLayout)findViewById(R.id.schedule);
        our_view = (RelativeLayout) findViewById(R.id.our);
        feedback_view = (RelativeLayout) findViewById(R.id.feedback);

        menuLayout=(LinearLayout)findViewById(R.id.menuLayout);

        menuLayout.setOnClickListener(this);
        login_view.setOnClickListener(this);
        library_view.setOnClickListener(this);
        score_view.setOnClickListener(this);
        schedule_view.setOnClickListener(this);
        our_view.setOnClickListener(this);
        feedback_view.setOnClickListener(this);

        fm =getSupportFragmentManager();
        setFragmentSelect(R.id.library);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.login:
                Log.d(TAG,"点击了login");
                break;
            case  R.id.library:
                setFragmentSelect(R.id.library);
                menu.getItem(0).setVisible(true);
                dlMain.closeDrawers();
                break;

            case  R.id.score:
                Log.d(TAG,"点击了score");
                break;
            case  R.id.schedule:
                menu.getItem(0).setVisible(false);
                setFragmentSelect(R.id.schedule);
                dlMain.closeDrawers();
                break;
            case  R.id.our:
                Log.d(TAG,"点击了our");
                break;
            case  R.id.feedback:

                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                String[] recipients=new String[]{"745322878@qq.com",""};
                intent.putExtra(Intent.EXTRA_EMAIL,recipients);
                intent.putExtra(Intent.EXTRA_SUBJECT,"Test");
                intent.putExtra(Intent.EXTRA_TEXT, "This is email's message");
                startActivity(Intent.createChooser(intent, "Select email application.."));
               /* Intent data=new Intent(Intent.ACTION_SENDTO);
                data.setData(Uri.parse("mailto:745322878@qq.com"));
                data.putExtra(Intent.EXTRA_SUBJECT, "这是标题");
                data.putExtra(Intent.EXTRA_TEXT, "这是内容");
                startActivity(data);*/
                break;


        }
    }
    private  void setFragmentSelect(int i)
    {
        FragmentTransaction transaction=fm.beginTransaction();
        hideFragments(transaction);
        switch (i)
        {
            case R.id.library:
                if(libraryFragment==null)
                {
                    libraryFragment=new LibraryMainFragment();
                    transaction.add(R.id.dl_container,libraryFragment);
                }
                else
                {
                    transaction.show(libraryFragment);
                }
                break;
            case R.id.schedule:
                if(scheduleFragment==null)
                {
                    scheduleFragment=new ScheduleFragment();
                    transaction.add(R.id.dl_container,scheduleFragment);
                }
                else
                {
                    transaction.show(scheduleFragment);
                }
                break;
        }
        transaction.commitAllowingStateLoss();
    }
    private  void hideFragments(FragmentTransaction transaction)
    {
        if(scheduleFragment!=null)
        {
            transaction.hide(scheduleFragment);
        }
        if(libraryFragment!=null)
        {
            transaction.hide(libraryFragment);
        }
    }
}


/*
        btnClickGetMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm.beginTransaction().replace(R.id.dl_container, clickGetMoreFragment).commit();
                dlMain.closeDrawers();
            }
        });
        btnAddExtraHeader1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm.beginTransaction().replace(R.id.dl_container, addExtraHeaderFragment1).commit();
                dlMain.closeDrawers();
            }
        });

        btnAddExtraHeader2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm.beginTransaction().replace(R.id.dl_container, addExtraHeaderFragment2).commit();
                dlMain.closeDrawers();
            }
        });*/

