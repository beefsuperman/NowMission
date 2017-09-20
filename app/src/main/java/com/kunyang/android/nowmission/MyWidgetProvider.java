package com.kunyang.android.nowmission;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.RemoteViews;

import com.kunyang.android.nowmission.gson.Weather;
import com.kunyang.android.nowmission.util.HttpUtil;
import com.kunyang.android.nowmission.util.Utility;
import com.kunyang.android.nowmission.util.WeatherFetchr;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 坤阳 on 2017/9/14.
 */

public class MyWidgetProvider extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds){
        // TODO Auto-generated method stub
        //显示时间的定时器，每秒刷新一次
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MyTime(context, appWidgetManager), 1, 1000);
        //显示天气的定时器，设置为没小时刷新一次
        Timer wTimer = new Timer();
        wTimer.scheduleAtFixedRate(new MyWeather(context, appWidgetManager), 1, 3600000);
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
    @Override
    public void onReceive(Context context, Intent intent)
    {
        super.onReceive(context, intent);
    }

    public class MyTime extends TimerTask {
        RemoteViews remoteViews;
        AppWidgetManager appWidgetManager;
        ComponentName thisWidget;
        DateFormat format = SimpleDateFormat.getTimeInstance(SimpleDateFormat.MEDIUM, Locale.getDefault());
        public MyTime(Context context, AppWidgetManager appWidgetManager) {
            this.appWidgetManager = appWidgetManager;
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.weather_widget);
            thisWidget = new ComponentName(context, MyWidgetProvider.class);
        }
        @Override
        public void run() {
            remoteViews.setTextViewText(R.id.time, format.format(new Date()));
            appWidgetManager.updateAppWidget(thisWidget, remoteViews);
        }
    }

    //显示天气信息处理
    public class MyWeather extends TimerTask {
        RemoteViews remoteViews;
        AppWidgetManager appWidgetManager;
        ComponentName thisWidget;
        public MyWeather(Context context, AppWidgetManager appWidgetManager) {
            this.appWidgetManager = appWidgetManager;
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.weather_widget);
            thisWidget = new ComponentName(context, MyWidgetProvider.class);
        }
        @Override
        public void run() {
            String temp = "";
            String city = "惠州市";
            String image_url="";
            try {
                //此处笔者用的是“易源接口”提供的API，下面的***分别代表你应用的appid和secretid
                final String info1="https://free-api.heweather.com/v5/weather?city=" + city + "&key=a960da1bb9f64f289b4432445e0c1974";
                //获取接口返回的信息

                image_url ="https://cdn.heweather.com/cond_icon/103.png";
                Bitmap weather_pic = getBitmap(image_url);
                System.out.println("temp:"+temp);
                remoteViews.setTextViewText(R.id.temperature, city +":"+ temp);
                if(weather_pic!=null){
                    remoteViews.setImageViewBitmap(R.id.image,weather_pic);
                }
                appWidgetManager.updateAppWidget(thisWidget, remoteViews);
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        //根据天气图标url从网络获取图片
        public Bitmap getBitmap(String path) throws IOException {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            if(conn.getResponseCode() == 200){
                InputStream inputStream = conn.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }
            return null;
        }
    }


    public String getUrlStrings(String urlSpec)throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        InputStream inputStream = httpURLConnection.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String responseStr = "";
        String readLine = null;
        while((readLine=bufferedReader.readLine())!=null){
            responseStr = responseStr + readLine;
        }
        inputStream.close();
        bufferedReader.close();
        httpURLConnection.disconnect();
        return responseStr;
    }
    public  String getUrlString(String urlSpec)throws IOException{
        return new String(getUrlStrings(urlSpec));
    }
}
