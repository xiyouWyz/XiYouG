package com.example.wyz.xiyoug;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wyz.xiyoug.Model.Book_Collection;
import com.example.wyz.xiyoug.Model.Book_HistoryBor;
import com.example.wyz.xiyoug.Model.HttpLinkHeader;
import com.example.wyz.xiyoug.Util.OkHttpUtil;
import com.example.wyz.xiyoug.View.MyFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wyz on 2016/7/24.
 */
public class MyHistoryBorActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ListView listView;
    private List<Book_HistoryBor> book_historyBors;
    private  MyAdapter myAdapter;
    private  MyThread myThread;
    private  MyHandler myHandler=new MyHandler();
    private  final  String TAG="MyHistoryBorActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_history_bor_page);
        setupViewComponent();
        getListViewData();
    }

    private void setupViewComponent() {
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listView=(ListView) findViewById(R.id.listView);
    }
    private void getListViewData() {
        myThread=new MyThread();
        new Thread(myThread).start();
    }

    private void setListViewData() {
        myAdapter=new MyAdapter();
        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG,"点击了第"+i+"个item");
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }



    private class  MyAdapter  extends BaseAdapter
    {

        @Override
        public int getCount() {
            return book_historyBors.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            MyViewHolder myViewHolder=null;
            if(view==null)
            {
                myViewHolder=new MyViewHolder();
                view= LayoutInflater.from(MyHistoryBorActivity.this).inflate(R.layout.my_history_bor_listview_item,null);
                myViewHolder.title_view=(TextView)view.findViewById(R.id.title);
                myViewHolder.type_view=(TextView)view.findViewById(R.id.type);
                myViewHolder.date_view=(TextView)view.findViewById(R.id.date);
                view.setTag(myViewHolder);
            }
            else
            {
                myViewHolder=(MyViewHolder) view.getTag();
            }
            myViewHolder.title_view.setText(book_historyBors.get(i).getTitle());
            myViewHolder.type_view.setText(book_historyBors.get(i).getType());
            myViewHolder.date_view.setText(book_historyBors.get(i).getDate());

            return  view;
        }
        private   class MyViewHolder
        {
            public TextView title_view;
            public TextView type_view;
            public TextView date_view;
        }
    }
    private  class  MyThread  implements  Runnable
    {

        @Override
        public void run() {
            String url= OkHttpUtil.attachHttpGetParam(HttpLinkHeader.MY_HISTORY_BORROW, "session", MyFragment.SESSIONID);
            try {
                String his_result=OkHttpUtil.getStringFromServer(url);
                Message message=new Message();
                Bundle bundle=new Bundle();
                bundle.putString("his_result",his_result);
                message.setData(bundle);
                myHandler.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private  class  MyHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg) {
            String his_result=msg.getData().getString("his_result");
            if(!his_result.equals(""))
            {
                try {
                    boolean result= new JSONObject(his_result).getBoolean("Result");
                    if(result==true)
                    {
                        String detail=new JSONObject(his_result).getString("Detail");
                        if(detail.equals("NO_RECORD"))
                        {
                            Toast.makeText(MyHistoryBorActivity.this,"没有历史记录",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            JSONArray jsonArray=new JSONArray(detail);
                            book_historyBors=new ArrayList<>();
                            for(int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject=(JSONObject)jsonArray.get(i);
                                Book_HistoryBor book_historyBor=new Book_HistoryBor(
                                        jsonObject.getString("Title"),
                                        jsonObject.getString("Type"),
                                        jsonObject.getString("Date")
                                );
                                book_historyBors.add(book_historyBor);
                            }
                            if(myAdapter!=null)
                            {
                                myAdapter.notifyDataSetChanged();
                            }
                            else
                            {
                                setListViewData();
                            }
                        }
                    }
                    else
                    {
                        Toast.makeText(MyHistoryBorActivity.this,"获取失败",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
