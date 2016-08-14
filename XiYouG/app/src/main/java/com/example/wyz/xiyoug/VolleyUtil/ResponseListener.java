package com.example.wyz.xiyoug.VolleyUtil;

import com.android.volley.Response;

/**
 * Created by Wyz on 2016/8/13.
 */
public interface ResponseListener<T> extends Response.ErrorListener,Response.Listener<T> {

}
