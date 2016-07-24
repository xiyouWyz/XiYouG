package com.example.wyz.xiyoug.Model;

/**
 * Created by Wyz on 2016/7/24.
 */
public class Book_HistoryBor {
    private  String  title;
    private  String type;
    private  String date;

    public Book_HistoryBor(String title, String barcode, String date ) {
        this.type = barcode;
        this.date = date;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
