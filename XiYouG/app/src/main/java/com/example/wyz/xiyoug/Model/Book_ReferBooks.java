package com.example.wyz.xiyoug.Model;

/**
 * Created by Wyz on 2016/7/25.
 */
public class Book_ReferBooks {
    private  String id;
    private  String title;
    private String author;

    public Book_ReferBooks(String id, String title, String author) {
        this.author = author;
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
