package com.example.wyz.xiyoug.Util;

import com.example.wyz.xiyoug.Model.HttpLinkHeader;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.Response;

/**
 * Created by Wyz on 2016/8/31.
 */
public class NoHttpUtil {

    public static   String GetClassRoom()
    {
        Request request=NoHttp.createStringRequest(HttpLinkHeader.CLASSROOM, RequestMethod.GET);
        Response response=NoHttp.startRequestSync(request);
        if(response.isSucceed())
        {
             return  response.toString();
        }
        return  "";
    }
}
