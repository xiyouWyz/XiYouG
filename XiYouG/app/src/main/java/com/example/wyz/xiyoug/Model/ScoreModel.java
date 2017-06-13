package com.example.wyz.xiyoug.Model;

import android.app.AlertDialog;
import android.app.Notification;
import android.os.Bundle;

import java.util.StringTokenizer;

/**
 * Created by Wyz on 2016/8/5.
 */
public class ScoreModel {
    public   String title;              //2016-2017学年第1学期学习成绩
    public  String college;             //学年
    public  String semester;            //1
    public  String course_code;         //JS100140，课程代码
    public  String course_name;         //编译原理，课程名称
    public  String course_nature;       //必修课，课程性质
    public  String course_attribute;       //“”，课程归属
    public  String credit;              //4.0，学分
    public  String grade_point;         //1.30，绩点
    public  String grade;               //63，成绩
    public  String minor_mark;          //0，辅修标记
    public  String repair_code;         //补考成绩
    public  String restudy_code;        //重修成绩

    public  String qzcj;                //期中成绩
    public  String qmcj;                //期末成绩
    public  String pscj;                //平时成绩

    public  ScoreModel(Builder builder)
    {
        this.title=builder.title;
        this.college=builder.college;
        this.semester=builder.semester;
        this.course_code=builder.course_code;
        this.course_name=builder.course_name;
        this.course_nature=builder.course_nature;
        this.course_attribute=builder.course_attribute;
        this.credit=builder.credit;
        this.grade_point=builder.grade_point;
        this.grade=builder.grade;
        this.minor_mark=builder.minor_mark;
        this.repair_code=builder.repair_code;
        this.restudy_code=builder.restudy_code;
        this.pscj=builder.pscj;
        this.qzcj=builder.qzcj;
        this.qmcj=builder.qmcj;
    }
    public  static  class  Builder
    {
        private  String title;
        private  String college;
        private   String semester;
        private  String course_code;
        private  String course_name;
        private  String course_nature;
        private  String course_attribute;
        private  String credit;
        private  String grade_point;
        private  String grade;
        private  String minor_mark;
        private  String repair_code;
        private  String restudy_code;
        private  String qzcj="";                //期中成绩
        private  String qmcj="";                //期末成绩
        private  String pscj="";                //平时成绩
        public  Builder title(String title)
        {
            this.title=title;
            return  this;
        }
        public Builder college(String college)
        {
            this.college=college;
            return  this;
        }
        public  Builder semester(String semester)
        {
            this.semester=semester;
            return  this;
        }
        public  Builder course_code(String course_code)
        {
            this.course_code=course_code;
            return  this;
        }
        public  Builder course_name(String course_name)
        {
            this.course_name=course_name;
            return  this;
        }  public  Builder course_nature(String course_nature)
        {
            this.course_nature=course_nature;
            return  this;
        }
        public  Builder course_attribute(String course_attribute)
        {
            this.course_attribute=course_attribute;
            return  this;
        }
        public  Builder credit(String credit)
        {
            this.credit=credit;
            return  this;
        }
        public  Builder grade_point(String grade_point)
        {
            this.grade_point=grade_point;
            return  this;
        }
        public  Builder grade(String grade)
        {
            this.grade=grade;
            return  this;
        }
        public  Builder minor_mark(String minor_mark)
        {
            this.minor_mark=minor_mark;
            return  this;
        }
        public  Builder repair_code(String repair_code)
        {
            this.repair_code=repair_code;
            return  this;
        }
        public  Builder restudy_code(String restudy_code)
        {
            this.restudy_code=restudy_code;
            return  this;
        }
        public  Builder qzcj(String qzcj)
        {
            this.qzcj=qzcj;
            return  this;
        }
        public  Builder qmcj(String qmcj)
        {
            this.qmcj=qmcj;
            return  this;
        }
        public  Builder pscj(String pscj)
        {
            this.pscj=pscj;
            return  this;
        }

        public ScoreModel build()
        {
            return new ScoreModel(this);
        }

    }

}
