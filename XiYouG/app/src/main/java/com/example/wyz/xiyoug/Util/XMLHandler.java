package com.example.wyz.xiyoug.Util;

import android.util.Xml;

import com.example.wyz.xiyoug.Model.ScoreModel;
import com.example.wyz.xiyoug.Viewer.ScoreMyFragment;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by WYZ on 2017/1/19.
 */
public class XMLHandler {
    public static List<ScoreModel> parse(String xmlData,List<ScoreModel> scoreModels) throws Exception{
        ScoreModel scoreModel=null;
        XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
        // 由android.util.Xml创建一个XmlPullParser实例
        XmlPullParser xpp = factory.newPullParser();
        // 设置输入流 并指明编码方式
        xpp.setInput(new StringReader(xmlData));
        // 产生第一个事件
        int eventType = xpp.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT){
            switch (eventType) {
                // 判断当前事件是否为文档开始事件
                case XmlPullParser.START_DOCUMENT:
                   // 初始化scores集合
                    break;
                // 判断当前事件是否为标签元素开始事件
                case XmlPullParser.START_TAG:
                    if (xpp.getName().equals("KCMC")) {
                        eventType = xpp.next();
                        // 判断开始标签元素是否是book
                        scoreModel=getScoreModel(scoreModels,xpp.getText());
                    } else if (xpp.getName().equals("PSCJ")) {
                        eventType = xpp.next();//让解析器指向name属性的值
                        // 得到name标签的属性值，并设置beauty的name
                        if(scoreModel!=null){
                            scoreModel.pscj=xpp.getText();
                        }
                    } else if (xpp.getName().equals("QZCJ")) { // 判断开始标签元素是否是book
                        eventType = xpp.next();//让解析器指向age属性的值
                        // 得到age标签的属性值，并设置beauty的age
                        if(scoreModel!=null){
                            scoreModel.qzcj=xpp.getText();
                        }
                    }
                    else if (xpp.getName().equals("QMCJ")) { // 判断开始标签元素是否是book
                        eventType = xpp.next();//让解析器指向age属性的值
                        // 得到age标签的属性值，并设置beauty的age
                        if(scoreModel!=null){
                            scoreModel.qmcj=xpp.getText();
                        }
                    }
                    break;

                // 判断当前事件是否为标签元素结束事件
                case XmlPullParser.END_TAG:
                    if (xpp.getName().equals("beauty")) { // 判断结束标签元素是否是book
                       // mList.add(beauty); // 将book添加到books集合
                        //beauty = null;
                    }
                    break;
            }
            // 进入下一个元素并触发相应事件
            eventType = xpp.next();
        }
        return scoreModels;

    }
    private  static  ScoreModel getScoreModel(List<ScoreModel> scoreModels,String kcmc){
        for(int i=0;i<scoreModels.size();i++){
            if(kcmc.equals(scoreModels.get(i).course_name)){
                return scoreModels.get(i);
            }
        }
        return  null;
    }
}
