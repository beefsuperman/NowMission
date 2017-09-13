package com.kunyang.android.nowmission.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 坤阳 on 2017/9/11.
 */

public class Forecast {
    public Cond mCond;

    private class Cond {
        public String txt_d;
    }

    public String date;
    @SerializedName("tmp")
    public Temperature mTemperature;

    private class Temperature {

        public String max;
        public String min;
    }

    public String hum;

    public Wind mWind;

    private class Wind {
        public String deg;
        public String dir;
        @SerializedName("spd")
        public String speed;
    }
}
