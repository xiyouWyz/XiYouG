package com.example.wyz.xiyoug.Model;

import android.widget.TextView;

import java.util.List;

/**
 * Created by Wyz on 2016/7/25.
 */
public class Book_Detail {

    private String id;
    private String  pub;
    private String  summary;
    private String title;
    private String form;
    private String author;
    private String sort;
    private String subject;
    private String rentTimes;
    private String favTimes;
    private String browseTimes;
    private String total;
    private String avaliable ;
    private List<Book_CirculationInfo> book_circulationInfos;
    private List<Book_ReferBooks> book_referBookses;
    private String img;

    public Book_Detail(String id,String pub, String summary, String title, String form, String author, String sort, String subject, String rentTimes, String favTimes, String browseTimes, String total, String avaliable,List<Book_CirculationInfo> book_circulationInfos, List<Book_ReferBooks> book_referBookses,String img ) {
        this.id=id;
        this.author = author;
        this.avaliable = avaliable;
        this.book_circulationInfos = book_circulationInfos;
        this.book_referBookses = book_referBookses;
        this.favTimes = favTimes;
        this.browseTimes = browseTimes;
        this.form = form;
        this.sort = sort;
        this.pub = pub;
        this.rentTimes = rentTimes;
        this.summary = summary;
        this.subject = subject;
        this.title = title;
        this.total = total;
        this.img=img;
    }

    public List<Book_CirculationInfo> getBook_circulationInfos() {
        return book_circulationInfos;
    }

    public void setBook_circulationInfos(List<Book_CirculationInfo> book_circulationInfos) {
        this.book_circulationInfos = book_circulationInfos;
    }

    public List<Book_ReferBooks> getBook_referBookses() {
        return book_referBookses;
    }

    public void setBook_referBookses(List<Book_ReferBooks> book_referBookses) {
        this.book_referBookses = book_referBookses;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }



    public String getAvaliable() {
        return avaliable;
    }

    public void setAvaliable(String avaliable) {
        this.avaliable = avaliable;
    }

    public String getBrowseTimes() {
        return browseTimes;
    }

    public void setBrowseTimes(String browseTimes) {
        this.browseTimes = browseTimes;
    }

    public String getFavTimes() {
        return favTimes;
    }

    public void setFavTimes(String favTimes) {
        this.favTimes = favTimes;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getPub() {
        return pub;
    }

    public void setPub(String pub) {
        this.pub = pub;
    }

    public String getRentTimes() {
        return rentTimes;
    }

    public void setRentTimes(String rentTimes) {
        this.rentTimes = rentTimes;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
