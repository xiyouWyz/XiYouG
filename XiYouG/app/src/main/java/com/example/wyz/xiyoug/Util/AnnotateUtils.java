package com.example.wyz.xiyoug.Util;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.wyz.xiyoug.R;
import com.example.wyz.xiyoug.Viewer.ScheduleFragment;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Created by Wyz on 2016/8/8.
 */
public class AnnotateUtils  {
    private  final static String TAG="AnnotateUtils";
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public  @interface ViewInject
    {
        int id();
    }
    public  @interface  setData
    {

    }
    public  static  void injectViews(ScheduleFragment context)
    {
        Class object=context.getClass();

        Field[] fields=object.getDeclaredFields();
        for(Field field:fields)
        {
            ViewInject viewInject=field.getAnnotation(ViewInject.class);
            if(viewInject!=null)
            {
                int viewid=viewInject.id();
                if(viewid!=-1)
                {
                    try
                    {
                      /*  Method method=object.getMethod("findViewById",int.class);
                        Object resView=method.invoke(context,viewid);*/
                        field.setAccessible(true);

                        field.set(context,context.view.findViewById(viewid));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }
    public  static  void setDataViews(Activity activity, Map<String,List<String>> stringListMap,List<String> time , List<Integer> day, List<Integer> color)
    {
        Class<? extends  Activity> object=activity.getClass();
        Field[] fields=object.getDeclaredFields();
        int i=0;
        Log.d(TAG,String.valueOf(fields.length));
        for(Field field:fields)
        {
            ViewInject viewInject=field.getAnnotation(ViewInject.class);
            if(viewInject!=null)
            {
                int viewid=viewInject.id();
                if(viewid!=-1)
                {
                    try
                    {
                        Method method=object.getMethod("findViewById",int.class);
                        Object resView=method.invoke(activity,viewid);
                        TextView textView=(TextView)resView;
                        String str=stringListMap.get(time.get(i)).get(day.get(i));
                        if(str.equals(""))
                        {
                            textView.setBackgroundColor(activity.getResources().getColor(R.color.schedule_none));
                            i++;
                        }
                        else
                        {
                            textView.setText(str);
                            textView.setBackgroundColor(activity.getResources().getColor(color.get(i)));
                            i++;
                        }


                        field.setAccessible(true);
                        field.set(activity,resView);
                    }catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }

                }
            }
        }

    }


}
