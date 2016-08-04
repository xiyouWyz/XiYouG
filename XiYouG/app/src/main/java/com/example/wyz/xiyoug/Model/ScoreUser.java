package com.example.wyz.xiyoug.Model;

/**
 * Created by Wyz on 2016/8/4.
 */
public class ScoreUser {
    private ScoreUser()
    {
    }
    private static ScoreUser user=new ScoreUser();
    public  static ScoreUser  getInstance(String id,String name,String college,String department)
    {
        user.setId(id);
        user.setName(name);
        user.setDepartment(department);
        user.setCollege(college);
        return  user;
    }
    public  static  ScoreUser getInstance()
    {
        return user;

    }
    public static void Clear()
    {
        user.setId("");
        user.setName("");
        user.setDepartment("");
        user.setCollege("");
    }
    private  String id;
    private  String name;
    private  String college;
    private String  department;


    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
