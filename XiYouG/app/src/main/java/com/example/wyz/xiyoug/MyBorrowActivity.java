package com.example.wyz.xiyoug;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompatSideChannelService;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wyz.xiyoug.Model.Book_Borrow;
import com.example.wyz.xiyoug.Model.HttpLinkHeader;
import com.example.wyz.xiyoug.RecyclerView.DividerItemDecoration;
import com.example.wyz.xiyoug.RecyclerView.OnItemOnClickListenerInterface;
import com.example.wyz.xiyoug.Util.OkHttpUtil;
import com.example.wyz.xiyoug.View.MyFragment;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wyz on 2016/7/23.
 */
public class MyBorrowActivity  extends AppCompatActivity
{
    private  Toolbar toolbar;
    private  TextView already_view;
    private  TextView remain_view;
    private  TextView continue_view;
    private  TextView outTime_view;
    private  RecyclerView recyclerView;
    private  MyAdapter myAdapter;
    private  MyThread myThread;
    private  MyRenewThread myRenewThread;
    private  MyRenewHandler myRenewHandler;
    private  MyHandler myhandler=new MyHandler();
    private List<Book_Borrow> book_borrows;
    private final   String TAG="MyBorrowActivity";
    private String renew_url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_bor_page);
        setupViewComponent();
        getData();


    }


    private void setupViewComponent() {
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        already_view=(TextView)findViewById(R.id.already_number);
        remain_view=(TextView)findViewById(R.id.remain_number);
        continue_view=(TextView)findViewById(R.id.continue_number);
        outTime_view=(TextView)findViewById(R.id.outTime_number);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(MyBorrowActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration =new DividerItemDecoration(MyBorrowActivity.this,DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(dividerItemDecoration);

    }
    private void setupRecyclerView() {

        myAdapter=new MyAdapter();
        myAdapter.setOnItemClickListener(new OnItemOnClickListenerInterface.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {

                List<BasicNameValuePair> basicNameValuePairs=new ArrayList<BasicNameValuePair>();
                basicNameValuePairs.add(new BasicNameValuePair("session",MyFragment.SESSIONID));
                basicNameValuePairs.add(new BasicNameValuePair("barcode",book_borrows.get(position).getBarcode()));
                basicNameValuePairs.add(new BasicNameValuePair("department_id",book_borrows.get(position).getDepartment_id()));
                basicNameValuePairs.add(new BasicNameValuePair("library_id",book_borrows.get(position).getLibrary_id()));
                renew_url=OkHttpUtil.attachHttpGetParams(HttpLinkHeader.BOOK_RENEW,basicNameValuePairs);

                Log.d(TAG,"点击了第"+position+"个item");
            }

            @Override
            public void OnItemLongClick(View view, int position) {
                Log.d(TAG,"长按了第"+position+"个item");
            }
        });
        recyclerView.setAdapter(myAdapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getData() {
        myThread=new MyThread();
        new Thread(myThread).start();
    }
    private  class  MyAdapter extends  RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

            MyViewHolder holder=new MyViewHolder(LayoutInflater.from(MyBorrowActivity.this).inflate(R.layout.my_bor_listview_item,null));
            return  holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder myViewHolder, int i) {
            myViewHolder.title_view.setText(book_borrows.get(i).getTitle());
            myViewHolder.department_view.setText(book_borrows.get(i).getDepartment());
            myViewHolder.date_view.setText(book_borrows.get(i).getDate());
            myViewHolder.state_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG,"点击了按钮");
                }
            });
            if(book_borrows.get(i).getCanRenew()==true)
            {
                myViewHolder.state_view.setText("我要续借");
            }
            else
            {
                myViewHolder.state_view.setText("已经续借");
            }
            //实现长按事件
            myViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    return false;
                }
            });
            //实现点击事件
            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos=myViewHolder.getPosition();
                    //Log.d(TAG,"点击了第"+pos+"个item");
                    onItemClickListener.OnItemClick(view,pos);
                }
            });

        }

        @Override
        public int getItemCount() {

            if(book_borrows!=null)
            {
                return book_borrows.size();
            }
            return 0;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            private  TextView title_view;
            private  TextView department_view;
            private  TextView date_view;
            private  TextView state_view;

            public MyViewHolder(View itemView) {
                super(itemView);
                title_view=(TextView) itemView.findViewById(R.id.title);
                department_view=(TextView) itemView.findViewById(R.id.department);
                date_view=(TextView) itemView.findViewById(R.id.date);
                state_view=(TextView) itemView.findViewById(R.id.state);
            }
        }
        private OnItemOnClickListenerInterface.OnItemClickListener onItemClickListener;


        public void setOnItemClickListener(OnItemOnClickListenerInterface.OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }
    }
    public  class  MyThread implements  Runnable
    {

        @Override
        public void run() {
            String url= OkHttpUtil.attachHttpGetParam(HttpLinkHeader.MY_BORROW,"session", MyFragment.SESSIONID);

            try {
                String bor_result = OkHttpUtil.getStringFromServer(url);
                Message message=new Message();
                Bundle bundle=new Bundle();
                bundle.putString("bor_result",bor_result);
                message.setData(bundle);
                myhandler.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    private  class  MyHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg) {
            String bor_result=msg.getData().getString("bor_result");
            Log.d(TAG,bor_result);
            if(!bor_result.equals(""))
            {

                try {
                    boolean  result = new JSONObject(bor_result).getBoolean("Result");
                    Log.d(TAG,result+"");
                    if(result==true)
                    {
                        String detail=new JSONObject(bor_result).getString("Detail");
                        if(detail.equals("NO_RECORD"))
                        {
                            already_view.setText(String.valueOf(0));
                            remain_view.setText(String.valueOf(15));
                            continue_view.setText(String.valueOf(0));
                            outTime_view.setText(String.valueOf(0));
                            Toast.makeText(MyBorrowActivity.this,"没有借阅记录",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            JSONArray jsonArray=new JSONArray(detail);
                            book_borrows=new ArrayList<>();
                            int outTime_account=0;
                            int continue_account=0;
                            for(int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject=(JSONObject)jsonArray.get(i);
                                Book_Borrow book_borrow=new Book_Borrow(
                                        jsonObject.getString("Title"),
                                        jsonObject.getString("Barcode"),
                                        jsonObject.getString("Department"),
                                        jsonObject.getString("State"),
                                        jsonObject.getString("Date"),
                                        jsonObject.getBoolean("CanRenew"),
                                        jsonObject.getString("Department_id "),
                                        jsonObject.getString("Library_id ")
                                );
                                book_borrows.add(book_borrow);
                                if(book_borrow.getState().equals("本馆续借"))
                                {
                                    continue_account++;
                                }
                            }
                            if(myAdapter!=null)
                            {
                                myAdapter.notifyDataSetChanged();
                            }
                            else
                            {
                                setupRecyclerView();
                            }
                            int bor_account=book_borrows.size();
                            already_view.setText(String.valueOf(bor_account));
                            remain_view.setText(String.valueOf(15-bor_account));
                            continue_view.setText(String.valueOf(continue_account));
                            outTime_view.setText(String.valueOf(0));

                        }

                    }
                    else{
                        Toast.makeText(MyBorrowActivity.this,"请求失败",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }
    public class MyRenewThread implements  Runnable
    {

        @Override
        public void run() {
            try {
                String renew_result=OkHttpUtil.getStringFromServer(renew_url);
                Message message=new Message();
                Bundle bundle=new Bundle();
                bundle.putString("renew_result",renew_result);
                message.setData(bundle);
                myRenewHandler.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    private class  MyRenewHandler extends  Handler
    {
        @Override
        public void handleMessage(Message msg) {
            String renew_result=msg.getData().getString("renew_result");
            try {
                boolean result=new JSONObject(renew_result).getBoolean("Result");
                if(result==true)
                {
                    Toast.makeText(MyBorrowActivity.this,"续借成功",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(MyBorrowActivity.this,"续借失败",Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
