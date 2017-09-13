package com.kunyang.android.nowmission.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 坤阳 on 2017/9/11.
 */

public class Now {

    public Cond cond;

    public class Cond {
        public String txt;
    }
    public String hum;
    public String tmp;
    public Wind wind;

    public class Wind {
        public String deg;
        public String dir;
        @SerializedName("spd")
        public String speed;
    }
}
