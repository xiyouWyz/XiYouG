package com.example.wyz.xiyoug.Model;

/**
 * Created by Wyz on 2016/8/10.
 */
public class ScoreFailedModel {
    public  String course_code;
    public  String course_name;
    public  String course_nature;
    public  String credit;
    public  String best_score;
    public ScoreFailedModel(Builder builder)
    {
        this.course_code=builder.course_code;
        this.course_name=builder.course_name;
        this.course_nature=builder.course_nature;
        this.credit=builder.credit;
        this.best_score=builder.best_score;
    }
    public  static  class  Builder
    {
        private  String course_code;
        private  String course_name;
        private  String course_nature;
        private   String credit;
        private  String best_score;
        public Builder course_code(String course_code)
        {
            this.course_code=course_code;
            return  this;
        }
        public Builder course_name(String course_name)
        {
            this.course_name=course_name;
            return  this;
        }
        public Builder course_nature(String course_nature)
        {
            this.course_nature=course_nature;
            return  this;
        }
        public Builder credit(String credit)
        {
            this.credit=credit;
            return  this;
        }
        public Builder best_score(String best_score)
        {
            this.best_score=best_score;
            return  this;
        }
        public ScoreFailedModel build()
        {
            return  new ScoreFailedModel(this);
        }

    }
}
