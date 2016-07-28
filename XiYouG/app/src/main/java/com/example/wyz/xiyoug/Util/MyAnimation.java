package com.example.wyz.xiyoug.Util;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Wyz on 2016/7/26.
 */
public class MyAnimation extends RelativeLayout {
    private AnimationDrawable mAnimation;
    private ImageView mImageView;
    private TextView mTextView;
    private String loadingTip;
    private int resid;
    public  MyAnimation(Context context,String content,int resid,RelativeLayout relativeLayout)
    {
        super(context);
        this.loadingTip = content;
        this.resid = resid;
        LinearLayout linearLayout = (LinearLayout) relativeLayout.getChildAt(0);

        mImageView = (ImageView) linearLayout.getChildAt(0);
        mTextView = (TextView) linearLayout.getChildAt(1);
        mImageView.setBackgroundResource(resid);
        // 通过ImageView对象拿到背景显示的AnimationDrawable
        mAnimation = (AnimationDrawable) mImageView.getBackground();
        mImageView.post(new Runnable() {
            @Override
            public void run() {
                mAnimation.start();
            }
        });
        mTextView.setText(loadingTip);
    }
}
