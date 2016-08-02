package com.example.wyz.xiyoug.View;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.wyz.xiyoug.R;

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

    public LoginWindow(Context context,View.OnClickListener loginOnClick ,String account,String password,boolean remember,int type) {
        super(context);
        switch (type)
        {
            case 0:loginView=LayoutInflater.from(context).inflate(R.layout.login_page,null);
                setupViewComponent(context,loginOnClick,account,password,remember);
                break;
            case  1:loginView=LayoutInflater.from(context).inflate(R.layout.schedule_login,null);
                setupViewComponent(context,loginOnClick,account,password,remember);
                break;
        }

    }
    private  void setupViewComponent(Context context,View.OnClickListener loginOnClick ,String account,String password,boolean remember)
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
