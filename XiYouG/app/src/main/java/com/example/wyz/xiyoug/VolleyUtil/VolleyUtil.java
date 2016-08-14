package com.example.wyz.xiyoug.VolleyUtil;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Wyz on 2016/8/13.
 */
public class VolleyUtil {
    private  static RequestQueue requestQueue;
    public static void initialize(Context context) {
        requestQueue= Volley.newRequestQueue(context);
    }

    public  static RequestQueue getInstance()
    {
        return  requestQueue;
    }
}
