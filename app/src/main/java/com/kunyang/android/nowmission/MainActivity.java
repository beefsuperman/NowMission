package com.kunyang.android.nowmission;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.kunyang.android.nowmission.db.Province;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;
    private ArrayAdapter<String> adapter;
    private List<Province> mProvinceList;
    private ProvinceDao dao;
    private List<String> pList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pList=new ArrayList<>();
        mListView=(ListView)this.findViewById(R.id.province_list);

        dao=new ProvinceDao(MainActivity.this);
        DatabaseUtil.packDataBase(MainActivity.this);
        initProvinceList();
        adapter=new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,pList);
        mListView.setAdapter(adapter);
    }

    private void initProvinceList(){
        mProvinceList=new ArrayList<Province>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                prepareProvinceList();
            }
        }).start();
    }

    private void prepareProvinceList() {
        mProvinceList=dao.getProvinceList();
        for (Province province:mProvinceList){
            pList.add(province.getProvinceName());
        }
    }

}
