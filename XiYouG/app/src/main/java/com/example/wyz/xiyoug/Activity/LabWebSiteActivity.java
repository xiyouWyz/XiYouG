package com.example.wyz.xiyoug.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.wyz.xiyoug.Model.HttpLinkHeader;
import com.example.wyz.xiyoug.R;
import com.example.wyz.xiyoug.Util.IsNetworkConnected;

/**
 * Created by Wyz on 2016/8/10.
 */
public class LabWebSiteActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private WebView webView;
    private FrameLayout mFrameLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.labwebsite);
        setupViewComponent();
        System.out.println(android.os.HandlerThread.currentThread().getName());
        System.out.println(android.os.HandlerThread.currentThread().getId());
        System.out.println(android.os.Process.myPid());
    }

    private void setupViewComponent() {
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mFrameLayout=(FrameLayout)findViewById(R.id.frameLayout);
        webView=new WebView(getApplicationContext());
        mFrameLayout.addView(webView);
        //webView=(WebView)findViewById(R.id.webView);
        if(!IsNetworkConnected.isNetworkConnected(LabWebSiteActivity.this))
        {
            Toast.makeText(LabWebSiteActivity.this,"网络超时",Toast.LENGTH_SHORT).show();
        }
        else
        {
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
            webView.loadUrl(HttpLinkHeader.LABWEBSITE);
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            finish();

        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onDestroy() {
        deStoryWebView();
        android.os.Process.killProcess(android.os.Process.myPid());
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
