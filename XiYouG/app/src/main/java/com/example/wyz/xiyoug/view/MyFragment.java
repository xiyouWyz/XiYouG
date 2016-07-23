package com.example.wyz.xiyoug.View;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wyz.xiyoug.R;

/**
 * Created by Wyz on 2016/7/17.
 */
public class MyFragment extends Fragment {
    private  View view;
    private  TextView major_view;
    private  TextView name_view;
    private  TextView  studyNumber_view;
    private  LinearLayout my_login;
    private  LinearLayout my_bor;
    private  LinearLayout my_col;
    private  LinearLayout my_history;
    private  final  String TAG="MyFragment";
    LocalBroadcastManager broadcastManager;
    IntentFilter intentFilter;
    BroadcastReceiver mReceiver;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.my_page,container,false);

        setupViewComponent();
        return  view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
         broadcastManager=LocalBroadcastManager.getInstance(getActivity());
        //注册广播
        intentFilter=new IntentFilter();
        intentFilter.addAction("android.GET_USER_INFO");
        mReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals("android.GET_USER_INFO"))
                {
                    String id=intent.getStringExtra("ID");
                    String name=intent.getStringExtra("Name");
                    String department=intent.getStringExtra("Department");
                    String debt=intent.getStringExtra("Debt");
                    studyNumber_view.setText(id);
                    name_view.setText(name);
                    major_view.setText(department);

                }
            }
        };

        broadcastManager.registerReceiver(mReceiver,intentFilter);




    }
    private void setupViewComponent() {
        major_view=(TextView)view.findViewById(R.id.major);
        name_view=(TextView) view.findViewById(R.id.name);
        studyNumber_view=(TextView) view.findViewById(R.id.studyNumber);
        my_bor=(LinearLayout)view.findViewById(R.id.my_bor);
        my_col=(LinearLayout)view.findViewById(R.id.my_col);
        my_history=(LinearLayout)view.findViewById(R.id.my_history);
        my_bor.setOnClickListener(new MyOnClickListener());
        my_col.setOnClickListener(new MyOnClickListener());
        my_history.setOnClickListener(new MyOnClickListener());
        my_login=(LinearLayout)view.findViewById(R.id.login);
        my_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginDialog loginDialog=new LoginDialog(getContext());
                loginDialog.show();
            }
        });
    }

    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Log.d(TAG,String.valueOf(view.getId()));
            switch (view.getId())
            {
                case  R.id.my_bor:
                    break;
                case  R.id.my_col:
                    break;
                case  R.id.my_history:
                    break;
            }
            Log.d(TAG,String.valueOf(R.id.my_bor));
        }
    }
}
