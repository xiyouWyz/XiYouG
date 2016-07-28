package com.example.wyz.xiyoug.Model;

/**
 * Created by Wyz on 2016/7/25.
 */
public class Book_Search {
    private String title;
    private String id;
    private String author;

    public Book_Search(String id, String title,String author) {
        this.author = author;
        this.id = id;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
