package com.example.wyz.xiyoug.Viewer;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.wyz.xiyoug.R;
import com.yolanda.nohttp.NoHttp;

/**
 * Created by Wyz on 2016/8/31.
 */
public class ClassRoomFragment extends Fragment {
     View view;
    private TextView title_textView;
    private WebView webView;
    //private  MyThread myThread;
    private FrameLayout mFrameLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.class_room,container,false);
        NoHttp.init(getActivity().getApplication());
        setupViewComponent1();
        System.out.println(android.os.HandlerThread.currentThread().getName());
        System.out.println(android.os.HandlerThread.currentThread().getId());
        System.out.println(android.os.Process.myPid());
        //setupViewComponent();
        return view;
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.d("ClassRoomFragment", "onPause");
    }
    private void setupViewComponent1() {
        mFrameLayout=(FrameLayout)view.findViewById(R.id.frameLayout);
        webView=new WebView(getActivity().getApplicationContext());
        mFrameLayout.addView(webView);
        //webView=(WebView)view.findViewById(R.id.webView);
        //支持javascript
        webView.getSettings().setJavaScriptEnabled(true);
        // 设置可以支持缩放
        webView.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        webView.getSettings().setBuiltInZoomControls(true);
        //扩大比例的缩放
        webView.getSettings().setUseWideViewPort(true);
        //自适应屏幕
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.loadUrl("http://jwkq.xupt.edu.cn:8080/");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        webView.removeAllViews();
        webView.destroy();
    }
/*
    private void setupViewComponent() {

        title_textView=(TextView)view.findViewById(R.id.title);
        Date date=new Date();
        String title="今日("+String.valueOf(date.getMonth()+1)+"月"+date.getDate()+"日"+")教室使用情况";
        title_textView.setText(title);

        webView=(WebView)view.findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        if(!IsNetworkConnected.isNetworkConnected(getContext()))
        {
            Message message=Message.obtain();
            message.what=2;
            handler.sendMessage(message);
        }
        else
        {
            myThread=new MyThread();
            new Thread(myThread).start();
        }



    }
    class  MyThread implements  Runnable
    {
        @Override
        public void run() {
            String result_all=NoHttpUtil.GetClassRoom(HttpLinkHeader.CLASSROOM);
            String result=JsonHandle.getClassRoomHtml(result_all);
            Message message=Message.obtain();
            Bundle bundle=new Bundle();
            bundle.putString("result",result);
            message.setData(bundle);
            message.what=1;
            handler.sendMessage(message);
        }
    }
    private  Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what)
            {
                case  1:
                    String html=msg.getData().getString("result");
                    webView.loadData(html,"text/html","utf-8");

                    break;
                case 2:
                    Toast.makeText(getContext(),"网络超时",Toast.LENGTH_SHORT).show();
                    break;
            }
            super.handleMessage(msg);
        }
    };*/

}
