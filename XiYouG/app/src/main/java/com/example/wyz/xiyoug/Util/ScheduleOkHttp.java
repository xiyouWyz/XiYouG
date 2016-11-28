package com.example.wyz.xiyoug.Util;

import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.util.Log;

import com.example.wyz.xiyoug.Activity.FourLevelActivity;
import com.example.wyz.xiyoug.Model.ScoreModel;
import com.example.wyz.xiyoug.Viewer.ScheduleFragment;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.yolanda.nohttp.RedirectHandler;

import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
    public static String rootPath = Environment.getExternalStorageDirectory() + "/";
    private static OkHttpClient okHttpClient=new OkHttpClient();
    static
    {
        okHttpClient.setConnectTimeout(30,TimeUnit.SECONDS);
    }
    public static String GetRequestCheckCodeByOkHttp(final String checkCode_url) throws  Exception{
            String session_Id;
            okHttpClient.setFollowRedirects(false);
            com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder()
                    .url(checkCode_url)
                    /*.addHeader("Cookie",SESSION_ID)*/
                    .build();

            com.squareup.okhttp.Response response = okHttpClient.newCall(request).execute();
            if(response.isSuccessful()) {
                session_Id=response.headers().get("Set-Cookie");
                char sessionId[]=session_Id.toCharArray();
                for(int i=0;i<sessionId.length;i++)
                {
                    if(sessionId[i]==';')
                    {
                        session_Id=session_Id.substring(0,i);
                        break;
                    }
                }
                Log.d(TAG,session_Id);
                File file = new File(rootPath + "/checkCode");
                if(!file.exists()){
                    file.mkdirs();
                }
                File file1 = new File(file.getPath() + "/checkCode.png");
                if(file1.exists()){
                    file1.delete();
                }
                InputStream is = response.body().byteStream();
                OutputStream os = new FileOutputStream(file1);
                byte [] b = new byte[1024];
                int c;
                while ((c = is.read(b)) > 0){
                    os.write(b, 0, c);;
                }
                is.close();
                os.close();

                ScheduleFragment.sessionId=session_Id;
                return  file1.getPath();
            }
        return  "";
    }
    public static   String PostRequestFormByOkHttp(final  String account ,final String password,final String valiCode,final  String login_url,final String session_Id)
            throws IOException
    {
        RequestBody requestBody=new FormEncodingBuilder()
                .add("__VIEWSTATE","dDwtNTE2MjI4MTQ7Oz5O9kSeYykjfN0r53Yqhqckbvd83A==")
                .add("txtUserName",account)
                .add("TextBox2",password)
                .add("txtSecretCode",valiCode)
                .add("RadioButtonList1","学生")
                .add("Button1","")
                .add("lbLanguage","")
                .add("hidPdrs","")
                .add("hidsc","")
                .build();
        okHttpClient.setFollowRedirects(false);
        com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder()
                .url(login_url)
                .addHeader("Cookie",session_Id)
                .addHeader("Referer","http://222.24.62.120/")
                .post(requestBody)
                .build();

            com.squareup.okhttp.Response response = okHttpClient.newCall(request).execute();
            if(response.code()==302) {
                String redirect_url = response.headers().get("Location");
                Log.d(TAG, redirect_url);
                return GetRequestMainByOkHttp(redirect_url, session_Id);
            }
        return  "";
    }
    private  static   String GetRequestMainByOkHttp(final String redirect_url,final String session_Id)
    {
            Request request=new Request.Builder()
                    .addHeader("Cookie",session_Id)
                    .addHeader("Referer","http://222.24.62.120/")
                    .addHeader("Origin","http://222.24.62.120")
                    .url("http://222.24.62.120"+redirect_url)
                    .build();
            okHttpClient.setFollowRedirects(false);
            try {
                Response response=okHttpClient.newCall(request).execute();

                BufferedReader bufferedReader=new BufferedReader( response.body().charStream());
                StringBuffer stringBuffer=new StringBuffer();
                String line;
                while (((line=bufferedReader.readLine())!=null))
                {
                    stringBuffer.append(line);
                }
                String html=stringBuffer.toString();
                Log.d(TAG,html);
                return html;
            } catch (IOException e) {
                e.printStackTrace();
            }
        return  "";
    }
   /* public  static String postGetSessionFromServer(String url,String studyNumber,String password) throws IOException {
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
    }*/






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
    public  static String getFourLevelScore(String url,String session) throws Exception {
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
