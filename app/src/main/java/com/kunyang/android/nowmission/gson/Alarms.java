package com.kunyang.android.nowmission.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 坤阳 on 2017/9/11.
 */

public class Alarms {
    public String level;
    public String stat;
    public String title;
    public String txt;

    @SerializedName("type")
    public String airpower;
}
