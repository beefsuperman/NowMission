package com.kunyang.android.nowmission.gson;

/**
 * Created by 坤阳 on 2017/9/11.
 */

public class Suggestion {

    public Comf comf;

    public class Comf {
        public String brf;
        public String text;
    }
    public Drsg mDrsg;

    public class Drsg {
        public String brf;
        public String txt;
    }
    public Sport mSport;

    public class Sport {
        public String brf;
        public String txt;
    }
    public Trav mTrav;

    public class Trav {
        public String brf;
        public String txt;
    }
    public Uv mUv;

    private class Uv {
        public String brf;
        public String txt;
    }
}
