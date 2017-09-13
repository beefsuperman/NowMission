package com.kunyang.android.nowmission.util;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.kunyang.android.nowmission.db.City;
import com.kunyang.android.nowmission.db.Country;
import com.kunyang.android.nowmission.db.Province;
import com.kunyang.android.nowmission.gson.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 坤阳 on 2017/4/20.
 */

public class Utility {

    public static Weather handleWeatherResponse(String response){
        try {
            JSONObject jsonObject=new JSONObject(response);
            JSONArray jsonArray=jsonObject.getJSONArray("HeWeather5");
            String weatherContent=jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent,Weather.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
