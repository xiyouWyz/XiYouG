package com.example.wyz.xiyoug.Util;

import android.util.Log;

import com.example.wyz.xiyoug.Activity.FourLevelActivity;
import com.example.wyz.xiyoug.Model.ScoreModel;
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
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.StreamHandler;

/**
 * Created by Wyz on 2016/7/31.
 */
public class ScheduleOkHttp {
    private static   String TAG="MyOkHttp";
    public static int semesCount;
    private static OkHttpClient okHttpClient=new OkHttpClient();
    static
    {
        okHttpClient.setConnectTimeout(30,TimeUnit.SECONDS);
    }

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
            BufferedReader bufferedReader=new BufferedReader( response.body().charStream());
            StringBuffer stringBuffer=new StringBuffer();
            String line;
            while (((line=bufferedReader.readLine())!=null))
            {
                stringBuffer.append(line);
            }
            return stringBuffer.toString();
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
            StringBuffer stringBuffer=new StringBuffer();
            String line;
            while (((line=bufferedReader.readLine())!=null))
            {
                stringBuffer.append(line);
            }
            return stringBuffer.toString();
        }
        return  "";
    }

    public  static    List<String>  getPostScore(String url,String sessionID,String viewState,List<String> semesters) throws  IOException
    {
        List<String> allScoreHtml=new ArrayList<>();
        List<String> xqs=new ArrayList<>(Arrays.asList("2","1"));
        List<Map<String,String>> maps=null;
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
    public  static     List<ScoreModel> getPostFirstScore(String url, String sessionID, String viewState, List<String> xns, int alreadySeme) throws  IOException
    {
        List<String> xqs=new ArrayList<>(Arrays.asList("2","1"));
        List<ScoreModel> scoreModels=null;
        for (int i=0;i<xns.size();i++)
        {
            for(int j=0;j<xqs.size();j++)
            {
                String score_html=getPostSemesterScore(url,sessionID,viewState,xns.get(i),xqs.get(j));
                scoreModels=JsonHandle.getSemesterScore(score_html);
                if(scoreModels!=null)
                {
                    semesCount=alreadySeme;
                    return  scoreModels;
                }
                else
                {
                    alreadySeme-=1;
                }
                //allScoreHtml.add(score);
            }
        }
        return  null;
    }
    public  static  int  getPostFirstScoreCount(String url,String sessionID,String viewState,List<String> xns,int alreadySeme) throws  IOException
    {
        String score_html;
        List<String> xqs=new ArrayList<>(Arrays.asList("2","1"));
        List<ScoreModel> scoreModels=null;
        for (int i=0;i<xns.size();i++)
        {
            for(int j=0;j<xqs.size();j++)
            {
                score_html=getPostSemesterScore(url,sessionID,viewState,xns.get(i),xqs.get(j));
                scoreModels=JsonHandle.getSemesterScore(score_html);
                if(scoreModels!=null)
                {
                    semesCount=alreadySeme;
                    return semesCount;
                }
                else
                {
                    alreadySeme-=1;
                }
                //allScoreHtml.add(score);
            }
        }
        return 0;
    }
    public    static  String getPostSemesterScore(String url,String sessionID,String viewState,String xn,String xq) throws  IOException
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
            StringBuilder lines=new StringBuilder();

            String line;
            while (((line=bufferedReader.readLine())!=null))
            {
                lines.append(line);
                line=null;
            }
            return lines.toString();
        }
        return  null;
    }
    public  static String getFourLevelScore(String url,String session) throws IOException {
        Request request=new Request.Builder()
                .url(url)
                .addHeader("Referer","http://www.chsi.com.cn/cet/")
                .addHeader("Cookie",session)
                .build();
        okHttpClient.setFollowSslRedirects(false);
        okHttpClient.setFollowRedirects(false);
        Response response=okHttpClient.newCall(request).execute();
        if(response.isSuccessful())
        {
            BufferedReader bufferedReader=new BufferedReader( response.body().charStream());
            StringBuffer stringBuffer=new StringBuffer();
            String line;
            while (((line=bufferedReader.readLine())!=null))
            {
                stringBuffer.append(line);
            }
            return stringBuffer.toString();
        }
        return  "";
    }
}
