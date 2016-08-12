package com.example.wyz.xiyoug.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wyz.xiyoug.Model.ScoreFailedModel;
import com.example.wyz.xiyoug.Model.ScoreModel;
import com.example.wyz.xiyoug.R;
import com.example.wyz.xiyoug.RecyclerView.DividerItemDecoration;
import com.example.wyz.xiyoug.RecyclerView.OnItemOnClickListenerInterface;
import com.example.wyz.xiyoug.Util.JsonHandle;
import com.example.wyz.xiyoug.Util.MyAnimation;
import com.example.wyz.xiyoug.Util.ScheduleOkHttp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ScoreFailedActivity extends AppCompatActivity {
        private  final String  TAG="ScoreActivity";
        private Toolbar toolbar;
        private  String score_html;
        private  GetFailedScoreThread getFailedScoreThread;
        private  List<ScoreFailedModel> failedModels=null;
        private RecyclerView recyclerView;
        private LinearLayoutManager linearLayoutManager;
        private   MyAdapter myAdapter;
        private RelativeLayout load_view;
        private LinearLayout content;
        private  MyHandler myHandler=new MyHandler();
        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.score_failed);
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
        }
        private void setupViewComponent() {
            toolbar=(Toolbar)findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            load_view=(RelativeLayout)findViewById(R.id.loading);
            load_view.setVisibility(View.VISIBLE);
            content=(LinearLayout)findViewById(R.id.content);
            content.setVisibility(View.INVISIBLE);
            new MyAnimation(ScoreFailedActivity.this,"胖萌正在努力为您加载...",R.drawable.loading,load_view);
            recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
            linearLayoutManager=new LinearLayoutManager(ScoreFailedActivity.this);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            DividerItemDecoration dividerItemDecoration =new DividerItemDecoration(ScoreFailedActivity.this,DividerItemDecoration.VERTICAL_LIST,1);
            recyclerView.addItemDecoration(dividerItemDecoration);
            recyclerView.setLayoutManager(linearLayoutManager);
        }



        private void ThreadGetScore() {
            getFailedScoreThread=new GetFailedScoreThread();
            new Thread(getFailedScoreThread).start();
        }

        private  class GetFailedScoreThread implements  Runnable
        {

            @Override
            public void run() {
                DealWithScore();
            }
        }
        private  void setRecyclerViewData()
        {
            myAdapter=new MyAdapter();
            myAdapter.setOnItemClickListener(new OnItemOnClickListenerInterface.OnItemClickListener() {
                @Override
                public void OnItemClick(View view, int position) {
                    String s=failedModels.get(position).course_name;
                    Toast.makeText(ScoreFailedActivity.this,s,Toast.LENGTH_SHORT).show();
                }

                @Override
                public void OnItemLongClick(View view, int position) {

                }
            });
            if(failedModels==null)
            {
                Toast.makeText(ScoreFailedActivity.this,"无未通过记录",Toast.LENGTH_SHORT).show();
            }
            recyclerView.setAdapter(myAdapter);

        }
        private class MyHandler extends Handler {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what)
                {
                    case 1:
                         load_view.setVisibility(View.INVISIBLE);
                        content.setVisibility(View.VISIBLE);
                        setRecyclerViewData();
                        break;
                    case  2:
                        Toast.makeText(ScoreFailedActivity.this,"网络超时",Toast.LENGTH_SHORT).show();
                        load_view.setVisibility(View.INVISIBLE);
                        content.setVisibility(View.VISIBLE);
                        finish();
                        break;
                }
            }
        }
        private class MyAdapter extends  RecyclerView.Adapter<MyAdapter.MyViewHolder>{
            @Override
            public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                MyViewHolder holder=new MyViewHolder(LayoutInflater.from(ScoreFailedActivity.this).inflate(R.layout.score_recyclerview_item,null));
                return  holder;
            }
            @Override
            public void onBindViewHolder(final MyViewHolder myViewHolder, int i) {
                myViewHolder.scorename_view.setText(failedModels.get(i).course_name);
                myViewHolder.score_view.setText(failedModels.get(i).best_score);
                myViewHolder.credit_view.setText(failedModels.get(i).credit);
                myViewHolder.couresNature_view.setText(failedModels.get(i).course_nature);
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

                if(failedModels!=null)
                {
                    return  failedModels.size();
                }
                return 0;
            }

            public  class  MyViewHolder extends  RecyclerView.ViewHolder
            {
                public TextView scorename_view;
                private  TextView score_view;
                private  TextView credit_view;
                private  TextView couresNature_view;

                public MyViewHolder(View itemView) {
                    super(itemView);
                    scorename_view=(TextView)itemView.findViewById(R.id.score_name);
                    score_view=(TextView)itemView.findViewById(R.id.score);
                    credit_view=(TextView)itemView.findViewById(R.id.credit);
                    couresNature_view=(TextView)itemView.findViewById(R.id.courseNature);
                }
            }
            private OnItemOnClickListenerInterface.OnItemClickListener onItemClickListener;


            public void setOnItemClickListener(OnItemOnClickListenerInterface.OnItemClickListener onItemClickListener) {
                this.onItemClickListener = onItemClickListener;
            }
        }
        private  void DealWithScore()
        {
            try {

                failedModels=JsonHandle.getFailedScore(score_html);
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
