package com.example.wyz.xiyoug.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wyz.xiyoug.Model.Book_CirculationInfo;
import com.example.wyz.xiyoug.Model.Book_Detail;
import com.example.wyz.xiyoug.Model.Book_ReferBooks;
import com.example.wyz.xiyoug.Model.HttpLinkHeader;
import com.example.wyz.xiyoug.R;
import com.example.wyz.xiyoug.RecyclerView.DividerItemDecoration;
import com.example.wyz.xiyoug.RecyclerView.FullyLinearLayoutManager;
import com.example.wyz.xiyoug.RecyclerView.MyLinearLayoutManager;
import com.example.wyz.xiyoug.RecyclerView.OnItemOnClickListenerInterface;
import com.example.wyz.xiyoug.Util.ImageLoaderPicture;
import com.example.wyz.xiyoug.Util.MyAnimation;
import com.example.wyz.xiyoug.Util.OkHttpUtil;
import com.example.wyz.xiyoug.View.MyFragment;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wyz on 2016/7/24.
 */
public class BookDetailActivity  extends AppCompatActivity{

    private Toolbar toolbar;

    private boolean colIsPress=false;
    private  final  String TAG="BookDetailActivity";
    public  static  String book_url;
    private  MyHandler myHandler=new MyHandler();
    private  MyThread myThread;
    public  static Book_Detail book_detail=null;
    private MyColAddThread myColAddThread;
    private  MyColAddHandler myColAddHandler=new MyColAddHandler();

    private RelativeLayout load_view;
    private  LinearLayout content;
    private  MyLoadHandler myLoadHandler=new MyLoadHandler();
    private NestedScrollView scrollView;

    private ImageView imageView;
    private TextView title_view;
    private  TextView author_view;
    private  TextView pub_view;
    private  TextView sort_view;
    private  TextView pages_view;
    private  TextView avaliable_view;
    private  TextView fav_view;
    private  TextView total_view;
    private  TextView subject_view;
    private  TextView summary_view;
    private  TextView rentTimes_view;

    private RecyclerView  circul_recyclerView;
    private  TextView canBorCount_view;
    private  TextView totalCount_view;
    private  MyCirculAdapter myCirculAdapter;
    LinearLayoutManager linearLayoutManager;
    DividerItemDecoration dividerItemDecoration;
    LinearLayoutManager hor_linearLayoutManager;
    DividerItemDecoration hor_dividerItemDecoration;
   /* private  ListView refer_listView;
    private   MyAdapter myAdapter;*/
    private RecyclerView refer_recyclerView;
    private  MyReferAdapter myReferAdapter;
    private  String addColUrl;
    private  boolean isLoad=false;
    private int pressCount=0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        book_detail=null;
        isLoad=false;
        getBookId();
        setContentView(R.layout.book_detail_page);
        setupViewComponent();
        getData();
       // setFragement();
    }


    private void getBookId() {
        Intent intent=getIntent();
        book_url=intent.getStringExtra("url");
    }
    private void getData() {
        myThread=new MyThread();
        new Thread(myThread).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.book_detail,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_collection:
            {
                pressCount++;
                if(MyFragment.isLogin)
                {
                    //item.setIcon(R.drawable.book_col_press);
                    List<BasicNameValuePair>  basicNameValuePairs=new ArrayList<>();
                    basicNameValuePairs.add(new BasicNameValuePair("session", MyFragment.SESSIONID));
                    if(book_detail!=null)
                    {
                        basicNameValuePairs.add(new BasicNameValuePair("id",book_detail.getId()));
                        addColUrl=OkHttpUtil.attachHttpGetParams(HttpLinkHeader.BOOK_ADD_COLLECTION,basicNameValuePairs);
                        myColAddThread=new MyColAddThread();
                        new Thread(myColAddThread).start();
                    }
                    if(pressCount>7)
                    {
                        Toast.makeText(BookDetailActivity.this,"亲，小爪子点击太快啦",Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(BookDetailActivity.this,"请先登录",Toast.LENGTH_SHORT).show();
                }
                break;
            }


            case R.id.action_share:
                Toast.makeText(BookDetailActivity.this,"点击分享",Toast.LENGTH_SHORT).show();
                break;
            case  android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setupViewComponent() {
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageView=(ImageView)findViewById(R.id.img);
        pub_view=(TextView)findViewById(R.id.pub);
        summary_view=(TextView)findViewById(R.id.summary);
        title_view=(TextView)findViewById(R.id.title);
        pages_view=(TextView)findViewById(R.id.pages);
        author_view=(TextView)findViewById(R.id.author);
        sort_view=(TextView)findViewById(R.id.sort);
        rentTimes_view=(TextView)findViewById(R.id.rentTimes);
        subject_view=(TextView)findViewById(R.id.subject);
        fav_view=(TextView)findViewById(R.id.favTimes);
        total_view=(TextView)findViewById(R.id.total);
        avaliable_view=(TextView)findViewById(R.id.avaliable);
        canBorCount_view=(TextView)findViewById(R.id.canBorCount);
        totalCount_view=(TextView)findViewById(R.id.totalCount);

        circul_recyclerView=(RecyclerView) findViewById(R.id.circul_recyclerView);
        linearLayoutManager=new LinearLayoutManager(BookDetailActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        dividerItemDecoration=new DividerItemDecoration(BookDetailActivity.this,DividerItemDecoration.HORIZONTAL_LIST);
        circul_recyclerView.setLayoutManager(linearLayoutManager);
        circul_recyclerView.addItemDecoration(dividerItemDecoration);

        //refer_listView=(ListView)findViewById(R.id.listView);
        refer_recyclerView=(RecyclerView) findViewById(R.id.refer_cyclerView);
        //不使用自己定义的MyLinearLayoutManager就无法显示，wrap_content，
        hor_linearLayoutManager=new MyLinearLayoutManager(BookDetailActivity.this);
        hor_linearLayoutManager.setOrientation(FullyLinearLayoutManager.VERTICAL);

        //hor_linearLayoutManager=new LinearLayoutManager(BookDetailActivity.this);
        //hor_linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        refer_recyclerView.setLayoutManager(hor_linearLayoutManager);
        //添加分割线就会出现recyclerView显示不全的问题
      /*  hor_dividerItemDecoration = new DividerItemDecoration(BookDetailActivity.this, DividerItemDecoration.VERTICAL_LIST);
        refer_recyclerView.addItemDecoration(hor_dividerItemDecoration);*/





        load_view=(RelativeLayout)findViewById(R.id.loading);
        content=(LinearLayout) findViewById(R.id.content);
        new MyAnimation(BookDetailActivity.this,"胖萌正在为您努力加载....", R.drawable.loading,load_view);

    }
    private void setCirculRecyclerViewData() {
        myCirculAdapter = new MyCirculAdapter();
        //实现接口setOnItemClickLitener
        myCirculAdapter.setOnItemClickListener(new OnItemOnClickListenerInterface.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {

            }

            @Override
            public void OnItemLongClick(View view, int position) {

            }
        });

        //设置适配器
        circul_recyclerView.setAdapter(myCirculAdapter);
        //设置动画
        circul_recyclerView.setItemAnimator(new DefaultItemAnimator());
        //隐藏滚动条横向的
        circul_recyclerView.setHorizontalScrollBarEnabled(false);
    }
  /*  private  void setReferListViewData()
    {
        myAdapter=new MyAdapter();
        refer_listView.setAdapter(myAdapter);
    }*/
    private void setReferRecyclerViewData() {
        myReferAdapter = new MyReferAdapter();
        myReferAdapter.setOnItemClickListener(new OnItemOnClickListenerInterface.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int position) {

            }

            @Override
            public void OnItemLongClick(View view, int position) {
                Log.d(TAG,"点击了第"+position+"个item");
                String url=HttpLinkHeader.BOOK_DETAIL_ID+ BookDetailActivity.book_detail.getBook_referBookses().get(position).getId();
                Intent intent=new Intent();
                Bundle bundle=new Bundle();
                bundle.putString("url",url);
                intent.putExtras(bundle);
                //intent.setClass(getContext(),BookDetailActivity.class);
                startActivity(intent);
            }
        });
        //设置适配器
        refer_recyclerView.setAdapter(myReferAdapter);
        //设置动画
        refer_recyclerView.setItemAnimator(new DefaultItemAnimator());
        //隐藏滚动条横向的
        //refer_recyclerView.setHorizontalScrollBarEnabled(false);
        refer_recyclerView.setVerticalScrollBarEnabled(false);
    }
    private  class  MyThread implements  Runnable
    {

        @Override
        public void run() {
            try {
                String book_result=OkHttpUtil.getStringFromServer(book_url);
                Message message=new Message();
                Bundle bundle=new Bundle();
                bundle.putString("book_result",book_result);
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
    private class MyHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg) {
            String book_result=msg.getData().getString("book_result");
            book_detail=null;
            try {
                boolean result=new JSONObject(book_result).getBoolean("Result");
                if(result)
                {
                    String detail=new JSONObject(book_result).getString("Detail");
                    if(detail.equals("NO_RECORD"))
                    {
                        Message message=Message.obtain();
                        message.what=0;
                        myLoadHandler.sendMessage(message);
                    }
                    else
                    {
                        JSONArray circulArray=new JSONArray(new JSONObject(detail).getString("CirculationInfo"));
                        List<Book_CirculationInfo> book_circulationInfos=new ArrayList<>();
                        int j=0,canBorCount=0,totalCount=circulArray.length();
                        for(int i=0;i<circulArray.length();i++)
                        {
                            JSONObject circulObject=(JSONObject) circulArray.get(i);
                            Book_CirculationInfo book_circulationInfo=new Book_CirculationInfo(
                                    circulObject.getString("Barcode"),
                                    circulObject.getString("Sort"),
                                    circulObject.getString("Department"),
                                    circulObject.getString("Status"),
                                    circulObject.getString("Date")
                            );
                            if(book_circulationInfo.getStatue().equals("在架可借"))
                            {
                                book_circulationInfos.add(j,book_circulationInfo);
                                j++;
                                canBorCount++;
                            }
                            else{
                                book_circulationInfos.add(book_circulationInfo);
                            }

                        }
                        JSONArray referArray=new JSONArray(new JSONObject(detail).getString("ReferBooks"));
                        List<Book_ReferBooks> book_referBookses=new ArrayList<>();
                        for(int i=0;i<referArray.length();i++)
                        {
                            JSONObject referObject=(JSONObject)referArray.get(i);
                            Book_ReferBooks book_referBooks=new Book_ReferBooks(
                                    referObject.getString("ID"),
                                    referObject.getString("Title"),
                                    referObject.getString("Author")
                            );
                            book_referBookses.add(book_referBooks);
                        }
                        String doubanInfo=new JSONObject(detail).getString("DoubanInfo");
                        String image="";
                        if(!doubanInfo.equals("null"))
                        {
                            String images=new JSONObject(doubanInfo).getString("Images");
                            if(!images.equals(""))
                            {
                                image=new JSONObject(images).getString("medium");
                            }
                        }
                        JSONObject jsonObject=new JSONObject(detail);
                        book_detail=new Book_Detail(
                                jsonObject.getString("ID"),
                                jsonObject.getString("Pub"),
                                jsonObject.getString("Summary"),
                                jsonObject.getString("Title"),
                                jsonObject.getString("Form"),
                                jsonObject.getString("Author"),
                                jsonObject.getString("Sort"),
                                jsonObject.getString("Subject"),
                                jsonObject.getString("RentTimes"),
                                jsonObject.getString("FavTimes"),
                                jsonObject.getString("BrowseTimes"),
                                jsonObject.getString("Total"),
                                jsonObject.getString("Avaliable"),
                                book_circulationInfos,
                                book_referBookses,
                                image
                        );
                        pub_view.setText(book_detail.getPub());
                        summary_view.setText(book_detail.getSummary());
                        title_view.setText(book_detail.getTitle());
                        pages_view.setText(book_detail.getForm());
                        author_view.setText(book_detail.getAuthor());
                        sort_view.setText(book_detail.getSort());
                        subject_view.setText(book_detail.getSubject());
                        rentTimes_view.setText(book_detail.getRentTimes());
                        fav_view.setText(book_detail.getFavTimes());
                        total_view.setText(book_detail.getTotal());
                        avaliable_view.setText(book_detail.getAvaliable());
                        canBorCount_view.setText(String.valueOf(canBorCount));
                        totalCount_view.setText(String.valueOf(totalCount));
                        if(image!="")
                        {
                            ImageLoader.getInstance().displayImage(image,imageView,new ImageLoaderPicture(BookDetailActivity.this).getOptions(),new SimpleImageLoadingListener());
                        }
                        else
                        {
                            imageView.setImageResource(R.drawable.book_detail_img);
                        }

                        setCirculRecyclerViewData();
                        setReferRecyclerViewData();
                        isLoad=true;
                        Message message=new Message();
                        message.what=1;
                        myLoadHandler.sendMessage(message);
                    }
                }
            } catch (JSONException e) {
               Message message=Message.obtain();
                message.what=3;
                myLoadHandler.sendMessage(message);
            }
        }
    }
    private  class  MyColAddThread implements  Runnable
    {

        @Override
        public void run() {
            try {
                String addColResult= OkHttpUtil.getStringFromServer(addColUrl);
                Message message=new Message();
                Bundle bundle=new Bundle();
                bundle.putString("addColResult",addColResult);
                message.setData(bundle);
                myColAddHandler.sendMessage(message);
            } catch (Exception e) {
                Log.d(TAG,e.toString());
                Message message=new Message();
                message.what=2;
                myLoadHandler.sendMessage(message);
            }
        }
    }
    private  class MyColAddHandler extends  Handler
    {
        @Override
        public void handleMessage(Message msg) {
            String addColResult=msg.getData().getString("addColResult");
            String detail="";
            try {
                detail =new JSONObject(addColResult).getString("Detail");
                switch (detail)
                {
                    case  "ADDED_SUCCEED":

                        Toast.makeText(BookDetailActivity.this,"亲，收藏成功",Toast.LENGTH_SHORT).show();
                        break;
                    case  "ALREADY_IN_FAVORITE":
                        Toast.makeText(BookDetailActivity.this,"亲，已经收藏过啦",Toast.LENGTH_SHORT).show();
                        break;
                    case "USER_NOT_LOGIN":
                        Toast.makeText(BookDetailActivity.this,"亲，请先登录",Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(BookDetailActivity.this,"sorry，收藏失败啦",Toast.LENGTH_SHORT).show();
                        break;

                }
            } catch (JSONException e) {
                Log.d(TAG,e.toString());
            }

        }
    }

    private  class  MyCirculAdapter extends  RecyclerView.Adapter<MyCirculAdapter.MyViewHolder> {
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

            MyViewHolder holder=new MyViewHolder(LayoutInflater.from(BookDetailActivity.this).inflate(R.layout.bookcircul_listview_item,null));
            return  holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder myViewHolder, int i) {
            myViewHolder.barcode_view.setText(book_detail.getBook_circulationInfos().get(i).getBarcode());
            myViewHolder.sort_view.setText(book_detail.getBook_circulationInfos().get(i).getSort());
            myViewHolder.department_view.setText(book_detail.getBook_circulationInfos().get(i).getDepartment());
            String status=book_detail.getBook_circulationInfos().get(i).getStatue();
            myViewHolder.status_view.setText(status);
            if(status.equals("在架可借"))
            {
                myViewHolder.status_view.setTextColor(getResources().getColor(R.color.book_circul_status_canBor));
            }
            else if(status.equals("本馆借出"))
            {
                myViewHolder.status_view.setTextColor(getResources().getColor(R.color.book_circul_status_alrBor));
            }
            else
            {
                myViewHolder.status_view.setTextColor(getResources().getColor(R.color.book_circul_status_reBor));
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

                }
            });

        }

        @Override
        public int getItemCount() {

            if(book_detail!=null)
            {
                if(book_detail.getBook_circulationInfos()!=null)
                {
                    return book_detail.getBook_circulationInfos().size();
                }
            }
            return 0;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            public TextView barcode_view;
            public TextView sort_view;
            public TextView department_view;
            public   TextView status_view;
            public MyViewHolder(View itemView) {
                super(itemView);
                barcode_view=(TextView) itemView.findViewById(R.id.barcode);
                sort_view=(TextView) itemView.findViewById(R.id.sort);
                department_view=(TextView) itemView.findViewById(R.id.department);
                status_view=(TextView) itemView.findViewById(R.id.status);
            }
        }
        private OnItemOnClickListenerInterface.OnItemClickListener onItemClickListener;


        public void setOnItemClickListener(OnItemOnClickListenerInterface.OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }
    }

    private  class  MyReferAdapter extends  RecyclerView.Adapter<MyReferAdapter.MyViewHolder> {
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

            MyViewHolder holder=new MyViewHolder(LayoutInflater.from(BookDetailActivity.this).inflate(R.layout.bookrefer_listview_item,null));
            return  holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder myViewHolder, int i) {
            myViewHolder.title_view.setText(book_detail.getBook_referBookses().get(i).getTitle());
            myViewHolder.author_view.setText(book_detail.getBook_referBookses().get(i).getAuthor());
            myViewHolder.id_view.setText(book_detail.getBook_referBookses().get(i).getId());

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

            if(book_detail!=null)
            {
                if(book_detail.getBook_referBookses()!=null)
                {
                    return book_detail.getBook_referBookses().size();
                }
            }
            return 0;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            public TextView title_view;
            public TextView author_view;
            public TextView id_view;
            public MyViewHolder(View itemView) {
                super(itemView);
                title_view=(TextView) itemView.findViewById(R.id.title);
                author_view=(TextView) itemView.findViewById(R.id.author);
                id_view=(TextView) itemView.findViewById(R.id.id);
            }
        }
        private OnItemOnClickListenerInterface.OnItemClickListener onItemClickListener;


        public void setOnItemClickListener(OnItemOnClickListenerInterface.OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }
    }

    private class  MyAdapter  extends BaseAdapter
    {

        @Override
        public int getCount() {
            return  book_detail.getBook_referBookses().size();
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
                view= LayoutInflater.from(BookDetailActivity.this).inflate(R.layout.bookrefer_listview_item,null);
                myViewHolder.title_view=(TextView) view.findViewById(R.id.title);
                myViewHolder.author_view=(TextView)view.findViewById(R.id.author);
                myViewHolder.id_view=(TextView)view.findViewById(R.id.id);
                view.setTag(myViewHolder);
            }
            else
            {
                myViewHolder=(MyViewHolder) view.getTag();
            }
            myViewHolder.title_view.setText(book_detail.getBook_referBookses().get(i).getTitle());
            myViewHolder.author_view.setText(book_detail.getBook_referBookses().get(i).getAuthor());
            myViewHolder.id_view.setText(book_detail.getBook_referBookses().get(i).getId());

            return  view;
        }
        private   class MyViewHolder
        {
            public TextView title_view;
            public TextView author_view;
            public TextView id_view;
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
                Toast.makeText(BookDetailActivity.this, "没有记录", Toast.LENGTH_SHORT).show();
                BookDetailActivity.this.finish();
            }
            else if(msg.what==1)
            {
                load_view.setVisibility(View.INVISIBLE);
                content.setVisibility(View.VISIBLE);
            }
            else if(msg.what==2)
            {
                Toast.makeText(BookDetailActivity.this,"网络不稳定，请检查网络",Toast.LENGTH_SHORT).show();
                BookDetailActivity.this.finish();
            }
            else  if(msg.what==3)
            {

                Toast.makeText(BookDetailActivity.this,"请求出错",Toast.LENGTH_SHORT).show();
                BookDetailActivity.this.finish();
            }
        }
    }
}
