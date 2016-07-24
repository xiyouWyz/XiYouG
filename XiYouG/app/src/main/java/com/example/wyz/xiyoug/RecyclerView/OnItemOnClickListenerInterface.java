package com.example.wyz.xiyoug.RecyclerView;

import android.view.View;

/**
 * Created by Wyz on 2016/7/23.
 */
public class OnItemOnClickListenerInterface {
    public  interface  OnItemClickListener
    {
        void OnItemClick(View view ,int position);
        void OnItemLongClick(View view,int position);
    }

}
