package com.example.wyz.xiyoug.Util;

import android.util.Log;

import com.example.wyz.xiyoug.Model.FourLevelModel;
import com.example.wyz.xiyoug.Model.ScoreFailedModel;
import com.example.wyz.xiyoug.Model.ScoreModel;

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
    public  static  boolean getIsJudge(String content)
    {
        Document document=Jsoup.parse(content);
        Element ul=document.getElementsByClass("nav").get(0);
        for(int i=0;i<ul.children().size();i++)
        {
            if(ul.child(i).text().contains("教学质量评价"))
            {
                if(ul.child(i).children().size()==2)
                {
                    return true;
                }
            }
        }
        return  false;
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
        //String schedule= document.body().getElementById("Table1").text();
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
            else if(textNodes.size()==2){

                String schedule_name = textNodes.get(0).text();
                String schedule_teacher = textNodes.get(1).text();
                String schedule = schedule_name +"\n"+"@"+ schedule_teacher+"\n";
                time.add(schedule);
            }
            else if(textNodes.size()==3){

                String schedule_name = textNodes.get(0).text();
                String schedule_teacher = textNodes.get(1).text();
                String schedule_room = textNodes.get(2).text();
                String schedule = schedule_name +"\n"+"@"+ schedule_teacher +"\n"+"@"+ schedule_room+"\n";
                time.add(schedule);
            }
            else if(textNodes.size()==4){

                String schedule_name = textNodes.get(0).text();
                String schedule_teacher = textNodes.get(2).text();
                String schedule_room = textNodes.get(3).text();
                String schedule = schedule_name +"\n"+"@"+ schedule_teacher +"\n"+"@"+ schedule_room+"\n";
                time.add(schedule);
            }
            else if(textNodes.size()==6){

                String schedule_name = textNodes.get(0).text();
                String schedule_teacher = textNodes.get(1).text();
                String schedule_room = textNodes.get(2).text();
                String schedule = schedule_name +"\n"+"@"+ schedule_teacher +"\n"+"@"+ schedule_room+"\n";
                time.add(schedule);
            }
            else if(textNodes.size()==8){

                String schedule_name = textNodes.get(0).text();
                String schedule_teacher = textNodes.get(2).text();
                String schedule_room = textNodes.get(3).text();
                String schedule = schedule_name +"\n"+"@"+ schedule_teacher +"\n"+"@"+ schedule_room+"\n";
                time.add(schedule);
            }
            else if(textNodes.size()==9){

                String schedule_name = textNodes.get(0).text();
                String schedule_teacher = textNodes.get(1).text();
                String schedule_room = textNodes.get(2).text();
                String schedule = schedule_name +"\n"+"@"+ schedule_teacher +"\n"+"@"+ schedule_room+"\n";
                time.add(schedule);
            }
            else if(textNodes.size()==12){

                String schedule_name = textNodes.get(0).text();
                String schedule_teacher = textNodes.get(2).text();
                String schedule_room = textNodes.get(3).text();
                String schedule = schedule_name +"\n"+"@"+ schedule_teacher +"\n"+"@"+ schedule_room+"\n";
                time.add(schedule);
            }
            else if(textNodes.size()==16){

                String schedule_name = textNodes.get(0).text();
                String schedule_teacher = textNodes.get(2).text();
                String schedule_room = textNodes.get(3).text();
                String schedule = schedule_name +"\n"+"@"+ schedule_teacher +"\n"+"@"+ schedule_room+"\n";
                time.add(schedule);
            }
            else
            {
                String schedule_name = textNodes.get(0).text();
                String schedule_teacher = textNodes.get(2).text();
                String schedule_room = textNodes.get(3).text();
                String schedule = schedule_name +"\n"+"@"+ schedule_teacher +"\n"+"@"+ schedule_room+"\n";
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
            else if(textNodes.size()==2){

                String schedule_name = textNodes.get(0).text();
                String schedule_teacher = textNodes.get(1).text();
                String schedule = schedule_name +"\n"+"@"+ schedule_teacher +"\n";
                time.add(schedule);
            }
            else if(textNodes.size()==3){

                String schedule_name = textNodes.get(0).text();
                String schedule_teacher = textNodes.get(1).text();
                String schedule_room = textNodes.get(2).text();
                String schedule = schedule_name +"\n"+"@"+ schedule_teacher +"\n"+"@"+ schedule_room+"\n";
                time.add(schedule);
            }
            else if(textNodes.size()==4){

                String schedule_name = textNodes.get(0).text();
                String schedule_teacher = textNodes.get(2).text();
                String schedule_room = textNodes.get(3).text();
                String schedule = schedule_name +"\n"+"@"+ schedule_teacher +"\n"+"@"+ schedule_room+"\n";
                time.add(schedule);
            }
            else if(textNodes.size()==6){

                String schedule_name = textNodes.get(0).text();
                String schedule_teacher = textNodes.get(1).text();
                String schedule_room = textNodes.get(2).text();
                String schedule = schedule_name +"\n"+"@"+ schedule_teacher +"\n"+"@"+ schedule_room+"\n";
                time.add(schedule);
            }
            else if(textNodes.size()==8){

                String schedule_name = textNodes.get(0).text();
                String schedule_teacher = textNodes.get(2).text();
                String schedule_room = textNodes.get(3).text();
                String schedule = schedule_name +"\n"+"@"+ schedule_teacher +"\n"+"@"+ schedule_room+"\n";
                time.add(schedule);
            }
            else if(textNodes.size()==9){

                String schedule_name = textNodes.get(0).text();
                String schedule_teacher = textNodes.get(1).text();
                String schedule_room = textNodes.get(2).text();
                String schedule = schedule_name +"\n"+"@"+ schedule_teacher +"\n"+"@"+ schedule_room+"\n";
                time.add(schedule);
            }
            else if(textNodes.size()==12){

                String schedule_name = textNodes.get(0).text();
                String schedule_teacher = textNodes.get(2).text();
                String schedule_room = textNodes.get(3).text();
                String schedule = schedule_name +"\n"+"@"+ schedule_teacher +"\n"+"@"+ schedule_room+"\n";
                time.add(schedule);
            }
            else if(textNodes.size()==16){

                String schedule_name = textNodes.get(0).text();
                String schedule_teacher = textNodes.get(2).text();
                String schedule_room = textNodes.get(3).text();
                String schedule = schedule_name +"\n"+"@"+ schedule_teacher +"\n"+"@"+ schedule_room+"\n";
                time.add(schedule);
            }
            else
            {
                String schedule_name = textNodes.get(0).text();
                String schedule_teacher = textNodes.get(2).text();
                String schedule_room = textNodes.get(3).text();
                String schedule = schedule_name +"\n"+"@"+ schedule_teacher +"\n"+"@"+ schedule_room+"\n";
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
    public  static  List<String> getYearCount(String score_html)
    {
        List<String> years=new ArrayList<>();
        String year;
        Document document=Jsoup.parse(score_html);
        Element form=document.body().getElementById("Form1");
        Element select=form.getElementById("ddlXN");

        for(int i=0;i<select.children().size();i++)
        {
             year=select.child(i).text();
            if(!year.equals(""))
            {
                years.add(year);
            }
        }
        return  years;
    }
    public static List<ScoreModel> getSemesterScore(String semesterScore) {
        ScoreModel scoreModel=null;
        List<ScoreModel> scoreModels=new ArrayList<>();
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
                scoreModel=new ScoreModel.Builder()
                        .title(getSemesterTitle(semesterScore))
                        .college(tr.child(0).text())
                        .semester(tr.child(1).text())
                        .course_code(tr.child(2).text())
                        .course_name(tr.child(3).text())
                        .course_nature(tr.child(4).text())
                        .course_attribute(tr.child(5).text())
                        .credit(tr.child(6).text())
                        .grade_point(tr.child(7).text())
                        .grade(tr.child(8).text())
                        .minor_mark(tr.child(9).text())
                        .repair_code(tr.child(10).text())
                        .restudy_code(tr.child(11).text())
                        .build();
                scoreModels.add(scoreModel);
           /*     map=new HashMap<>();
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
                map=null;*/
            }
            return  scoreModels;
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
    public  static  List<ScoreFailedModel> getFailedScore(String score_html)
    {
        List<ScoreFailedModel> scoreFailedModels=new ArrayList<>();
        ScoreFailedModel scoreFailedModel=null;
        Document document=Jsoup.parse(score_html);
        Element table=document.body().getElementById("Datagrid3");
        Element tbody=table.child(0);
        if(tbody.children().size()!=1)
        {
            for (int i=1;i<tbody.children().size();i++)
            {
                Element tr=tbody.child(i);
               scoreFailedModel=new ScoreFailedModel.Builder()
                      .course_code(tr.child(0).text())
                      .course_name(tr.child(1).text())
                      .course_nature(tr.child(2).text())
                      .credit(tr.child(3).text())
                      .best_score(tr.child(4).text())
                      .build();
                scoreFailedModels.add(scoreFailedModel);
            }
            return  scoreFailedModels;
        }
        return  null;
    }
    public static List<List<ScoreModel>> getAllScore(List<String> allScoreHtml) {
        List<List<ScoreModel>> lists=new ArrayList<>();
        List<String> semesterTitle=new ArrayList<>();
        List<ScoreModel> scoreModels;
        for(int i=0;i<allScoreHtml.size();i++)
        {
            scoreModels=getSemesterScore(allScoreHtml.get(i));
            if(scoreModels!=null)
            {
                semesterTitle.add(getSemesterTitle(allScoreHtml.get(i)));
                lists.add(scoreModels);
            }
        }
        return  lists;
    }
    public static FourLevelModel getFourLevelScore(String content) throws  Exception
    {
        Document document= Jsoup.parse(content);
        Elements elements= document.body().getElementsByClass("cetTable");

        Element body=(Element) elements.get(0).childNodes().get(1);
        String name=body.childNodes().get(0).childNodes().get(3).childNodes().get(0).toString();
        String school=body.childNodes().get(2).childNodes().get(3).childNodes().get(0).toString();
        String type=body.childNodes().get(4).childNodes().get(3).childNodes().get(0).toString();
        String account=body.childNodes().get(6).childNodes().get(3).childNodes().get(0).toString();
        String time=body.childNodes().get(8).childNodes().get(3).childNodes().get(0).toString();

        String total_grade=body.childNodes().get(10).childNodes().get(3).childNodes().get(1).childNodes().get(0).toString();
        String listen_grade=body.childNodes().get(10).childNodes().get(3).childNodes().get(5).toString();
        String read_grade=body.childNodes().get(10).childNodes().get(3).childNodes().get(8).toString();
        String write_grade=body.childNodes().get(10).childNodes().get(3).childNodes().get(11).toString();
        FourLevelModel fourLevelModel=new FourLevelModel.Builder()
                .name(name)
                .school(school)
                .type(type)
                .account(account)
                .time(time)
                .total_grade(total_grade)
                .listen_grade(listen_grade)
                .read_grade(read_grade)
                .write_grade(write_grade)
                .builder();

        return  fourLevelModel;
    }
    public static String getFourLevelScoreLabel(String content)
    {
        Document document= Jsoup.parse(content);
        Element div= document.body().getElementById("leftH");
        String result=div.child(1).text();
        return  getFourLevelLabel(result);
    }
    private  static  String getFourLevelLabel(String content)
    {
        String[] a=content.split("。");
        return  a[0];
    }
}
