package com.example.wyz.xiyoug.Viewer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.wyz.xiyoug.Model.HttpLinkHeader;
import com.example.wyz.xiyoug.R;
import com.example.wyz.xiyoug.Util.ScheduleOkHttp;

/**
 * Created by Wyz on 2016/7/22.
 */
public class LoginWindow extends PopupWindow{

    private  View loginView;
    private EditText account_view;
    private EditText password_view;
    private  EditText valiCode_view;
    private ImageView valiCode_imgView;
    private CheckBox isRemember;
    private Button loginBtn;
    private TextView close_textView;
    private TextView intro_textView;
    private  String filepath;
    public LoginWindow(Context context,View.OnClickListener loginOnClick ,String account,String password,boolean remember,int type,String path) {
        super(context);
        switch (type)
        {
            case 0:loginView=LayoutInflater.from(context).inflate(R.layout.login_page,null);
                setupBookViewComponent(context,loginOnClick,account,password,remember);
                break;
            case  1:loginView=LayoutInflater.from(context).inflate(R.layout.schedule_login,null);
                setupScheduleViewComponent(context,loginOnClick,account,password,remember,path);
                break;
        }

    }
    private  void setupBookViewComponent(Context context,View.OnClickListener loginOnClick ,String account,String password,boolean remember)
    {

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
        intro_textView=(TextView)loginView.findViewById(R.id.introduction);

        loginBtn.setOnClickListener(loginOnClick);
        close_textView.setOnClickListener(loginOnClick);
        intro_textView.setOnClickListener(loginOnClick);
        DisplayMetrics displayMetrics=context.getResources().getDisplayMetrics();
        this.setContentView(loginView);
        this.setWidth((int)(displayMetrics.widthPixels*0.8));
        this.setHeight((int)(displayMetrics.heightPixels*0.6));
        this.setFocusable(true);

        this.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.login_view_shape));
        this.setAnimationStyle(R.style.Login_Animation);
        this.setOutsideTouchable(false);

    }
    private  void setupScheduleViewComponent(Context context, View.OnClickListener loginOnClick , String account, String password, boolean remember, final String path)
    {

        account_view=(EditText)loginView.findViewById(R.id.account);
        password_view=(EditText)loginView.findViewById(R.id.password);
        valiCode_view=(EditText)loginView.findViewById(R.id.valiCode) ;
        valiCode_imgView=(ImageView)loginView.findViewById(R.id.valiCodeImg);
        Bitmap bitmap= BitmapFactory.decodeFile(path);
        valiCode_imgView.setImageBitmap(bitmap);
        valiCode_imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(){
                    @Override
                    public void run() {
                        try {
                            filepath=ScheduleOkHttp.GetRequestCheckCodeByOkHttp(HttpLinkHeader.CHECKCODE_URL);
                            Message message=Message.obtain();
                            message.what=1;
                            handler.sendMessage(message);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                }.start();
            }
        });

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
        intro_textView=(TextView)loginView.findViewById(R.id.introduction);

        loginBtn.setOnClickListener(loginOnClick);
        //valiCode_imgView.setOnClickListener(loginOnClick);
        close_textView.setOnClickListener(loginOnClick);
        intro_textView.setOnClickListener(loginOnClick);
        DisplayMetrics displayMetrics=context.getResources().getDisplayMetrics();
        this.setContentView(loginView);
        this.setWidth((int)(displayMetrics.widthPixels*0.8));
        this.setHeight((int)(displayMetrics.heightPixels*0.7));
        this.setFocusable(true);

        this.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.login_view_shape));
        this.setAnimationStyle(R.style.Login_Animation);
        this.setOutsideTouchable(false);

    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what)
            {
                case  1:
                    Bitmap bitmap=BitmapFactory.decodeFile(filepath);
                    valiCode_imgView.setImageBitmap(bitmap);
                    bitmap.recycle();
            }
        }
    };
    public    String getAccount()
    {
        return account_view.getText().toString();
    }
    public    String getPassword()
    {
        return password_view.getText().toString();
    }
    public    boolean getRemember()
    {
        return  isRemember.isChecked();
    }

    public  String getvaliCode()
    {
        return valiCode_view.getText().toString();
    }


}
