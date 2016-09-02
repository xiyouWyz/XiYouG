package com.example.wyz.xiyoug.Viewer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.wyz.xiyoug.Activity.LabWebSiteActivity;
import com.example.wyz.xiyoug.Activity.QuestionActivity;
import com.example.wyz.xiyoug.Model.HttpLinkHeader;
import com.example.wyz.xiyoug.R;
import com.example.wyz.xiyoug.Util.IsNetworkConnected;

/**
 * Created by Wyz on 2016/8/10.
 */
public class AboutOurFragment extends Fragment  implements View.OnClickListener{
    private  View view;
    private LinearLayout update_layout;
    private LinearLayout question_layout;
    private LinearLayout feedback_layout;
    private LinearLayout our_layout;
    private WebView webView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.about_our,container,false);
        setupViewComponent();
        return  view;
    }

    private void setupViewComponent() {
        update_layout=(LinearLayout)view.findViewById(R.id.update);
        question_layout=(LinearLayout)view.findViewById(R.id.question);
        feedback_layout=(LinearLayout)view.findViewById(R.id.feedback);
        our_layout=(LinearLayout)view.findViewById(R.id.our);
        webView=(WebView)view.findViewById(R.id.our_webView);
        String html="<html><head><meta charset=\"utf-8\"></ head><body><a href=\"http://jiayudong.cn/Library/\">web图书馆</ a></ body></ html>";
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        //支持javascript
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setBackgroundColor(getResources().getColor(R.color.my_background));
        //自适应屏幕
       /* webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);*/

        webView.loadDataWithBaseURL(null,html,"text/html","utf-8",null);
        webView.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Uri uri = Uri.parse(url); //url为你要链接的地址
                Intent intent =new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                return true;
            }
        });
        update_layout.setOnClickListener(this);
        question_layout.setOnClickListener(this);
        feedback_layout.setOnClickListener(this);
        our_layout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case  R.id.update:
                Toast.makeText(getContext(),"已是最新版本",Toast.LENGTH_SHORT).show();
                break;
            case  R.id.question:
                Intent intent=new Intent();
                intent.setClass(getContext(), QuestionActivity.class);
                startActivity(intent);
                break;
            case R.id.feedback:
                if(!IsNetworkConnected.isNetworkConnected(getContext()))
                {
                    Toast.makeText(getContext(),"网络超时",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent data=new Intent();
                    data.setAction(Intent.ACTION_SENDTO);
                    data.setData(Uri.parse("mailto:745322878@qq.com"));
                    startActivity(data);
                }
                break;
            case R.id.our:
                if(!IsNetworkConnected.isNetworkConnected(getContext()))
                {
                    Toast.makeText(getContext(),"网络超时",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent intent1=new Intent();
                    intent1.setClass(getContext(), LabWebSiteActivity.class);
                    startActivity(intent1);
                    break;
                }

        }
    }
}
