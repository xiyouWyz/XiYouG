package com.example.wyz.xiyoug.Viewer;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.wyz.xiyoug.R;

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

    private  String TAG="InfoFragment";
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.info_page,container,false);
        Log.d("Fragment","InfoFragment加载");
        Log.d(TAG,"InfoFragment+onCreateView");
        setupViewComponent();
        return  view;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"InfoFragment+onCreate");

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG,"InfoFragment+onAttach");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG,"InfoFragment+onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"InfoFragment+onResume");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG,"InfoFragment+onActivityCreated");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d(TAG,"InfoFragment+setUserVisibleHint");
        Log.d(TAG,"InfoFragment+setUserVisibleHint     "+isVisibleToUser);
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
        transaction.commitAllowingStateLoss();
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
