package com.kunyang.android.nowmission;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kunyang.android.nowmission.db.City;
import com.kunyang.android.nowmission.db.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 坤阳 on 2017/9/10.
 */

public class CityDao {

    private SSXDBHelper mHelper;
    private SQLiteDatabase db;

    public CityDao(Context context){
        mHelper=new SSXDBHelper(context);
    }

    public List<City> getCityList(){
        List<City> cList=new ArrayList<City>();
        try {
            db=mHelper.getReadableDatabase();

            Cursor cursor=db.rawQuery("select * from city",null);
            if (null!=cursor){
                while (cursor.moveToNext()){
                    int code = cursor.getInt(cursor.getColumnIndex("code"));
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    int province_id=cursor.getInt(cursor.getColumnIndex("province_id"));
                    City city=new City();
                    city.setCode(code);
                    city.setName(name);
                    city.setProvince_id(province_id);

                    cList.add(city);
                }
            }
            cursor.close();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if(null != db){
                db.close();
            }
        }
        return cList;
    }
}
