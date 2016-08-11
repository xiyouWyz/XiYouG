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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
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

import com.example.wyz.xiyoug.Model.Book_Borrow;
import com.example.wyz.xiyoug.Model.ScoreModel;
import com.example.wyz.xiyoug.R;
import com.example.wyz.xiyoug.RecyclerView.DividerItemDecoration;
import com.example.wyz.xiyoug.RecyclerView.OnItemOnClickListenerInterface;
import com.example.wyz.xiyoug.Util.IsNetworkConnected;
import com.example.wyz.xiyoug.Util.JsonHandle;
import com.example.wyz.xiyoug.Util.MyAnimation;
import com.example.wyz.xiyoug.Util.ScheduleOkHttp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by Wyz on 2016/8/4.
 */
public class ScoreActivity extends AppCompatActivity {

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
    private  List<List<ScoreModel>> lists=null;
    private  List<ScoreModel> oneSemesterScore=null;
    private  List<String> semester_title=new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private   MyAdapter myAdapter;
    private RelativeLayout load_view;
    private LinearLayout content;
    private  MyHandler myHandler=new MyHandler();
    //private  LoadAllScoreThread loadAllScoreThread;

    private  SemesterScoreThread semesterScoreThread;
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
    private void setupViewComponent() {
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        load_view=(RelativeLayout)findViewById(R.id.loading);
        load_view.setVisibility(View.VISIBLE);
        content=(LinearLayout)findViewById(R.id.content);
        content.setVisibility(View.INVISIBLE);
        new MyAnimation(ScoreActivity.this,"胖萌正在努力为您加载...",R.drawable.loading,load_view);
        spinner=(Spinner)findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(!IsNetworkConnected.isNetworkConnected(ScoreActivity.this))
                {
                    Message message=Message.obtain();
                    message.what=3;
                    myHandler.sendMessage(message);
                }
                else
                {
                    //oneSemesterScore=lists.get(i);
                    String selectStr=semester_title.get(i);
                    college=getCollege(selectStr);
                    semester=getSemester(selectStr);
                    new MyAnimation(ScoreActivity.this,"胖萌正在努力为您加载...",R.drawable.loading,load_view);
                    load_view.setVisibility(View.VISIBLE);
                    content.setVisibility(View.INVISIBLE);
                    //myAdapter.notifyDataSetChanged();
                    semesterScoreThread =new SemesterScoreThread();
                    new Thread(semesterScoreThread).start();
                }

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



    private void ThreadGetScore() {
        getScoreThread=new GetScoreThread();
        new Thread(getScoreThread).start();
    }
    private String getCollege(String selectStr) {
        String result;
        int index=0;
        char[] chs=selectStr.toCharArray();
        for(int i=0;i<chs.length;i++)
        {
            if(chs[i]=='学')
            {
                index=i;
                break;
            }
        }
        result=selectStr.substring(0,index);
        return result;
    }
    private String getSemester(String selectStr) {
        char[] chs=selectStr.toCharArray();
        for(int i=0;i<chs.length;i++)
        {
            if(chs[i]=='第')
            {
                i+=1;
                return  String.valueOf(chs[i]);
            }
        }
        return "1";
    }



private  class GetScoreThread implements  Runnable
{

    @Override
    public void run() {
        DealWithFirstScore();
    }
}
    private  void setFirstData()
    {
        if(semester_title!=null)
        {
            arrayAdapter=new ArrayAdapter<String>(ScoreActivity.this,android.R.layout.simple_spinner_item,semester_title)
            {
                @Override
                public View getView(int position, View convertView, ViewGroup parent)
                {
                    return setCentered(super.getView(position, convertView, parent));
                }

                @Override
                public View getDropDownView(int position, View convertView, ViewGroup parent)
                {
                    return setCentered(super.getDropDownView(position, convertView, parent));
                }

                private View setCentered(View view)
                {
                    TextView textView = (TextView)view.findViewById(android.R.id.text1);
                    textView.setGravity(Gravity.CENTER);
                    return view;
                }


            };
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(arrayAdapter);
        }

        myAdapter=new MyAdapter();
        myAdapter.setOnItemClickListener(new OnItemOnClickListenerInterface.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                String s=oneSemesterScore.get(position).course_name;
                Toast.makeText(ScoreActivity.this,s,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnItemLongClick(View view, int position) {

            }
        });
        recyclerView.setAdapter(myAdapter);

    }
private  class SemesterScoreThread implements Runnable
{
    @Override
    public void run() {
        try {
            String semester_html=ScheduleOkHttp.getPostSemesterScore(score_url,sessionId,viewState,college,semester);
            oneSemesterScore=JsonHandle.getSemesterScore(semester_html);
            Message message=Message.obtain();
            message.what=4;
            myHandler.sendMessage(message);
        } catch (Exception e) {
            Log.d(TAG,e.toString());
            Message message=Message.obtain();
            message.what=3;
            myHandler.sendMessage(message);
        }
    }
}
/* private  class  LoadAllScoreThread implements  Runnable
 {
     @Override
     public void run() {

         try {
             viewState= JsonHandle.getViewState(score_html);
             List<String> year=JsonHandle.getYearCount(score_html);
             List<String> allScoreHtml= null;
             allScoreHtml = ScheduleOkHttp.getPostScore(score_url,sessionId,viewState,year);
             lists=JsonHandle.getAllScore(allScoreHtml);
             oneSemesterScore=lists.get(0);
             Message message=Message.obtain();
             message.what=1;
             myHandler.sendMessage(message);
             Log.d(TAG,lists.toString());
         } catch (Exception e1) {
             Log.d(TAG, e1.toString());
         }
     }
 }
 private  void setAllData()
 {
     if(lists!=null)
     {
         for(int i=0;i<lists.size();i++)
         {
             String title=lists.get(i).get(i).get("title");
             semester_title.add(title);
         }
     }
     if(semester_title!=null)
     {
         arrayAdapter=new ArrayAdapter<String>(ScoreActivity.this,android.R.layout.simple_spinner_item,semester_title);
         arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
         spinner.setAdapter(arrayAdapter);
     }

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

 }*/
private class MyHandler extends Handler {
    @Override
    public void handleMessage(Message msg) {
        switch (msg.what)
        {
            case 1:
                 /*   load_view.setVisibility(View.INVISIBLE);
                    content.setVisibility(View.VISIBLE);*/
                setFirstData();
                break;
            case  2:
                Toast.makeText(ScoreActivity.this,"网络超时",Toast.LENGTH_SHORT).show();
                load_view.setVisibility(View.INVISIBLE);
                content.setVisibility(View.VISIBLE);
                finish();
                break;
            case 3:
                Toast.makeText(ScoreActivity.this,"网络超时",Toast.LENGTH_SHORT).show();
                load_view.setVisibility(View.INVISIBLE);
                content.setVisibility(View.VISIBLE);
                break;
            case 4:
                if(oneSemesterScore!=null)
                {
                    myAdapter.notifyDataSetChanged();
                }
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
        myViewHolder.scorename_view.setText(oneSemesterScore.get(i).course_name);
        myViewHolder.score_view.setText(oneSemesterScore.get(i).grade);
        myViewHolder.credit_view.setText(oneSemesterScore.get(i).credit);
        myViewHolder.couresNature_view.setText(oneSemesterScore.get(i).course_nature);
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
    private  void DealWithFirstScore()
    {
        try {
            viewState= JsonHandle.getViewState(score_html);
            List<String> year=JsonHandle.getYearCount(score_html);
            semesterCount=ScheduleOkHttp.getPostFirstScoreCount(score_url,sessionId,viewState,year,year.size()*2);
            semester_title=Resever(DealWithSemesterCount(semesterCount,year));
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
    private  List<String> DealWithSemesterCount(int semesCount,  List<String> years)
    {
        List<String> semes=new ArrayList<>(Arrays.asList("1","2"));
        List<String> titles=new ArrayList<>();
        for(int j=years.size()-1;j>=0;j--) {
            for (int i = 0; i < semes.size(); i++) {
                String seme_title = years.get(j) + "学年第" + semes.get(i) + "学期学习成绩";
                titles.add(seme_title);
                semesCount -= 1;
                if (semesCount == 0) {
                    return titles;
                }
            }
        }
        return  null;
    }
    private  List<String> Resever(List<String> lists)
    {
        List<String> result=new ArrayList<>();
        for ( int i =lists.size()-1;i>=0;i--) {
            result.add(lists.get(i));
        }
        return  result;
    }

}
