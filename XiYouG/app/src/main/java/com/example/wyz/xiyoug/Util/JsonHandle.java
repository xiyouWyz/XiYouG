package com.example.wyz.xiyoug.Util;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Wyz on 2016/7/31.
 */
public class JsonHandle {
    private static final   String TAG="JsonHandle";
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
    public  static  String getSemesterTitleSchedule(String schedule)
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
    public static  List<String> getScoreUserInfo(String score_html)
    {
        List<String> userinfo=new ArrayList<>();
        Document document=Jsoup.parse(score_html);
        Element tbody=document.body().getElementById("Table1").child(0);
        Element trName=tbody.child(1);
        String xh=trName.getElementById("lbl_xh").text();
        String xm=trName.getElementById("lbl_xm").text();
        String xy=trName.getElementById("lbl_xy").text();
        Element trClass=tbody.child(2);
        String zybj=trClass.getElementById("lbl_xzb").text();
        userinfo.add(DelWithScoreInfo(xh));
        userinfo.add(DelWithScoreInfo(xm));
        userinfo.add(DelWithScoreInfo(xy));
        userinfo.add(DelWithScoreInfo(zybj));
        return userinfo;
    }
    private static String DelWithScoreInfo(String str)
    {
        String[] strs=str.split("：");
        return  strs[1];
    }
    public static String getViewState(String score_html) {
        Document document=Jsoup.parse(score_html);
        Element form=document.body().getElementById("Form1");
        String result=form.select("input[name=__VIEWSTATE]").get(0).attr("value");
        return  result;
    }
    public  static  List<String> getSemesterCount(String score_html)
    {
        List<String> semester=new ArrayList<>();
        Document document=Jsoup.parse(score_html);
        Element form=document.body().getElementById("Form1");
        Element select=form.getElementById("ddlXN");

        for(int i=0;i<select.children().size();i++)
        {
            String seme=select.child(i).text();
            if(!seme.equals(""))
            {
                semester.add(seme);
            }
        }
        return  semester;
    }
    private static List<Map<String,String>> getSemesterScore(String semesterScore) {
        Map<String,String> map;
        List<Map<String,String>> mapList=new ArrayList<>();
        Document doc=Jsoup.parse(semesterScore);
        Element table=doc.getElementById("Datagrid1");
        //select用于找某一个元素
        //Elements table1=doc.select("table[id=Datagrid1]");
        Element tbody=table.child(0);
        if(tbody.children().size()!=1)
        {
            for (int i=1;i<tbody.children().size();i++)
            {
                Element tr=tbody.child(i);
                map=new HashMap<>();
                map.put("title",getSemesterTitle(semesterScore));
                map.put("学年",tr.child(0).text());
                map.put("学期",tr.child(1).text());
                map.put("课程代码",tr.child(2).text());
                map.put("课程名称",tr.child(3).text());
                map.put("课程性质",tr.child(4).text());
                map.put("课程属性",tr.child(5).text());
                map.put("学分",tr.child(6).text());
                map.put("绩点",tr.child(7).text());
                map.put("成绩",tr.child(8).text());
                map.put("辅修标记",tr.child(9).text());
                map.put("补考成绩",tr.child(10).text());
                map.put("重修成绩",tr.child(11).text());
                mapList.add(map);
            }
            return  mapList;
        }
       return  null;
    }
    private static  String getSemesterTitle(String scoreHtml)
    {
        Document document=Jsoup.parse(scoreHtml);
        Element tbody=document.body().getElementById("Table1").child(0);
        Element trName=tbody.child(0);
        Element span=trName.getElementById("lbl_bt");
        return  span.child(0).text();
    }
    public static List<List<Map<String,String>>> getAllScore(List<String> allScoreHtml) {
        List<List<Map<String,String>>> lists=new ArrayList<>();
        List<String> semesterTitle=new ArrayList<>();
        List<Map<String,String>> maps;
        for(int i=0;i<allScoreHtml.size();i++)
        {
           maps=getSemesterScore(allScoreHtml.get(i));
            if(maps!=null)
            {
                semesterTitle.add(getSemesterTitle(allScoreHtml.get(i)));
                lists.add(maps);
            }
        }
        return  lists;
    }
}
