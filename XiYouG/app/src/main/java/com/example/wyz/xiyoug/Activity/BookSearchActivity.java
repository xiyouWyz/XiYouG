package com.example.wyz.xiyoug.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wyz.xiyoug.Model.Book_Search;
import com.example.wyz.xiyoug.Model.HttpLinkHeader;
import com.example.wyz.xiyoug.R;
import com.example.wyz.xiyoug.Util.MyAnimation;
import com.example.wyz.xiyoug.Util.OkHttpUtil;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wyz on 2016/7/25.
 */
public class BookSearchActivity extends AppCompatActivity  implements SearchView.OnQueryTextListener{
    private Toolbar toolbar;
    private SearchView searchView;
    private ListView listView;
    private List<Book_Search> book_searches;
    private  MyAdapter myAdapter;
    private MyThread myThread=new MyThread();
    private MyHandler myHandler=new MyHandler();
    private  String url;
    private  final  String TAG="BookSearchActivity";
    private LinearLayout content;
    private RelativeLayout load_view;
    private final int  ACTIVITY_RESULT_SCAN = 1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_search_page);
        setupViewComponent();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.scan,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.scan:
            {
                Intent intent2 = new Intent(BookSearchActivity.this,ScanActivity.class);
                startActivityForResult(intent2, ACTIVITY_RESULT_SCAN,null);
                break;
            }
            case  android.R.id.home:
            {
                finish();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
/*    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTIVITY_RESULT_SCAN){
            if (data != null){
                String result = data.getStringExtra("result");
                Log.d(TAG,result);
                //mResultTextView.setText(result);
                Bitmap barcode = null;
                byte[] compressedBitmap = data.getByteArrayExtra("resultByte");
                if (compressedBitmap != null) {
                    barcode = BitmapFactory.decodeByteArray(compressedBitmap, 0, compressedBitmap.length, null);
                    barcode = barcode.copy(Bitmap.Config.RGB_565, true);

                }

            }else {
                Toast.makeText(BookSearchActivity.this,"没有结果!!!",Toast.LENGTH_SHORT).show();
            }
        }
    }*/
    private void setupViewComponent() {
        toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        searchView=(SearchView)findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(this);

        listView=(ListView)findViewById(R.id.listView);


        content=(LinearLayout)findViewById(R.id.content);
        load_view=(RelativeLayout)findViewById(R.id.loading);
        load_view.setVisibility(View.INVISIBLE);


    }
    private  void setupListView()
    {
        myAdapter=new MyAdapter();
        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG,"点击了搜索的第"+i+"个item");
                String url=HttpLinkHeader.BOOK_DETAIL_ID+book_searches.get(i).getId();
                Intent intent=new Intent();
                Bundle bundle=new Bundle();
                bundle.putString("url",url);
                intent.putExtras(bundle);
                intent.setClass(BookSearchActivity.this,BookDetailActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if(!newText.equals(""))
        {
            List<BasicNameValuePair> basicNameValuePairs=new ArrayList<>();
            basicNameValuePairs.add(new BasicNameValuePair("matchMethod","qx"));
            basicNameValuePairs.add(new BasicNameValuePair("keyword",newText));

            url= OkHttpUtil.attachHttpGetParams(HttpLinkHeader.BOOK_SEARCH,basicNameValuePairs);
            load_view.setVisibility(View.VISIBLE);
            content.setVisibility(View.INVISIBLE);
            new MyAnimation(BookSearchActivity.this, "胖萌正在为您努力加载....", R.drawable.loading, load_view);

            new Thread(myThread).start();
        }
        else
        {
            if(book_searches!=null)
            {
                book_searches.clear();
                myAdapter.notifyDataSetChanged();
            }


        }
        return  false;
    }



    private  class  MyAdapter extends BaseAdapter
    {

        @Override
        public int getCount() {
            return book_searches.size();
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
                view= LayoutInflater.from(BookSearchActivity.this).inflate(R.layout.book_search_listview_item,null);
                myViewHolder.title_view=(TextView)view.findViewById(R.id.title);
                myViewHolder.author_view=(TextView)view.findViewById(R.id.author);
                view.setTag(myViewHolder);
            }
            else
            {
                myViewHolder=(MyViewHolder) view.getTag();
            }
            myViewHolder.title_view.setText(book_searches.get(i).getTitle());
            myViewHolder.author_view.setText(book_searches.get(i).getAuthor());
            return view;
        }
        public  class  MyViewHolder
        {
            public TextView title_view;
            private  TextView author_view;
        }
    }
    private   class  MyThread implements  Runnable
    {

        @Override
        public void run() {

            try {
                String sea_result = OkHttpUtil.getStringFromServer(url);
                Message message=new Message();
                Bundle bundle=new Bundle();
                bundle.putString("sea_result",sea_result);
                message.setData(bundle);
                message.what=1;
                myHandler.sendMessage(message);
            } catch (Exception e) {
                Message message=Message.obtain();
                message.what=3;
                myHandler.sendMessage(message);
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
                if(book_searches!=null)
                {
                    book_searches.clear();
                    myAdapter.notifyDataSetChanged();
                }
            }
            if(msg.what==1)
            {
                String sea_result=msg.getData().getString("sea_result");
                DealWithSeaResult(sea_result);
            }
            else if(msg.what==2)
            {
                load_view.setVisibility(View.INVISIBLE);
                content.setVisibility(View.VISIBLE);
            }
            else if(msg.what==3)
            {
                Toast.makeText(BookSearchActivity.this,"网络超时",Toast.LENGTH_SHORT).show();
                load_view.setVisibility(View.INVISIBLE);
                content.setVisibility(View.VISIBLE);
                //BookSearchActivity.this.finish();
            }
            else if(msg.what==4)
            {
                Toast.makeText(BookSearchActivity.this,"请求出错",Toast.LENGTH_SHORT).show();
                //BookSearchActivity.this.finish();
            }

        }
    }


    private  void DealWithSeaResult(String sea_result)
    {
        Log.d(TAG,sea_result);
        if (sea_result != null &&!sea_result.equals("") ) {

            try {
                boolean result = new JSONObject(sea_result).getBoolean("Result");
                Log.d(TAG, result + "");
                if (result) {
                    String detail = new JSONObject(sea_result).getString("Detail");
                    if (detail.equals("NO_RECORD")) {
                        Message message = Message.obtain();
                        message.what = 0;
                        myHandler.sendMessage(message);

                    } else {
                        String bookData = new JSONObject(detail).getString("BookData");
                        JSONArray jsonArray = new JSONArray(bookData);
                        book_searches = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            Book_Search book_search = new Book_Search(
                                    jsonObject.getString("ID"),
                                    jsonObject.getString("Title"),
                                    jsonObject.getString("Author")
                            );
                            book_searches.add(book_search);
                        }
                        if (myAdapter != null) {
                            myAdapter.notifyDataSetChanged();
                        } else {
                            setupListView();
                        }
                        Message message = new Message();
                        message.what = 2;
                        myHandler.sendMessage(message);
                    }
                } else {
                    Message message = Message.obtain();
                    message.what = 4;
                    myHandler.sendMessage(message);
                }
            } catch (JSONException e) {
                Message message = Message.obtain();
                message.what = 4;
                myHandler.sendMessage(message);
            }

        }
    }
}
