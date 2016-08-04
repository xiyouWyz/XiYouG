package com.example.wyz.xiyoug.Util;

import android.util.Log;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.CookieHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Wyz on 2016/7/31.
 */
public class ScheduleOkHttp {
    private static   String TAG="MyOkHttp";
    private static OkHttpClient okHttpClient=new OkHttpClient();
    public  static String postGetSessionFromServer(String url,String studyNumber,String password) throws IOException {
        RequestBody requestBody=new FormEncodingBuilder()
                .add("__VIEWSTATE","dDwxMTE4MjQwNDc1Ozs+ombGLJflIyczODVOjorgMB6XZe8=")
                .add("TextBox1",studyNumber)
                .add("TextBox2",password)
                .add("RadioButtonList1","%D1%A7%C9%FA")
                .add("Button1","+%B5%C7+%C2%BC+")
                .build();
        Request request=new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        okHttpClient.setFollowSslRedirects(false);
        okHttpClient.setFollowRedirects(false);
        Response response=okHttpClient.newCall(request).execute();
        String session=response.headers().get("Set-Cookie");
        char sessionId[]=session.toCharArray();
        for(int i=0;i<sessionId.length;i++)
        {
            if(sessionId[i]==';')
            {
                session=session.substring(0,i);
                break;
            }
        }
        Log.d(TAG,session);
        return  session;
    }

    public  static String getGetInfoFromServer(String url,String sessionID) throws IOException {
        Request request=new Request.Builder()
                .url(url)
                .addHeader("Cookie",sessionID)
                .build();

        okHttpClient.setFollowSslRedirects(false);
        okHttpClient.setFollowRedirects(false);
        Response response=okHttpClient.newCall(request).execute();
        if(response.isSuccessful())
        {
            return response.body().string();
        }
        return  null;
    }

    public  static  String getGetScore(String url,String sessionID)throws  IOException{
        String result= getGetSchedule(url,sessionID);
        return  result;
    }
    public  static  String getGetSchedule(String url,String sessionID) throws IOException {
        Request request=new Request.Builder()
                .url(url)
                .addHeader("Referer",url)
                .addHeader("Cookie",sessionID)
                .build();
        okHttpClient.setFollowSslRedirects(false);
        okHttpClient.setFollowRedirects(false);
        Response response=okHttpClient.newCall(request).execute();
        if(response.isSuccessful())
        {
            BufferedReader bufferedReader=new BufferedReader( response.body().charStream());
            String lines="";
            String line;
            while (((line=bufferedReader.readLine())!=null))
            {
                lines+=line;
            }
            return lines;
        }
        return  "";
    }

    public  static    List<String>  getPostScore(String url,String sessionID,String viewState,List<String> semesters) throws  IOException
    {
        List<String> allScoreHtml=new ArrayList<>();
        List<String> xqs=new ArrayList<>(Arrays.asList("1","2"));
        for (int i=0;i<semesters.size();i++)
        {
            for(int j=0;j<xqs.size();j++)
            {
                String score=getPostSemesterScore(url,sessionID,viewState,semesters.get(i),xqs.get(j));
                allScoreHtml.add(score);
            }
        }
        return  allScoreHtml;
    }
    private   static  String getPostSemesterScore(String url,String sessionID,String viewState,String xn,String xq) throws  IOException
    {
        RequestBody requestBody=new FormEncodingBuilder()
                .add("__VIEWSTATE",viewState)
                .add("__EVENTTARGET","")
                .add("__EVENTARGUMENT","")
                .add("hidLanguage","")
                .add("ddlXN",xn)
                .add("ddlXQ",xq)
                .add("ddl_kcxz","")
                .add("btn_xq","%D1%A7%C6%DA%B3%C9%BC%A8")

                .build();
        Request request=new Request.Builder()
                .url(url)
                .addHeader("Cookie",sessionID)
                .addHeader("Referer",url)
                .post(requestBody)
                .build();

        okHttpClient.setFollowSslRedirects(false);
        okHttpClient.setFollowRedirects(false);
        Response response=okHttpClient.newCall(request).execute();
        if(response.isSuccessful())
        {
            BufferedReader bufferedReader=new BufferedReader( response.body().charStream());
            String lines="";
            String line;
            while (((line=bufferedReader.readLine())!=null))
            {
                lines+=line;
            }
            return lines;
        }
        return  null;
    }
}
