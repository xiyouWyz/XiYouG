package com.example.wyz.xiyoug.Util;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by Wyz on 2016/8/1.
 */
public class SerializableMap  implements Serializable{
    private Map<String,List<String>> map;


    public Map<String, List<String>> getMap() {
        return map;
    }

    public void setMap(Map<String, List<String>> map) {
        this.map = map;
    }
}
