package com.kunyang.android.nowmission;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kunyang.android.nowmission.db.Country;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 坤阳 on 2017/9/10.
 */

public class CountryDao {

    private SSXDBHelper mHelper;
    private SQLiteDatabase db;

    public CountryDao(Context context){
        mHelper=new SSXDBHelper(context);
    }

    public List<Country> getCountryList(){
        List<Country> countryList =new ArrayList<Country>();
        try {
            db=mHelper.getReadableDatabase();

            Cursor cursor=db.rawQuery("select * from country",null);
            if (null!=cursor){
                while (cursor.moveToNext()){
                    int code=cursor.getInt(cursor.getColumnIndex("code"));
                    String name=cursor.getString(cursor.getColumnIndex("name"));
                    int city_id=cursor.getInt(cursor.getColumnIndex("city_id"));
                    int id=cursor.getInt(cursor.getColumnIndex("id"));
                    Country country =new Country();
                    country.setId(id);
                    country.setCode(code);
                    country.setName(name);
                    country.setCity_id(city_id);

                    countryList.add(country);
                }
            }
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (null!=db){
                db.close();
            }
        }
        return countryList;
    }
}
