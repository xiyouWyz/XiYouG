package com.example.wyz.xiyoug;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wyz.xiyoug.View.HomeFragment;
import com.example.wyz.xiyoug.View.InfoFragment;
import com.example.wyz.xiyoug.View.LibraryMainFragment;
import com.example.wyz.xiyoug.View.MyFragment;
import com.example.wyz.xiyoug.View.SlideMenu;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private Toolbar toolbar;
    private DrawerLayout dlMain;
    private ActionBarDrawerToggle drawerToggle;

    private Fragment libraryFragment;
    private Fragment clickGetMoreFragment;
    private Fragment addExtraHeaderFragment1;
    private Fragment addExtraHeaderFragment2;



    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
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

    private void initView() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        dlMain = (DrawerLayout) findViewById(R.id.dl_main);




        drawerToggle = new ActionBarDrawerToggle(this, dlMain, toolbar, 0, 0);
        drawerToggle.syncState();
        dlMain.setDrawerListener(drawerToggle);

        //btnClickGetMore = (Button) findViewById(R.id.btn_click_get_more);
        //btnAddExtraHeader1 = (Button) findViewById(R.id.btn_add_extra_header1);
        //btnAddExtraHeader2 = (Button) findViewById(R.id.btn_add_extra_header2);


        libraryFragment = new LibraryMainFragment();
        //clickGetMoreFragment = new ClickGetMoreFragment();
        //addExtraHeaderFragment1 = new AddExtraHeaderFragment1();
        //addExtraHeaderFragment2 = new AddExtraHeaderFragment2();

        fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.dl_container, libraryFragment).commit();





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


    }


}
