package com.example.wyz.xiyoug.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wyz.xiyoug.R;
import com.example.wyz.xiyoug.RecyclerView.DividerItemDecoration;
import com.example.wyz.xiyoug.RecyclerView.OnItemOnClickListenerInterface;
import com.example.wyz.xiyoug.Util.JsonHandle;
import com.example.wyz.xiyoug.Util.MyAnimation;
import com.example.wyz.xiyoug.Util.ScheduleOkHttp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Wyz on 2016/8/4.
 */
public class ScoreActivity extends AppCompatActivity {

    private  final String  TAG="ScoreActivity";
    private  String score_html;
    private  String score_url;
    private  String sessionId;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score_all);
        getListData();
        setupViewComponent();
        getScoreThread=new GetScoreThread();
        new Thread(getScoreThread).start();
    }
    private void setupViewComponent() {
        load_view=(RelativeLayout)findViewById(R.id.loading);
        content=(LinearLayout)findViewById(R.id.content);
        new MyAnimation(ScoreActivity.this,"胖萌正在努力为您加载...",R.drawable.loading,load_view);
        spinner=(Spinner)findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                oneSemesterScore=lists.get(i);
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        linearLayoutManager=new LinearLayoutManager(ScoreActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        DividerItemDecoration dividerItemDecoration =new DividerItemDecoration(ScoreActivity.this,DividerItemDecoration.VERTICAL_LIST,1);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(linearLayoutManager);

    }
    private  void setData()
    {
        if(lists!=null)
        {
            for(int i=0;i<lists.size();i++)
            {
                String title=lists.get(i).get(i).get("title");
                semester_title.add(title);
            }
        }
        arrayAdapter=new ArrayAdapter<String>(ScoreActivity.this,android.R.layout.simple_spinner_item,semester_title);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        myAdapter=new MyAdapter();
        myAdapter.setOnItemClickListener(new OnItemOnClickListenerInterface.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                String s=oneSemesterScore.get(position).get("课程名称");
                Toast.makeText(ScoreActivity.this,s,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnItemLongClick(View view, int position) {

            }
        });
        recyclerView.setAdapter(myAdapter);

    }
    private  void getListData()
    {
        Intent intent = getIntent();
        score_html=intent.getStringExtra("score_html");
        score_url=intent.getStringExtra("score_url");
        sessionId=intent.getStringExtra("sessionId");
    }
    private  class GetScoreThread implements  Runnable
    {

        @Override
        public void run() {

            try {
                String viewState= JsonHandle.getViewState(score_html);
                List<String> semesters=JsonHandle.getSemesterCount(score_html);
                List<String> allScoreHtml= null;
                allScoreHtml = ScheduleOkHttp.getPostScore(score_url,sessionId,viewState,semesters);
                lists=JsonHandle.getAllScore(allScoreHtml);
                oneSemesterScore=lists.get(0);

                Log.d(TAG,lists.toString());
                Message message=Message.obtain();
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

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what)
            {
                case 1:
                    load_view.setVisibility(View.INVISIBLE);
                    content.setVisibility(View.VISIBLE);
                    setData();
                    break;
                case  2:
                    Toast.makeText(ScoreActivity.this,"网络超时",Toast.LENGTH_SHORT).show();
                    load_view.setVisibility(View.INVISIBLE);
                    content.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }
    private class MyAdapter extends  RecyclerView.Adapter<MyAdapter.MyViewHolder>{
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            MyViewHolder holder=new MyViewHolder(LayoutInflater.from(ScoreActivity.this).inflate(R.layout.score_recyclerview_item,null));
            return  holder;
        }
        @Override
        public void onBindViewHolder(final MyViewHolder myViewHolder, int i) {
            myViewHolder.scorename_view.setText(oneSemesterScore.get(i).get("课程名称"));
            myViewHolder.score_view.setText(oneSemesterScore.get(i).get("成绩"));
            myViewHolder.credit_view.setText(oneSemesterScore.get(i).get("学分"));
            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos=myViewHolder.getPosition();
                    onItemClickListener.OnItemClick(view,pos);
                }
            });
        }

        @Override
        public int getItemCount() {

            if(oneSemesterScore!=null)
            {
                return  oneSemesterScore.size();
            }
            return 0;
        }

        public  class  MyViewHolder extends  RecyclerView.ViewHolder
        {
            public TextView scorename_view;
            private  TextView score_view;
            private  TextView credit_view;

            public MyViewHolder(View itemView) {
                super(itemView);
                scorename_view=(TextView)itemView.findViewById(R.id.score_name);
                score_view=(TextView)itemView.findViewById(R.id.score);
                credit_view=(TextView)itemView.findViewById(R.id.credit);
            }
        }
        private OnItemOnClickListenerInterface.OnItemClickListener onItemClickListener;


        public void setOnItemClickListener(OnItemOnClickListenerInterface.OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }
    }

}
