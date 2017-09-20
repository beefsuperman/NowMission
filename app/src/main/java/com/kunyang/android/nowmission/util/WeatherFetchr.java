package com.kunyang.android.nowmission.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 坤阳 on 2017/9/19.
 */

public class WeatherFetchr {
    public byte[] request(String urlSpec)throws IOException{
        URL url=new URL(urlSpec);
        HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
        try {
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String responseStr = "";
            String readLine = null;
            while((readLine=bufferedReader.readLine())!=null){
                responseStr = responseStr + readLine;
            }
            inputStream.close();
            bufferedReader.close();
            return responseStr.getBytes();
        }finally {
            httpURLConnection.disconnect();
        }
    }

    public String getUrlString(String urlSpec)throws IOException{
        return new String(request(urlSpec));
    }
}
