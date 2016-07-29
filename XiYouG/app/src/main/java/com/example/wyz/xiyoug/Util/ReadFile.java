package com.example.wyz.xiyoug.Util;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * Created by Wyz on 2016/7/28.
 */
public class ReadFile {
    private static final  String  TAG="SaveFile";
    public static  String readLookRank(Context context)
    {
        return  loadData(context,"lookRankInfo");
    }
    public static  String  readColRank(Context context)
    {
        return  loadData(context,"colRankInfo");
    }
    public static   String readBorRank(Context context)
    {
        return  loadData(context,"borRankInfo");
    }
    public static   String readNews(Context context)
    {
        return  loadData(context,"newsInfo");
    }
    public static   String readNotice(Context context)
    {
        return  loadData(context,"noticeInfo");
    }
    public static   String loadData(Context context,String fileName)
    {
        FileInputStream in=null;
        BufferedReader reader=null;
        StringBuilder content=new StringBuilder();
        try
        {
            in=context.openFileInput(fileName);
            reader=new BufferedReader(new InputStreamReader(in));
            String line="";
            while((line=reader.readLine())!=null)
            {
                content.append(line);
            }

        }
        catch (Exception e)
        {
            Log.d(TAG,e.toString());
        }
        finally {
            if(reader!=null) {
                try
                {
                    reader.close();
                }
                catch (Exception e)
                {
                    Log.d(TAG,e.toString());
                }
            }
        }
        return content.toString();

    }
}

