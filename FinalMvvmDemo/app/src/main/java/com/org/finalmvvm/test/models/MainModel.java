package com.org.finalmvvm.test.models;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.org.finalmvvm.Interceptor.Model;
import com.org.finalmvvm.test.beans.Weather;


import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;

@Model
public class MainModel{

    /**
     * 从服务器获取天气数据，对数据进行基本处理，转成需要的格式(如json，bean，string等)
     * 不需要多线程，直接同步执行就行了
     */
    public Weather loadWeatherFromUrl(){
        String jsonStr=doHttp("http://www.weather.com.cn/data/cityinfo/101010100.html");
        JSONObject jo=JSON.parseObject(jsonStr);
        JSONObject weatherinfo=jo.getJSONObject("weatherinfo");
        Weather weather=weatherinfo.toJavaObject(Weather.class);
        return weather;
    }







    /**
     * 执行http请求
     * 这里只是为演示使用，建议自己使用okhttp等框架
     * 需要同步执行，不需要使用框架内的异步操作
     */
    public static String doHttp(String urlStr) {
        try {
            URL u = new URL(urlStr);
            InputStream in = u.openStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                byte buf[] = new byte[1024];
                int read;
                while ((read = in.read(buf)) > 0) {
                    out.write(buf, 0, read);
                }
            } finally {
                if (in != null) {
                    in.close();
                }
            }
            byte b[] = out.toByteArray();
            String result=new String(b, "utf-8");
            return result;
        }catch (Exception e){
            e.printStackTrace();
        }
        return "aaaaa";
    }



}
