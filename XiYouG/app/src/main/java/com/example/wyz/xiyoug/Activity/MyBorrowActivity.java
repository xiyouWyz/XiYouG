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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wyz.xiyoug.Model.Book_Borrow;
import com.example.wyz.xiyoug.Model.HttpLinkHeader;
import com.example.wyz.xiyoug.R;
import com.example.wyz.xiyoug.RecyclerView.DividerItemDecoration;
import com.example.wyz.xiyoug.RecyclerView.OnItemOnClickListenerInterface;
import com.example.wyz.xiyoug.Util.MyAnimation;
import com.example.wyz.xiyoug.Util.OkHttpUtil;
import com.example.wyz.xiyoug.Viewer.MyFragment;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private  MyHandler myhandler=new MyHandler();
    private List<Book_Borrow> book_borrows;
    private final   String TAG="MyBorrowActivity";
    private String renew_url;
    private LinearLayout content;
    private RelativeLayout load_view;

    private  String renew_barCode;
    private  String renew_department_id;
    private  String renew_library_id;

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
        DividerItemDecoration dividerItemDecoration =new DividerItemDecoration(MyBorrowActivity.this,DividerItemDecoration.VERTICAL_LIST,0);
        recyclerView.addItemDecoration(dividerItemDecoration);
        content=(LinearLayout)findViewById(R.id.content);
        load_view=(RelativeLayout)findViewById(R.id.loading);
        new MyAnimation(MyBorrowActivity.this, "胖萌正在为您努力加载....", R.drawable.loading, load_view);
    }
    private void setupRecyclerView() {

        myAdapter=new MyAdapter();
        myAdapter.setOnItemClickListener(new OnItemOnClickListenerInterface.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {
                String url=HttpLinkHeader.BOOK_DETAIL_BARCODE+book_borrows.get(position).getBarcode();
                Intent intent=new Intent();
                Bundle  bundle=new Bundle();
                bundle.putString("url",url);
                intent.putExtras(bundle);
                intent.setClass(MyBorrowActivity.this,BookDetailActivity.class);
                startActivity(intent);
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
            myViewHolder.state_view.setOnClickListener(new MyRenewOnClickListener(i));
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
    private   class  MyRenewOnClickListener implements View.OnClickListener {

        private  int index;
        public MyRenewOnClickListener(int i) {
            index=i;
        }

        @Override
        public void onClick(View view) {
            List<BasicNameValuePair> basicNameValuePairs=new ArrayList<BasicNameValuePair>();
            renew_barCode=book_borrows.get(index).getBarcode();
            renew_department_id=book_borrows.get(index).getDepartment_id();
            renew_library_id=book_borrows.get(index).getLibrary_id();
            basicNameValuePairs.add(new BasicNameValuePair("session",MyFragment.SESSIONID));
            basicNameValuePairs.add(new BasicNameValuePair("barcode",renew_barCode));
            basicNameValuePairs.add(new BasicNameValuePair("department_id",renew_department_id));
            basicNameValuePairs.add(new BasicNameValuePair("library_id",renew_library_id));
            renew_url=OkHttpUtil.attachHttpGetParams(HttpLinkHeader.BOOK_RENEW,basicNameValuePairs);
          /*  myRenewThread =new MyRenewThread();
            new Thread(myRenewThread).start();*/
        }
    }
    private   class  MyThread implements  Runnable
    {
        @Override
        public void run() {
            String url= OkHttpUtil.attachHttpGetParam(HttpLinkHeader.MY_BORROW,"session", MyFragment.SESSIONID);
            try {
                String bor_result = OkHttpUtil.getStringFromServer(url);
                Message message=new Message();
                Bundle bundle=new Bundle();
                bundle.putString("bor_result",bor_result);
                message.what=1;
                message.setData(bundle);
                myhandler.sendMessage(message);
            } catch (Exception e) {
                Log.d(TAG,e.toString());
                Message message=Message.obtain();
                message.what=3;
                myhandler.sendMessage(message);
            }

        }
    }
    private  class  MyHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==0)
            {
                load_view.setVisibility(View.INVISIBLE);
                content.setVisibility(View.VISIBLE);
                Toast.makeText(MyBorrowActivity.this, "没有借阅记录", Toast.LENGTH_SHORT).show();
            }
            else if(msg.what==1)
            {
                String bor_result=msg.getData().getString("bor_result");
                DealWithBorResult(bor_result);
            }
            else if(msg.what==2)
            {
                load_view.setVisibility(View.INVISIBLE);
                content.setVisibility(View.VISIBLE);
            }
            else if(msg.what==3)
            {
                Toast.makeText(MyBorrowActivity.this,"网络超时",Toast.LENGTH_SHORT).show();
                MyBorrowActivity.this.finish();
            }
            else if(msg.what==4)
            {
                Toast.makeText(MyBorrowActivity.this,"请求出错",Toast.LENGTH_SHORT).show();
                MyBorrowActivity.this.finish();
            }
            else if(msg.what==5)
            {
                String renew_result=msg.getData().getString("renew_result");
                DealWithReNewResult(renew_result);
            }

        }
    }
    private class MyRenewThread implements  Runnable
    {
        @Override
        public void run() {
            try {
                String renew_result=OkHttpUtil.getStringFromServer(renew_url);
                Message message=new Message();
                Bundle bundle=new Bundle();
                bundle.putString("renew_result",renew_result);
                message.setData(bundle);
                message.what=5;
                myhandler.sendMessage(message);
            } catch (Exception e) {
                Log.d(TAG,e.toString());
                Message message=Message.obtain();
                message.what=3;
                myhandler.sendMessage(message);
            }

        }
    }
    private  void UpDataRenew()
    {
        for(int i=0;i<book_borrows.size();i++)
        {
            if(book_borrows.get(i).getBarcode().equals(renew_barCode)
                    &&book_borrows.get(i).getDepartment_id().equals(renew_department_id)
                    &&book_borrows.get(i).getLibrary_id().equals(renew_library_id) )
            {
                book_borrows.get(i).setCanRenew(false);
                break;
            }
        }
    }
    private  void DealWithBorResult(String bor_result)
    {
        Log.d(TAG,bor_result);
        if (bor_result != null && !bor_result.equals("")) {

            try {
                boolean result = new JSONObject(bor_result).getBoolean("Result");
                if (result) {
                    String detail = new JSONObject(bor_result).getString("Detail");
                    if (detail.equals("NO_RECORD")) {
                        already_view.setText(String.valueOf(0));
                        remain_view.setText(String.valueOf(15));
                        continue_view.setText(String.valueOf(0));
                        outTime_view.setText(String.valueOf(0));
                        Message message = Message.obtain();
                        message.what = 0;
                        myhandler.sendMessage(message);
                    } else {
                        JSONArray jsonArray = new JSONArray(detail);
                        book_borrows = new ArrayList<>();
                        int outTime_account = 0;
                        int continue_account = 0;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            Book_Borrow book_borrow = new Book_Borrow(
                                    jsonObject.getString("Title"),
                                    jsonObject.getString("Barcode"),
                                    jsonObject.getString("Department"),
                                    jsonObject.getString("State"),
                                    jsonObject.getString("Date"),
                                    jsonObject.getBoolean("CanRenew"),
                                    jsonObject.getString("Department_id"),
                                    jsonObject.getString("Library_id")
                            );
                            book_borrows.add(book_borrow);
                            if (book_borrow.getState().equals("本馆续借")) {
                                continue_account++;
                            }
                        }
                        if (myAdapter != null) {
                            myAdapter.notifyDataSetChanged();
                        } else {
                            setupRecyclerView();
                        }
                        int bor_account = book_borrows.size();
                        already_view.setText(String.valueOf(bor_account));
                        remain_view.setText(String.valueOf(15 - bor_account));
                        continue_view.setText(String.valueOf(continue_account));
                        outTime_view.setText(String.valueOf(0));
                        Message message = Message.obtain();
                        message.what = 2;
                        myhandler.sendMessage(message);

                    }

                } else {
                    Message message = Message.obtain();
                    message.what = 4;
                    myhandler.sendMessage(message);
                }
            } catch (JSONException e) {
                Message message = Message.obtain();
                message.what = 4;
                myhandler.sendMessage(message);
            }
        }
    }

    private  void DealWithReNewResult(String renew_result)
    {
        try {
            boolean result=new JSONObject(renew_result).getBoolean("Result");
            if(result==true)
            {
                Toast.makeText(MyBorrowActivity.this,"续借成功",Toast.LENGTH_SHORT).show();
                UpDataRenew();
                myAdapter.notifyDataSetChanged();
            }
            else
            {
                Toast.makeText(MyBorrowActivity.this,"续借失败",Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            Log.d(TAG,e.toString());
            Toast.makeText(MyBorrowActivity.this,"续借失败",Toast.LENGTH_SHORT).show();
        }

    }
}
