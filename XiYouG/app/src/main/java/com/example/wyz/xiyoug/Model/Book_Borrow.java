package com.example.wyz.xiyoug.Model;

/**
 * Created by Wyz on 2016/7/23.
 */
public class Book_Borrow {
    private  String title;
    private  String department;
    private  String state;
    private  boolean canRenew;
    private  String date;

    private  String barcode;
    private  String department_id;
    private  String library_id;
    public Book_Borrow( String title,String barcode,String department,  String state, String date,boolean canRenew,String department_id,String library_id) {
        this.canRenew = canRenew;
        this.date = date;
        this.department = department;
        this.state = state;
        this.title = title;
        this.barcode=barcode;
        this.department_id=department_id;
        this.library_id=library_id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean getCanRenew() {
        return canRenew;
    }

    public void setCanRenew(boolean canRenew) {
        this.canRenew = canRenew;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(String department_id) {
        this.department_id = department_id;
    }

    public String getLibrary_id() {
        return library_id;
    }

    public void setLibrary_id(String library_id) {
        this.library_id = library_id;
    }
}
