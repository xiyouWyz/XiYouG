package com.example.wyz.xiyoug.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.LoginFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.example.wyz.xiyoug.R;
import com.example.wyz.xiyoug.Util.CircleImageView;
import com.example.wyz.xiyoug.Util.IsNetworkConnected;
import com.example.wyz.xiyoug.Viewer.AboutOurFragment;
import com.example.wyz.xiyoug.Viewer.LibraryMainFragment;
import com.example.wyz.xiyoug.Viewer.ScheduleFragment;
import com.example.wyz.xiyoug.Viewer.ScoreMyFragment;
import com.example.wyz.xiyoug.Viewer.SetLogoChoice;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    private Toolbar toolbar;
    private DrawerLayout dlMain;
    private ActionBarDrawerToggle drawerToggle;
    private LinearLayout menuLayout;
    private LinearLayout login_view;
    private RelativeLayout library_view;
    private RelativeLayout score_view;
    private RelativeLayout schedule_view;
    private RelativeLayout our_view;
    private RelativeLayout feedback_view;
    private Fragment scheduleFragment;
    private Fragment libraryFragment;
    private Fragment scoreFragment;
    private Fragment about_ourFragment;
    private FragmentManager fm;
    private CircleImageView circleImageView;
    private  Menu  menu;
    private  final String TAG="MainActivity";
    private  Uri imageUri;
    private   static  final  int TAKE_PHOTO=1;
    private  static  final  int CROP_PHOTO=2;
    private  LinearLayout setLogo_layout;
    private   SetLogoChoice setLogoChoice;
    private final  String ALBUM_PATH=Environment.getExternalStorageDirectory().getPath()+"/logo";
    private final  String ALBUM_ALL_PATH=Environment.getExternalStorageDirectory().getPath()+"/logo/logo.png";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initLogoView();
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
                if(!IsNetworkConnected.isNetworkConnected(MainActivity.this))
                {
                    Toast.makeText(MainActivity.this,"网络超时",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent intent=new Intent();
                    intent.setClass(MainActivity.this,BookSearchActivity.class);
                    startActivity(intent);

                }
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

        if(!IsNetworkConnected.isNetworkConnected(MainActivity.this))
        {
            Toast.makeText(MainActivity.this,"网络超时",Toast.LENGTH_SHORT).show();
        }
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
        setLogo_layout=(LinearLayout)findViewById(R.id.setLogo);

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
    private void initLogoView() {
        circleImageView=(CircleImageView)findViewById(R.id.logo);
        circleImageView.setOnClickListener(this);
        File outputImage=new File(ALBUM_PATH,"/logo.png/");
        try
        {
            if(outputImage.exists())
            {
                Bitmap bitmap=BitmapFactory.decodeFile(ALBUM_ALL_PATH);
                circleImageView.setImageBitmap(bitmap);
            }
            else
            {
                circleImageView.setImageResource(R.drawable.logo);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.logo:
                setLogoChoice=new SetLogoChoice(MainActivity.this,new SetLogoClick());
                setLogoChoice.showAtLocation(view, Gravity.BOTTOM,0,0);
                break;
            case  R.id.library:
                setFragmentSelect(R.id.library);
                menu.getItem(0).setVisible(true);
                dlMain.closeDrawers();
                break;

            case  R.id.score:
                setFragmentSelect(R.id.score);
                menu.getItem(0).setVisible(false);
                dlMain.closeDrawers();
                break;
            case  R.id.schedule:
                menu.getItem(0).setVisible(false);
                setFragmentSelect(R.id.schedule);
                dlMain.closeDrawers();
                break;
            case  R.id.our:
                setFragmentSelect(R.id.our);
                menu.getItem(0).setVisible(false);
                dlMain.closeDrawers();
                break;
            case  R.id.feedback:
                Intent data=new Intent();
                data.setAction(Intent.ACTION_SENDTO);
                data.setData(Uri.parse("mailto:745322878@qq.com"));
                startActivity(data);
                break;


        }
    }
    private class SetLogoClick implements  View.OnClickListener
    {

        @Override
        public void onClick(View view) {
            switch (view.getId())
            {
                case R.id.take_photo:
                    take_photo();
                    break;
                case  R.id.album:
                    select_album();
                    break;
                case R.id.cancel:
                    setLogoChoice.dismiss();
                    break;
            }
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
            case  R.id.score:
                if(scoreFragment==null)
                {
                    scoreFragment=new ScoreMyFragment();
                    transaction.add(R.id.dl_container,scoreFragment);
                }
                else
                {
                    transaction.show(scoreFragment);
                }
                break;
            case  R.id.our:
                if(about_ourFragment==null)
                {
                    about_ourFragment=new AboutOurFragment();
                    transaction.add(R.id.dl_container,about_ourFragment);
                }
                else
                {
                    transaction.show(about_ourFragment);
                }
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
        if(scoreFragment!=null)
        {
            transaction.hide(scoreFragment);
        }
        if(about_ourFragment!=null)
        {
            transaction.hide(about_ourFragment);
        }
    }

    private  void take_photo()
    {

        File outputImage = new File(ALBUM_PATH+"logo.jpg");  //新建图片
        File filePath = new File(ALBUM_PATH);
        if (!filePath.exists()) {  //如果文件夹不存在，创建文件夹
            filePath.mkdirs();
        }
     /*   try
        {
            if(outputImage.exists())
            {
                outputImage.delete();
            }
            outputImage.createNewFile();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }*/
        imageUri=Uri.fromFile(outputImage);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(intent,TAKE_PHOTO);
    }
    private  void select_album()
    {
        File outputImage = new File(ALBUM_PATH+"/logo.png/");  //新建图片
        File filePath = new File(ALBUM_PATH);
        if (!filePath.exists()) {  //如果文件夹不存在，创建文件夹
            filePath.mkdirs();
        }
       /* File outputImage=new File(Environment.getExternalStorageDirectory(),"logo.jpg");*/
    /*    try
        {
            if(outputImage.exists())
            {
                outputImage.delete();
            }
            outputImage.createNewFile();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }*/
        imageUri=Uri.fromFile(outputImage);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("image/*");
        intent.putExtra("crop","true");
        intent.putExtra("scale",true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(intent, CROP_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode)
        {
            case  TAKE_PHOTO:
                if(resultCode==RESULT_OK)
                {
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(imageUri,"image/*");
                    intent.putExtra("scale",true);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                    startActivityForResult(intent,CROP_PHOTO);
                }
                break;
            case  CROP_PHOTO:
                if(resultCode==RESULT_OK)
                {
                    try
                    {
                        Bitmap bitmap= BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        saveBitmap(bitmap);
                        circleImageView.setImageBitmap(bitmap);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Bitmap bitmap= null;
                    try {
                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        if(bitmap==null) {
                            circleImageView.setImageResource(R.drawable.logo);
                        }
                        else
                        {
                            circleImageView.setImageBitmap(bitmap);
                        }
                    } catch (FileNotFoundException e) {
                        Log.d(TAG,e.toString());
                        circleImageView.setImageResource(R.drawable.logo);
                    }

                }
                break;
            default:
                break;
        }
    }

    private  void saveBitmap(Bitmap bm) {
        BufferedOutputStream os = null;
        String _file="/logo.png/";
        try {
            File file = new File(ALBUM_PATH+_file);  //新建图片
            File filePath = new File(ALBUM_PATH);
            if (!filePath.exists()) {  //如果文件夹不存在，创建文件夹
                filePath.mkdirs();
            }
            file.createNewFile(); //创建图片文件
            os = new BufferedOutputStream(new FileOutputStream(file));
            bm.compress(Bitmap.CompressFormat.PNG, 100, os);  //图片存成png格式。
        }
        catch (Exception e)
        {
            Log.d(TAG,e.toString());
        }
        finally {
            if (os != null) {
                try {
                    os.close();  //关闭流
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        }
    }
}