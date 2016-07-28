package com.example.wyz.xiyoug.Model;

/**
 * Created by Wyz on 2016/7/24.
 */
public class Book_Collection {
    private  String title;
    private  String pub;
    private  String sort;
    private  String id;
    private  String images;

    public Book_Collection(String title, String pub , String sort,String id,String images) {
        this.title = title;
        this.sort = sort;
        this.pub = pub;
        this.images=images;
        this.id=id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPub() {
        return pub;
    }

    public void setPub(String pub) {
        this.pub = pub;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
