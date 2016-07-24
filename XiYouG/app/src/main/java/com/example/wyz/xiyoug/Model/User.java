package com.example.wyz.xiyoug.Model;

/**
 * Created by Wyz on 2016/7/23.
 */
public class User {
    private User()
    {
    }
    private static User user=new User();
    public  static User  getInstance(String id,String name,String department,String debt)
    {
        user.setId(id);
        user.setName(name);
        user.setDepartment(department);
        user.setDebt(debt);
        return  user;
    }
    public  static  User getInstance()
    {
        return user;
    }
    private  String id;
    private  String name;
    private String  department;
    private  String  debt;

    public String getDebt() {
        return debt;
    }

    public void setDebt(String debt) {
        this.debt = debt;
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
