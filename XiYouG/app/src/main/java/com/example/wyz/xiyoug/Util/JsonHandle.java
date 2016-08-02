package com.example.wyz.xiyoug.Util;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Wyz on 2016/7/31.
 */
public class JsonHandle {
    public static   String getName(String content)
    {
        Document document=Jsoup.parse(content);
        String name= document.body().getElementById("xhxm").text();
        name=name.substring(0,getIndex(name,'同'));
        Log.d("name",name) ;
        return  name;
    }
    public  static  int getIndex(String s,char ch)
    {
        char chs[]=s.toCharArray();
        for(int i=0;i<s.length();i++)
        {
            if(chs[i]==ch)
            {
                return  i;
            }
        }
        return  s.length();
    }

    public static Map<String, List<String>> getSchedule(String result) {
        List<String> time1=new ArrayList<>();
        List<String> time2=new ArrayList<>();
        List<String> time3=new ArrayList<>();
        List<String> time4=new ArrayList<>();
        Map<String,List<String>> listMap=new HashMap<>();
        Document document= Jsoup.parse(result);
        String schedule= document.body().getElementById("Table1").text();
        Element tbody=document.body().getElementById("Table1").child(0);

        Element tr_day1_time1=tbody.child(2);
        time1=getTime1Schedule(tr_day1_time1,time1);
        listMap.put("time1",time1);

        Element tr_day1_time2=tbody.child(4);
        time2=getTime2Schedule(tr_day1_time2,time2);
        listMap.put("time2",time2);

        Element tr_day1_time3=tbody.child(6);
        time3=getTime1Schedule(tr_day1_time3,time3);
        listMap.put("time3",time3);

        Element tr_day1_time4=tbody.child(8);
        time4=getTime2Schedule(tr_day1_time4,time4);
        listMap.put("time4",time4);
        return  listMap;
    }
    private static List<String> getTime1Schedule(Element tr_time,List<String> time)
    {
        for(int i=2;i<7;i++)
        {
            List<TextNode> textNodes=tr_time.child(i).textNodes();
            if(textNodes.size()==1)
            {
                time.add("");
            }
            else {
                String schedule_name = textNodes.get(0).text();
                String schedule_teacher = textNodes.get(2).text();
                String schedule_room = textNodes.get(3).text();
                String schedule = schedule_name +"@"+ schedule_teacher +"@"+ schedule_room;
                time.add(schedule);
            }
        }
        return  time;
    }
    private static  List<String> getTime2Schedule(Element tr_time,List<String> time)
    {
        for(int i=1;i<6;i++)
        {
            List<TextNode> textNodes=tr_time.child(i).textNodes();
            if(textNodes.size()==1)
            {
                time.add("");
            }
            else {
                String schedule_name = textNodes.get(0).text();
                String schedule_teacher = textNodes.get(2).text();
                String schedule_room = textNodes.get(3).text();
                String schedule = schedule_name +"@"+ schedule_teacher +"@"+ schedule_room;
                time.add(schedule);
            }
        }
        return  time;
    }
    public  static  String getSemesterSchedule(String schedule)
    {
        String semester="";
        Document document= Jsoup.parse(schedule);
        Element tbody=document.body().getElementById("Table2").child(0);
        String year=tbody.getElementById("xnd").child(0).text();
        semester+=year;
        semester+=tbody.getElementById("Label2").text();
        String sem=tbody.getElementById("xqd").child(0).text();
        semester+=sem;
        semester+=tbody.getElementById("Label1").text();
        return  semester;
    }

}
