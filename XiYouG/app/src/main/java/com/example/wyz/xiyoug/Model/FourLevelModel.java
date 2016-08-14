package com.example.wyz.xiyoug.Model;

/**
 * Created by Wyz on 2016/8/14.
 */
public class FourLevelModel {
    public String name;
    public String school;
    public String type;
    public String account;
    public String time;
    public String total_grade;
    public String listen_grade;
    public String read_grade;
    public String write_grade;
    public FourLevelModel(Builder builder)
    {
        this.name=builder.name;
        this.school=builder.school;
        this.type=builder.type;
        this.account=builder.account;
        this.time=builder.time;
        this.total_grade=builder.total_grade;
        this.listen_grade=builder.listen_grade;
        this.read_grade=builder.read_grade;
        this.write_grade=builder.write_grade;
    }

    public  static  class  Builder
    {
        private String name;
        private String school;
        private String type;
        private String account;
        private String time;
        private String total_grade;
        private String listen_grade;
        private String read_grade;
        private String write_grade;
        public Builder name(String name)
        {
            this.name=name;
            return  this;
        }
        public Builder school(String school)
        {
            this.school=school;
            return  this;
        }
        public Builder type(String type)
        {
            this.type=type;
            return  this;
        }
        public Builder account(String account)
        {
            this.account=account;
            return  this;
        }
        public Builder time(String time)
        {
            this.time=time;
            return  this;
        }
        public Builder total_grade(String total_grade)
        {
            this.total_grade=total_grade;
            return  this;
        }
        public Builder listen_grade(String listen_grade)
        {
            this.listen_grade=listen_grade;
            return  this;
        }
        public Builder read_grade(String read_grade)
        {
            this.read_grade=read_grade;
            return  this;
        }
        public Builder write_grade(String write_grade)
        {
            this.write_grade=write_grade;
            return  this;
        }
        public FourLevelModel builder()
        {
            return  new FourLevelModel(this);
        }
    }
}
