package com.example.wyz.xiyoug.Model;

/**
 * Created by Wyz on 2016/7/20.
 */
public class Book_Rank {
    private int rank;
    private  String title;
    private  String borNum;
    private  String id;

    public Book_Rank( int rank, String title,String borNum, String id) {
        this.borNum = borNum;
        this.rank = rank;
        this.id = id;
        this.title = title;
    }
    public  int getRank()
    {
        return  rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBorNum() {
        return borNum;
    }

    public void setBorNum(String bornum) {
        this.borNum = bornum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
