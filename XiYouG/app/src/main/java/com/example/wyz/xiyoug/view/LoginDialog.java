package com.example.wyz.xiyoug.View;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wyz.xiyoug.InfoDetail_Activity;
import com.example.wyz.xiyoug.R;
import com.example.wyz.xiyoug.Util.OkHttpUtil;

import org.json.JSONObject;

/**
 * Created by Wyz on 2016/7/22.
 */
public class LoginDialog extends Dialog{

    private EditText account;
    private EditText password;
    private CheckBox isRemember;
    private Button loginBtn;
    private String url;
    public  static  String SESSIONID;
    public LoginDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        setupViewComponent();
    }

    private void setupViewComponent() {
        account=(EditText)findViewById(R.id.account);
        password=(EditText)findViewById(R.id.password);
        isRemember=(CheckBox)findViewById(R.id.remember);
        loginBtn=(Button)findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(new MyOnClickListener());

    }
    public   class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if(account.getText().equals(""))
            {
                Toast.makeText(getContext(),"用户名不能为空",Toast.LENGTH_SHORT).show();
            }
            else if(password.getText().equals(""))
            {
                Toast.makeText(getContext(),"密码不能为空",Toast.LENGTH_SHORT).show();
            }
            else
            {
                 url="https://api.xiyoumobile.com/xiyoulibv2/user/login?username="+account.getText()+
                        "&password="+password.getText();
                MyThread myThread=new MyThread();
                new Thread(myThread).start();
            }
        }
    }
    private class   MyThread implements Runnable
    {
        @Override
        public void run() {
            try {
                String login_result= OkHttpUtil.getStringFromServer(url);
                boolean isSuccess=new JSONObject(login_result).getBoolean("Result");
                if(isSuccess==true)
                {
                    SESSIONID=new JSONObject(login_result).getString("Detail");
                    String user_url="https://api.xiyoumobile.com/xiyoulibv2/user/info?session="+SESSIONID;
                    String userInfo_result=OkHttpUtil.getStringFromServer(user_url);
                    boolean isGetInfoSuccess=new JSONObject(userInfo_result).getBoolean("Result");
                    if(isGetInfoSuccess==true)
                    {
                        JSONObject jsonObject=new JSONObject(userInfo_result).getJSONObject("Detail");
                        String id=jsonObject.getString("ID");
                        String name=jsonObject.getString("Name");
                        String department=jsonObject.getString("Department");
                        String debt=jsonObject.getString("Debt");
                        Intent intent=new Intent();

                        intent.setAction("android.GET_USER_INFO");
                        intent.putExtra("ID",id);
                        intent.putExtra("Name",name);
                        intent.putExtra("Department",department);
                        intent.putExtra("Debt",debt);
                        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
                        dismiss();
                    }
                    else
                    {
                        Toast.makeText(getContext(),"获取个人信息失败",Toast.LENGTH_SHORT).show();
                    }

                }
                else
                {
                    Toast.makeText(getContext(),"账号错误，密码错误或账户不存在",Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
