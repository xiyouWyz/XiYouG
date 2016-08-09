/*
package com.example.wyz.xiyoug.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.example.wyz.xiyoug.R;
import com.example.wyz.xiyoug.RecyclerView.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

*/
/**
 * Created by Wyz on 2016/8/5.
 *//*

public class ScoreInterfaceActivity extends AppCompatActivity {
    private  final String  TAG="ScoreActivity";
    private Toolbar toolbar;
    private  String score_html;
    private  String score_url;
    private  String sessionId;
    private  String viewState;
    private  String college;
    private  String semester;

    private  GetScoreThread getScoreThread;
    private Spinner spinner;
    private  List<List<Map<String,String>>> lists=null;
    private  List<Map<String,String>> oneSemesterScore=null;
    private  List<String> semester_title=new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private   MyAdapter myAdapter;
    private RelativeLayout load_view;
    private LinearLayout content;
    private  MyHandler myHandler=new MyHandler();

    private  int semesterCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_all);
        getListData();
        setupViewComponent();
        ThreadGetScore();
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private  void getListData()
    {
        Intent intent = getIntent();
        score_html=intent.getStringExtra("score_html");
        score_url=intent.getStringExtra("score_url");
        sessionId=intent.getStringExtra("sessionId");
    }

    private class GetScoreThread implements Runnable {
        @Override
        public void run() {

        }
    }
}
*/
