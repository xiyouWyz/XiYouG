package com.example.wyz.xiyoug.View;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wyz.xiyoug.Model.Book_Rank;
import com.example.wyz.xiyoug.Model.News;
import com.example.wyz.xiyoug.R;
import com.example.wyz.xiyoug.Util.OkHttpUtil;
import com.example.wyz.xiyoug.pulltorefreshlistview.PullListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wyz on 2016/7/17.
 */
public class InfoFragment extends Fragment {
    private  View view;
    private TextView info_news;
    private TextView info_notice;
    private NewsFragment newsFragment;
    private NoticeFragment noticeFragment;
    private FragmentManager fragmentManager;

    private  boolean isFirst=false;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.info_page,container,false);
        setupViewComponent();
        return  view;
    }

    private void setupViewComponent() {
       
        info_news=(TextView)view.findViewById(R.id.info_news);
        info_news.setOnClickListener(new MyNewsOnClickListener());
        info_notice=(TextView)view.findViewById(R.id.info_notice);
        info_notice.setOnClickListener(new MyNoticeOnClickListener());
        fragmentManager=getFragmentManager();
        setFragmentSelect(0);
    }

    private void setFragmentSelect(int i) {
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        hideFragments(transaction);
        switch (i)
        {
            case 0:
                if(newsFragment==null)
                {
                    newsFragment=new NewsFragment();
                    transaction.add(R.id.frameLayout,newsFragment);
                }
                else
                {
                    transaction.show(newsFragment);
                }
                break;
            case 1:
                if(noticeFragment==null)
                {
                    noticeFragment=new NoticeFragment();
                    transaction.add(R.id.frameLayout,noticeFragment);
                }
                else
                {
                    transaction.show(noticeFragment);
                }
                break;
        }
        transaction.commit();
    }

    private void hideFragments(FragmentTransaction transaction) {
        if(newsFragment!=null)
        {
            transaction.hide(newsFragment);
        }
        if(noticeFragment!=null)
        {
            transaction.hide(noticeFragment);
        }
    }


    private class MyNewsOnClickListener implements  View.OnClickListener{

        @Override
        public void onClick(View view) {
            view.setBackgroundColor(getResources().getColor(R.color.info_news_back_press));
            info_notice.setBackgroundColor(getResources().getColor(R.color.info_notice_back));
            setFragmentSelect(0);
        }
    }
    private class  MyNoticeOnClickListener implements  View.OnClickListener{

        @Override
        public void onClick(View view) {
            view.setBackgroundColor(getResources().getColor(R.color.info_notice_back_press));
            info_news.setBackgroundColor(getResources().getColor(R.color.info_news_back));
            setFragmentSelect(1);

        }
    }
}
