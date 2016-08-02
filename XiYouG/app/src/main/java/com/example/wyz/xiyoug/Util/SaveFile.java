package com.example.wyz.xiyoug.Util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

/**
 * Created by Wyz on 2016/7/28.
 */
public class SaveFile {
    private  static final  String  TAG="SaveFile";
    public static  void saveLookRank(Context context,String lookRankInfo)
    {
        saveData(context,"lookRankInfo",lookRankInfo);
    }
    public static  void  saveColRank(Context context, String colRankInfo)
    {
        saveData(context,"colRankInfo",colRankInfo);
    }
    public static   void saveBorRank(Context context,String borRankInfo)
    {
        saveData(context,"borRankInfo",borRankInfo);
    }
    public static   void saveNews(Context context,String newsInfo)
    {
        saveData(context,"newsInfo",newsInfo);
    }
    public static   void saveNotice(Context context,String noticeInfo)
    {
        saveData(context,"noticeInfo",noticeInfo);
    }

    public  static  void saveSchedule(Context context,String fileContent)
    {
        saveData(context,"scheduleInfo",fileContent);
    }
    private  static void saveData(Context context,String fileName,String fileContent)
    {
        FileOutputStream out=null;
        BufferedWriter writer=null;
        try
        {
            out=context.openFileOutput(fileName, Context.MODE_PRIVATE);
            writer=new BufferedWriter( new OutputStreamWriter(out));
            writer.write(fileContent);
        }
        catch (Exception e)
        {
            Log.d(TAG,e.toString());
        }
        finally {
            try
            {
                if(writer!=null)
                {
                    writer.close();
                }
            }
            catch (Exception e)
            {
                Log.d(TAG,e.toString());
            }
        }
    }
}
