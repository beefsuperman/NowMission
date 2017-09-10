package com.kunyang.android.nowmission;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.kunyang.android.nowmission.db.City;
import com.kunyang.android.nowmission.db.Country;
import com.kunyang.android.nowmission.db.Province;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int LEVEL_PROVINCE=0;
    private static final int LEVEL_CITY=1;
    private static final int LEVEL_COUNTY=2;

    private TextView titleText;
    private ListView mListView;
    private ArrayAdapter<String> adapter;
    private List<Province> mProvinceList;
    private ProvinceDao dao;
    private CityDao mCityDao;
    private CountryDao mCountryDao;

    private Button backButton;

    private List<String> pList;

    private List<City> mCityList;
    private List<Country> mCountryList;
    private List<City> cList=new ArrayList<City>();

    private Province selectedProvince;
    private City selectedCity;

    private int currentLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pList=new ArrayList<>();
        mListView=(ListView)this.findViewById(R.id.province_list);
        titleText=(TextView)this.findViewById(R.id.title_text);
        backButton=(Button)this.findViewById(R.id.back_button);

        dao=new ProvinceDao(MainActivity.this);
        mCityDao=new CityDao(MainActivity.this);
        mCountryDao=new CountryDao(MainActivity.this);
        DatabaseUtil.packDataBase(MainActivity.this);


        adapter=new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,pList);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (currentLevel==LEVEL_PROVINCE){
                    selectedProvince=mProvinceList.get(i);
                    queryCities();
                }else if (currentLevel==LEVEL_CITY){
                    selectedCity=cList.get(i);
                    queryCounties();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentLevel==LEVEL_COUNTY){
                    queryCities();
                }else if (currentLevel==LEVEL_CITY){
                    initProvinceList();
                }
            }
        });
        initProvinceList();
    }

    private void queryCounties() {
        mCountryList =new ArrayList<Country>();
        prepareCountyList();
    }

    private void prepareCountyList() {
        titleText.setText("当前市区:"+selectedCity.getName());
        backButton.setVisibility(View.VISIBLE);
        pList.clear();
        mCountryList =mCountryDao.getCountryList();
        for (Country country : mCountryList){
            if (selectedCity.getCode()/100== country.getCode()/100){
                pList.add(country.getName());
            }
        }
        if (pList.size()==0){
            titleText.setText("当前市区:"+selectedProvince.getProvinceName());
            for (City city:cList){
                pList.add(city.getName());
            }
        }
        adapter.notifyDataSetChanged();
        mListView.setSelection(0);
        currentLevel=LEVEL_COUNTY;
    }

    private void initProvinceList(){
        mProvinceList=new ArrayList<Province>();
        prepareProvinceList();
    }

    private void prepareProvinceList() {
        titleText.setText("这里是祖国");
        backButton.setVisibility(View.GONE);
        pList.clear();
        mProvinceList=dao.getProvinceList();
        for (Province province:mProvinceList){
            pList.add(province.getProvinceName());
        }
        adapter.notifyDataSetChanged();
        mListView.setSelection(0);
        currentLevel=LEVEL_PROVINCE;
    }

    private void queryCities(){
        mCityList=new ArrayList<City>();
        prepareCityList();
    }

    private void prepareCityList() {
        titleText.setText("当前地区:"+selectedProvince.getProvinceName());
        backButton.setVisibility(View.VISIBLE);
        mCityList=mCityDao.getCityList();
        pList.clear();
        cList.clear();
        int id=selectedProvince.getId();
        for (City city:mCityList){
            if (id==city.getProvince_id()) {
                pList.add(city.getName());
                cList.add(city);
            }
        }
        adapter.notifyDataSetChanged();
        mListView.setSelection(0);
        currentLevel=LEVEL_CITY;
    }

}
