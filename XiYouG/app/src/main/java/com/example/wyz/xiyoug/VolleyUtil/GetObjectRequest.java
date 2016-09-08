package com.example.wyz.xiyoug.VolleyUtil;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.example.wyz.xiyoug.Activity.FourLevelActivity;

import java.io.UnsupportedEncodingException;

/**
 * Created by Wyz on 2016/8/13.
 */
public  class GetObjectRequest<T> extends Request<T> {

    private  final String  TAG="GetObjectRequest";
    private  ResponseListener responseListener;

    public GetObjectRequest(String url, ResponseListener listener) {
        super(Method.GET,url, listener);
        this.responseListener = listener ;
    }

    /**
     * 对服务器响应的数据进行解析
     * @param networkResponse
     * @return
     */
    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse networkResponse) {

        try {
            T result;
            String string=new String(networkResponse.data, HttpHeaderParser.parseCharset(networkResponse.headers));
            String session=networkResponse.headers.get("Set-Cookie");
            char sessionId[]=session.toCharArray();
            for(int i=0;i<sessionId.length;i++)
            {
                if(sessionId[i]==';')
                {
                    session=session.substring(0,i);
                    break;
                }
            }
            FourLevelActivity.SESSION_ID=session;
            Log.d(TAG,session);
            result=(T)string;
            return  Response.success(result, HttpHeaderParser.parseCacheHeaders(networkResponse));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 对服务器的响应进行回调
     * @param t
     */
    @Override
    protected void deliverResponse(T t) {
        responseListener.onResponse(t);
    }
}
