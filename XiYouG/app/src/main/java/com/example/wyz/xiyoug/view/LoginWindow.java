package com.example.wyz.xiyoug.View;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wyz.xiyoug.InfoDetail_Activity;
import com.example.wyz.xiyoug.R;
import com.example.wyz.xiyoug.Util.OkHttpUtil;

import org.json.JSONObject;
import org.w3c.dom.Text;

/**
 * Created by Wyz on 2016/7/22.
 */
public class LoginWindow extends PopupWindow{

    private  View loginView;
    private EditText account_view;
    private EditText password_view;
    private CheckBox isRemember;
    private Button loginBtn;
    private TextView close_textView;

    public LoginWindow(Context context,View.OnClickListener loginOnClick ,String account,String password,boolean remember) {
        super(context);
        loginView=LayoutInflater.from(context).inflate(R.layout.login_page,null);
        account_view=(EditText)loginView.findViewById(R.id.account);
        password_view=(EditText)loginView.findViewById(R.id.password);
        isRemember=(CheckBox)loginView.findViewById(R.id.remember);
        if(remember)
        {
            account_view.setText(account);
            password_view.setText(password);
            isRemember.setChecked(remember);
        }
        else
        {
            account_view.setText(account);
        }
        loginBtn=(Button)loginView.findViewById(R.id.login_btn);
        close_textView=(TextView) loginView.findViewById(R.id.close) ;
        loginBtn.setOnClickListener(loginOnClick);
        close_textView.setOnClickListener(loginOnClick);
        DisplayMetrics displayMetrics=context.getResources().getDisplayMetrics();
        this.setContentView(loginView);
        this.setWidth((int)(displayMetrics.widthPixels*0.8));
        this.setHeight((int)(displayMetrics.heightPixels*0.6));
        this.setFocusable(true);
        this.setBackgroundDrawable(context.getDrawable(R.drawable.login_view_shape));
        this.setAnimationStyle(R.style.Login_Animation);
        this.setOutsideTouchable(false);



    }

    public    String getAccount()
    {
        return  account_view.getText().toString();
    }
    public    String getPassword()
    {
        return  password_view.getText().toString();
    }
    public    boolean getRemember()
    {
        return  isRemember.isChecked();
    }

}
