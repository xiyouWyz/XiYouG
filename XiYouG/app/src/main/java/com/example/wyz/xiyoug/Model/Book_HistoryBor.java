package com.example.wyz.xiyoug.Model;

/**
 * Created by Wyz on 2016/7/24.
 */
public class Book_HistoryBor {
    private  String  title;
    private  String type;
    private  String date;
    private  String barcode;

    public Book_HistoryBor(String title, String barcode,String type, String date ) {
        this.type = type;
        this.date = date;
        this.title = title;
        this.barcode=barcode;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
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
