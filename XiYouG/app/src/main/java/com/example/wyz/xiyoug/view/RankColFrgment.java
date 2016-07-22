/*
package com.example.wyz.xiyoug.View;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wyz.xiyoug.Model.Book_Rank;
import com.example.wyz.xiyoug.R;
import com.example.wyz.xiyoug.Util.DataUtil;
import com.example.wyz.xiyoug.pulltorefreshlistview.PullListView;

import java.util.ArrayList;
import java.util.List;

public class RankColFrgment extends android.support.v4.app.Fragment {
    private PullListView plvData;
    private List<Book_Rank> book_ranks;
    private MyAdapter adapter;
    private Handler handler_pull=new Handler();
    private Handler handler_up=new Handler();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.rank_collection_recyclerview, container, false);
        initData();
        initView(v);
        return v;
    }

    private  void initData()
    {
        book_ranks=new ArrayList<>();
        Book_Rank book_rank =new Book_Rank(1,"看见","33","01h0058748");
        Book_Rank book_rank1=new Book_Rank(1,"看见","33","01h0058748");
        Book_Rank book_rank2=new Book_Rank(1,"看见","33","01h0058748");
        Book_Rank book_rank3=new Book_Rank(1,"看见","33","01h0058748");
        Book_Rank book_rank4=new Book_Rank(1,"看见","33","01h0058748");
        Book_Rank book_rank5=new Book_Rank(1,"看见","33","01h0058748");
        Book_Rank book_rank6=new Book_Rank(1,"看见","33","01h0058748");
        book_ranks.add(book_rank);
        book_ranks.add(book_rank1);
        book_ranks.add(book_rank2);
        book_ranks.add(book_rank3);
        book_ranks.add(book_rank4);
        book_ranks.add(book_rank5);
        book_ranks.add(book_rank6);

    }
    private void initView(View v) {

        plvData = (PullListView) v.findViewById(R.id.plv_data);

        //plvData.addPullHeaderView();
        plvData.setOnRefreshListener(new PullListView.OnRefreshListener() {

            @Override
            public void onRefresh() {
                handler_pull.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getData();
                        Toast.makeText(getContext(),"刷新成功", Toast.LENGTH_SHORT).show();
                    }
                },2000);

                //DataUtil.getData(true, adapter, plvData);

            }
        });

        plvData.setOnGetMoreListener(new PullListView.OnGetMoreListener() {

            @Override
            public void onGetMore() {

                handler_up.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getData1();
                        Toast.makeText(getContext(),"加载成功",Toast.LENGTH_SHORT).show();
                    }
                },2000);
                //DataUtil.getData(false, adapter, plvData);

            }
        });
        adapter=new MyAdapter(getContext(),book_ranks);
        //adapter = new ArrayAdapter<>(getActivity(), R.layout.pull_list_view_item);
        plvData.setAdapter(adapter);

        //plvData.performRefresh();

    }

    private void getData() {
        adapter.notifyDataSetChanged();
        plvData.refreshComplete();

    }
    private  void getData1()
    {
        adapter.notifyDataSetChanged();
        plvData.getMoreComplete();
    }

    private class MyAdapter extends BaseAdapter{
        private  LayoutInflater inflater;
        private  List<Book_Rank>  bRanks;
        public MyAdapter(Context context,List<Book_Rank> objects) {
            this.inflater=LayoutInflater.from(context);
            bRanks=objects;
        }


        @Override
        public int getCount() {
            return bRanks.size();
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
            MyViewHolder holder=null;
            if(view==null)
            {
                holder=new MyViewHolder();
                view=inflater.inflate(R.layout.pull_list_view_item,null);
                holder.rank_book_name=(TextView)view.findViewById(R.id.rank_item_book_name);
                holder.rank_count=(TextView)view.findViewById(R.id.rank_item_count);
                view.setTag(holder);
            }
            else
            {
                holder=(MyViewHolder)view.getTag();
            }
            holder.rank_book_name.setText(bRanks.get(i).getTitle());
            holder.rank_count.setText(bRanks.get(i).getBorNum());
            return view;
        }

        private class MyViewHolder {
            public TextView rank_book_name;
            public TextView rank_count;
        }
    }


}
*/