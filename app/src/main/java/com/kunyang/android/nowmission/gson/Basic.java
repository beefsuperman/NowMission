package com.kunyang.android.nowmission.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 坤阳 on 2017/9/11.
 */

public class Basic {

    public String city;

    public Update update;

    public class Update {
        @SerializedName("loc")
        public String updateTime;
    }
}
