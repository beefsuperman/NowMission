package com.kunyang.android.nowmission;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kunyang.android.nowmission.db.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 坤阳 on 2017/9/10.
 */

public class ProvinceDao {

    private SSXDBHelper helper;
    private SQLiteDatabase db = null;

    public ProvinceDao(Context mContext){
        helper = new SSXDBHelper(mContext);
    }

    public List<Province> getProvinceList(){
        List<Province> proList = new ArrayList<Province>();
        try{
            db = helper.getReadableDatabase();

            Cursor cursor = db.rawQuery("select * from province", null);
            if(null != cursor){
                while(cursor.moveToNext()){
                    int id=cursor.getInt(cursor.getColumnIndex("id"));
                    int code = cursor.getInt(cursor.getColumnIndex("code"));
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    Province province = new Province();
                    province.setId(id);
                    province.setProvinceCode(code);
                    province.setProvinceName(name);

                    proList.add(province);
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
        return proList;
    }
}

