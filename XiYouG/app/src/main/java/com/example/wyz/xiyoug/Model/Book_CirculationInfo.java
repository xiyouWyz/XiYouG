package com.example.wyz.xiyoug.Model;

/**
 * Created by Wyz on 2016/7/25.
 */
public class Book_CirculationInfo {
    private String barcode;
    private String sort;
    private String department;
    private String statue;
    private String date;

    public Book_CirculationInfo(String barcode, String sort, String department, String statue, String date) {
        this.department = department;
        this.sort = sort;
        this.statue = statue;
        this.date = date;
        this.barcode = barcode;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getStatue() {
        return statue;
    }

    public void setStatue(String statue) {
        this.statue = statue;
    }
}
