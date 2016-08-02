package com.example.wyz.xiyoug.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wyz.xiyoug.Model.Book_Collection;
import com.example.wyz.xiyoug.Model.HttpLinkHeader;
import com.example.wyz.xiyoug.Model.User;
import com.example.wyz.xiyoug.R;
import com.example.wyz.xiyoug.Util.MyAnimation;
import com.example.wyz.xiyoug.Util.OkHttpUtil;
import com.example.wyz.xiyoug.View.MyFragment;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wyz on 2016/7/24.
 */
public class MyCollectionActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ListView listView;
    private List<Book_Collection> book_collections;
    private  MyAdapter myAdapter;
    private  MyThread myThread;
    private  MyHandler myHandler=new MyHandler();
    private  final  String TAG="MyCollectionActivity";
    private RelativeLayout load_view;
    private  LinearLayout content;
    private  MyLoadHandler myLoadHandler=new MyLoadHandler();
    private  String colDelUrl;
    private  String colDelID;
    private  MyColDelThread myColDelThread;
    private  MyColDelHandler myColDelHandler=new MyColDelHandler();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_col_page);
        setupViewComponent();
        getListViewData();

    }


    @Nullable
    private void setupViewComponent() {
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listView=(ListView) findViewById(R.id.listView);
        content=(LinearLayout)findViewById(R.id.content);
        load_view=(RelativeLayout)findViewById(R.id.loading);
        new MyAnimation(MyCollectionActivity.this, "胖萌正在为您努力加载....", R.drawable.loading, load_view);

    }
    private void getListViewData() {
        myThread=new MyThread();
        new Thread(myThread).start();
    }

    private void setListViewData() {
        myAdapter=new MyAdapter();
        listView.setAdapter(myAdapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
              @Override
              public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                  List<BasicNameValuePair> basicNameValuePairs=new ArrayList<BasicNameValuePair>();
                  basicNameValuePairs.add(new BasicNameValuePair("session",MyFragment.SESSIONID));
                  colDelID=book_collections.get(i).getId();
                  basicNameValuePairs.add(new BasicNameValuePair("id",colDelID));
                  if(User.getInstance().getId().equals(""))
                  {
                      basicNameValuePairs.add(new BasicNameValuePair("username", User.getInstance().getId()));
                  }

                  colDelUrl=OkHttpUtil.attachHttpGetParams(HttpLinkHeader.BOOK_CANCEL_COLLECTION,basicNameValuePairs);
                  dialog();
                  return false;
              }
         });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG,"点击了第"+i+"个item");
                String url=HttpLinkHeader.BOOK_DETAIL_ID+book_collections.get(i).getId();
                Intent intent=new Intent();
                Bundle bundle=new Bundle();
                bundle.putString("url",url);
                intent.putExtras(bundle);
                intent.setClass(MyCollectionActivity.this,BookDetailActivity.class);
                startActivity(intent);
            }
        });
    }
    private  void  dialog()
    {
        DialogInterface.OnClickListener onClickListener=new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i)
                {
                    case  AlertDialog.BUTTON_POSITIVE:
                        myColDelThread=new MyColDelThread();
                        new Thread(myColDelThread).start();
                        break;
                    case  AlertDialog.BUTTON_NEGATIVE:

                        break;
                }
            }
        };
        AlertDialog.Builder builder=new AlertDialog.Builder(MyCollectionActivity.this);
        builder.setTitle("提示");
        builder.setMessage("是否取消收藏?");
        builder.setPositiveButton("确定",onClickListener);
        builder.setNegativeButton("取消",onClickListener);
        builder.create().show();
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
            return book_collections.size();
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
                view= LayoutInflater.from(MyCollectionActivity.this).inflate(R.layout.my_col_listview_item,null);
                myViewHolder.title_view=(TextView)view.findViewById(R.id.title);
                myViewHolder.pub_view=(TextView)view.findViewById(R.id.pub);
                myViewHolder.sort_view=(TextView)view.findViewById(R.id.sort);
                view.setTag(myViewHolder);
            }
            else
            {
                myViewHolder=(MyViewHolder) view.getTag();
            }
            myViewHolder.title_view.setText(book_collections.get(i).getTitle());
            myViewHolder.pub_view.setText(book_collections.get(i).getPub());
            myViewHolder.sort_view.setText(book_collections.get(i).getSort());

            return  view;
        }
        private   class MyViewHolder
        {
            public TextView title_view;
            public TextView pub_view;
            public TextView sort_view;
        }
    }
    private  class  MyThread  implements  Runnable
    {

        @Override
        public void run() {
            String url= OkHttpUtil.attachHttpGetParam(HttpLinkHeader.MY_COLLECTION, "session", MyFragment.SESSIONID);
            try {
                String col_result=OkHttpUtil.getStringFromServer(url);
                Message message=new Message();
                Bundle bundle=new Bundle();
                bundle.putString("col_result",col_result);
                message.setData(bundle);
                myHandler.sendMessage(message);
            } catch (Exception e) {
                Log.d(TAG,e.toString());
                Message message=Message.obtain();
                message.what=2;
                myLoadHandler.sendMessage(message);
            }
        }
    }
    private  class  MyHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg) {
            String col_result=msg.getData().getString("col_result");
            if (col_result != null && !col_result.equals("")) {
                try {
                    boolean result = new JSONObject(col_result).getBoolean("Result");
                    if (result) {
                        String detail = new JSONObject(col_result).getString("Detail");
                        if (detail.equals("NO_RECORD")) {
                            Message message=Message.obtain();
                            message.what=0;
                            myLoadHandler.sendMessage(message);
                        } else {
                            JSONArray jsonArray = new JSONArray(detail);
                            book_collections = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                JSONObject imagesObject=null;
                                try {
                                    imagesObject= new JSONObject(jsonObject.getString("Images"));
                                }catch (JSONException e)
                                {
                                    Log.d(TAG,e.toString());
                                }
                                Book_Collection book_collection=null;
                                if(imagesObject!=null)
                                {
                                    book_collection= new Book_Collection(
                                            jsonObject.getString("Title"),
                                            jsonObject.getString("Pub"),
                                            jsonObject.getString("Sort"),
                                            jsonObject.getString("ID"),
                                            imagesObject.getString("medium")
                                    );
                                }
                                else
                                {
                                     book_collection = new Book_Collection(
                                            jsonObject.getString("Title"),
                                            jsonObject.getString("Pub"),
                                            jsonObject.getString("Sort"),
                                            jsonObject.getString("ID"),
                                            ""
                                    );
                                }

                                book_collections.add(book_collection);
                            }
                            if (myAdapter != null) {
                                myAdapter.notifyDataSetChanged();
                            } else {
                                setListViewData();

                            }
                            Message message = Message.obtain();
                            message.what = 1;
                            myLoadHandler.sendMessage(message);
                        }
                    } else {
                        Message message=Message.obtain();
                        message.what=3;
                        myLoadHandler.sendMessage(message);
                    }
                } catch (JSONException e) {
                    Log.d(TAG,e.toString());
                    Message message=Message.obtain();
                    message.what=3;
                    myLoadHandler.sendMessage(message);
                }
            }
        }
    }
    private  class  MyColDelThread implements  Runnable
    {

        @Override
        public void run() {
            try {
                String addDelResult= OkHttpUtil.getStringFromServer(colDelUrl);
                Message message=new Message();
                Bundle bundle=new Bundle();
                bundle.putString("addDelResult",addDelResult);
                message.setData(bundle);
                myColDelHandler.sendMessage(message);
            } catch (Exception e) {
                Log.d(TAG,e.toString());
                Message message=new Message();
                message.what=2;
                myLoadHandler.sendMessage(message);
            }
        }
    }
    private  class MyColDelHandler extends  Handler
    {
        @Override
        public void handleMessage(Message msg) {
            String addDelResult=msg.getData().getString("addDelResult");
            String detail="";
            try {
                detail =new JSONObject(addDelResult).getString("Detail");
                switch (detail)
                {
                    case  "DELETED_SUCCEED":
                        Toast.makeText(MyCollectionActivity.this,"亲，删除成功",Toast.LENGTH_SHORT).show();
                        DeleteColId();
                        myAdapter.notifyDataSetChanged();

                        break;
                    case "USER_NOT_LOGIN":
                        Toast.makeText(MyCollectionActivity.this,"亲，请先登录",Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(MyCollectionActivity.this,"sorry,删除出错啦",Toast.LENGTH_SHORT).show();
                        break;

                }
            } catch (JSONException e) {
                Log.d(TAG,e.toString());
            }

        }
    }
    private  void DeleteColId()
    {
        for(int i=0;i<book_collections.size();i++){
            if(book_collections.get(i).getId().equals(colDelID))
            {
                book_collections.remove(i);
                break;
            }
        }
    }
    private  class  MyLoadHandler extends  Handler
    {
        @Override
        public void handleMessage(Message msg) {

            if(msg.what==0)
            {
                load_view.setVisibility(View.INVISIBLE);
                content.setVisibility(View.VISIBLE);
                Toast.makeText(MyCollectionActivity.this, "没有收藏记录", Toast.LENGTH_SHORT).show();
            }
            else if(msg.what==1)
            {
                load_view.setVisibility(View.INVISIBLE);
                content.setVisibility(View.VISIBLE);
            }
            else if(msg.what==2)
            {
                Toast.makeText(MyCollectionActivity.this,"请检查网络连接",Toast.LENGTH_SHORT).show();
                MyCollectionActivity.this.finish();
            }
            else  if(msg.what==3)
            {

                Toast.makeText(MyCollectionActivity.this,"请求出错",Toast.LENGTH_SHORT).show();
                MyCollectionActivity.this.finish();
            }
        }
    }
}
