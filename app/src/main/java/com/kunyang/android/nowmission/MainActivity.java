package com.kunyang.android.nowmission;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.kunyang.android.nowmission.db.City;
import com.kunyang.android.nowmission.db.Country;
import com.kunyang.android.nowmission.db.Province;
import com.kunyang.android.nowmission.gson.AQI;
import com.kunyang.android.nowmission.gson.Weather;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int LEVEL_PROVINCE=0;
    private static final int LEVEL_CITY=1;
    private static final int LEVEL_COUNTY=2;
    private static final int LEVEL_SEARCH=3;

    private LayoutInflater m_inflater;
    private ListViewAdapter mListViewAdapter;

    private TextView titleText;
    private ListView mListView;
    private ArrayAdapter<String> adapter;
    private List<Province> mProvinceList;
    private ProvinceDao dao;
    private CityDao mCityDao;
    private CountryDao mCountryDao;

    private Button backButton;

    private List<String> pList;


    private List<City> getCityList;
    private List<City> mCityList;
    private List<Country> mCountryList;
    private List<City> cList=new ArrayList<City>();
    private List<Country> ccList=new ArrayList<Country>();

    private Province selectedProvince;
    private City selectedCity;
    private Country selectedCountry;

    private int currentLevel;

    private DrawerLayout drawerLayout;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        if (mToolbar != null) {setSupportActionBar(mToolbar);}

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, mToolbar, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerOpened(View v) {        super.onDrawerOpened(v); }
                @Override
                public void onDrawerClosed(View v) {        super.onDrawerClosed(v); }
                };
                drawerLayout.setDrawerListener(toggle);
                toggle.syncState();

        m_inflater = LayoutInflater.from(this);
        pList=new ArrayList<>();
        mListView=(ListView)this.findViewById(R.id.province_list);
        titleText=(TextView)this.findViewById(R.id.title_text);
        backButton=(Button)this.findViewById(R.id.back_button);

        dao=new ProvinceDao(MainActivity.this);
        mCityDao=new CityDao(MainActivity.this);
        mCountryDao=new CountryDao(MainActivity.this);
        DatabaseUtil.packDataBase(MainActivity.this);

        mListViewAdapter=new ListViewAdapter(pList);
        mListView.setAdapter(mListViewAdapter);


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (currentLevel==LEVEL_PROVINCE){
                    selectedProvince=mProvinceList.get(i);
                    queryCities();
                }else if (currentLevel==LEVEL_CITY){
                    selectedCity=cList.get(i);
                    queryCounties();
                }else if (currentLevel==LEVEL_COUNTY){
                    String weatherCity;
                    if (ccList.size()==0){
                        weatherCity=selectedCity.getName();
                    }else {
                        selectedCountry=ccList.get(i);
                        weatherCity=selectedCountry.getName();
                    }
                    Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
                    intent.putExtra("select_city",selectedCity.getName());
                    intent.putExtra("weather_city", weatherCity);
                    startActivity(intent);
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

        SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getString("weather",null)!=null){
            Intent intent=new Intent(this,WeatherActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchItem=menu.findItem(R.id.menu_item_search);
        final SearchView searchView=(SearchView)searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                getCityList=new ArrayList<City>();
                getCityList=mCityDao.getCityList();
                cList.clear();
                pList.clear();
                if (newText.length()<4&&newText.length()>0){
                    for (City city:getCityList){
                        String str=PinyinUtils.converterToFirstSpell(city.getName());
                        if (str.indexOf(newText.toUpperCase())!=-1) {
                            pList.add(city.getName());
                            cList.add(city);
                            str=null;
                        }
                    }
                    if (pList.size()==0){
                        for (City city:getCityList){
                            String str1=PinyinUtils.getPingYin(city.getName());
                            if (str1.indexOf(newText.toLowerCase())!=-1) {
                                pList.add(city.getName());
                                cList.add(city);
                                str1=null;
                            }
                        }
                    }
                }else if (newText.length()>=4){
                    pList.clear();
                    cList.clear();
                    for (City city:getCityList){
                        String str1=PinyinUtils.getPingYin(city.getName());
                        if (str1.indexOf(newText.toLowerCase())!=-1) {
                            pList.add(city.getName());
                            cList.add(city);
                            str1=null;
                        }
                    }
                }else if (newText.length()==0){
                    initProvinceList();
                }
                ccList.clear();
                mListViewAdapter.notifyDataSetChanged();
                mListView.setSelection(0);
                currentLevel=LEVEL_CITY;
                return false;
            }
        });

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.menu_item_clear:
                initProvinceList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void queryCounties() {
        mCountryList =new ArrayList<Country>();
        prepareCountyList();
    }

    private void prepareCountyList() {
        mToolbar.setTitle("当前地区:"+selectedCity.getName());
        backButton.setVisibility(View.VISIBLE);
        pList.clear();
        mCountryList =mCountryDao.getCountryList();
        for (Country country : mCountryList){
            if (selectedCity.getCode()/100== country.getCode()/100){
                pList.add(country.getName());
                ccList.add(country);
            }
        }
        if (pList.size()==0){
            mToolbar.setTitle("当前地区:"+selectedProvince.getProvinceName());
            for (City city:cList){
                pList.add(city.getName());
            }
        }
        mListViewAdapter.notifyDataSetChanged();
        mListView.setSelection(0);
        currentLevel=LEVEL_COUNTY;
    }

    private void initProvinceList(){
        mProvinceList=new ArrayList<Province>();
        prepareProvinceList();
    }

    private void prepareProvinceList() {
        mToolbar.setTitle("这里是祖国");
        backButton.setVisibility(View.GONE);
        pList.clear();
        cList.clear();
        ccList.clear();
        mProvinceList=dao.getProvinceList();
        for (Province province:mProvinceList){
            pList.add(province.getProvinceName());
        }
        mListViewAdapter.notifyDataSetChanged();
        mListView.setSelection(0);
        currentLevel=LEVEL_PROVINCE;
    }

    private void queryCities(){
        mCityList=new ArrayList<City>();
        prepareCityList();
    }

    private void prepareCityList() {
        mToolbar.setTitle("当前地区:"+selectedProvince.getProvinceName());
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
        mListViewAdapter.notifyDataSetChanged();
        mListView.setSelection(0);
        currentLevel=LEVEL_CITY;
    }

    public class ListViewAdapter extends BaseAdapter {

        List<String> beans = new ArrayList<String>();

        public ListViewAdapter(List<String> beans) {
            this.beans = beans;
        }
        @Override
        public int getCount() {
            return beans.size();
        }

        @Override
        public Object getItem(int i) {
            return beans.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup parent) {
            View tv;
            TextView t;

            if (convertView==null){
                tv=m_inflater.inflate(R.layout.city_item,parent,false);
            }else {
                tv=convertView;
            }
            try {
                t=(TextView)tv.findViewById(R.id.city_name);
                t.setText(pList.get(i));
            }catch (Exception e){
                e.printStackTrace();
            }
            return tv;
        }


    }



}
