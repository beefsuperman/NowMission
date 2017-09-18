package com.kunyang.android.nowmission.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 坤阳 on 2017/9/11.
 */

public class Forecast {

    public Cond cond;

    public class Cond {
        public String txt_d;
    }

    public String date;

    public Tmp tmp;

    public class Tmp {

        public String max;
        public String min;
    }

    public Wind wind;

    public class Wind {
        public String spd;
    }
}
