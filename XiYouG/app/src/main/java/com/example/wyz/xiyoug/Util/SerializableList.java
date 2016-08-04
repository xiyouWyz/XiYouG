package com.example.wyz.xiyoug.Util;

import android.app.SearchableInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Wyz on 2016/8/4.
 */
public class SerializableList implements Serializable{
    private List<String> stringList;

    public List<String> getStringList() {
        return stringList;
    }

    public void setStringList(List<String> stringList) {
        this.stringList = stringList;
    }
}
