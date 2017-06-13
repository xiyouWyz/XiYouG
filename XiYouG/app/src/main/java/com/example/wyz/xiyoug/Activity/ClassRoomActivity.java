package com.example.wyz.xiyoug.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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
public class ClassRoomActivity extends AppCompatActivity {
    private TextView title_textView;
    private WebView webView;

   private FrameLayout mFrameLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.class_room);
        NoHttp.init(getApplication());


        setupViewComponent1();
    }
    private void setupViewComponent1() {
        mFrameLayout=(FrameLayout) findViewById(R.id.frameLayout);
        webView=new WebView(getApplicationContext());

        //webView=(WebView)findViewById(R.id.webView);
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
        mFrameLayout.addView(webView);
    }

    @Override
    protected void onDestroy() {
      /*  deStoryWebView();
        android.os.Process.killProcess(android.os.Process.myPid());*/
        super.onDestroy();

    }

    private  void deStoryWebView(){
        if(webView!=null){
            webView.pauseTimers();
            webView.removeAllViews();
            webView.destroy();
            webView=null;
        }
    }
}
