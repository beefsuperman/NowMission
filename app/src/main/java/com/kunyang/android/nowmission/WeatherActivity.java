package com.kunyang.android.nowmission;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kunyang.android.nowmission.Service.AutoUpdateService;
import com.kunyang.android.nowmission.gson.Weather;
import com.kunyang.android.nowmission.util.HttpUtil;
import com.kunyang.android.nowmission.util.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by 坤阳 on 2017/9/11.
 */

public class WeatherActivity extends AppCompatActivity implements View.OnClickListener,ViewPager.OnPageChangeListener{

    private ImageView[] dots;
    private int[] ids = {R.id.iv1, R.id.iv2};

    private ViewPagerAdapter vpAdapter;
    private ViewPager vp;
    private List<View> views;

    private DrawerLayout drawerLayout;


    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView humText;
    private TextView pmdeg;
    private ImageView pm25Image;
    private TextView pm25quality;
    private ImageView weatherImage;
    private TextView todayWeather;
    private TextView temperature;
    private TextView climate;
    private TextView wind;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.citylayout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        if (mToolbar != null) {        setSupportActionBar(mToolbar);    }


        titleCity=(TextView) this.findViewById(R.id.city);
        titleUpdateTime=(TextView) this.findViewById(R.id.time);
        humText=(TextView)this.findViewById(R.id.humidity);
        pmdeg=(TextView)this.findViewById(R.id.pm_data);
        pm25Image=(ImageView)this.findViewById(R.id.pm2_5_img);
        pm25quality=(TextView)this.findViewById(R.id.pm2_5_quality);
        weatherImage=(ImageView)this.findViewById(R.id.weather_img);
        todayWeather=(TextView)this.findViewById(R.id.week_today);
        temperature=(TextView)this.findViewById(R.id.temperature);
        climate=(TextView)this.findViewById(R.id.climate);
        wind=(TextView)this.findViewById(R.id.wind);

        LayoutInflater inflater = LayoutInflater.from(this);
        View one_page = inflater.inflate(R.layout.page1, null);
        View two_page = inflater.inflate(R.layout.page2, null);
        views = new ArrayList<View>();
        views.add(one_page);
        views.add(two_page);
        vpAdapter = new ViewPagerAdapter(views, this);
        vp = (ViewPager)findViewById(R.id.viewpager);
        vp.setAdapter(vpAdapter);

        vp.setOnPageChangeListener(this);
        initDots();

        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString=prefs.getString("weather",null);
        if (weatherString!=null){
            Weather weather= Utility.handleWeatherResponse(weatherString);
            showWeatherInfo(weather);
        }
        if (getIntent().getStringExtra("weather_city")!=null){
            String weatherCity=getIntent().getStringExtra("weather_city");
            requestWeather(weatherCity);
        }

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer2);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, mToolbar, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerOpened(View v) {        super.onDrawerOpened(v); }
            @Override
            public void onDrawerClosed(View v) {        super.onDrawerClosed(v); }
        };
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_context, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_share:
                finish();
                return true;
            case R.id.menu_refresh:
                String weatherCity=getIntent().getStringExtra("weather_city");
                requestWeather(weatherCity);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void requestWeather(final String weatherCity) {
        String weatherUrl="https://free-api.heweather.com/v5/weather?city=" + weatherCity + "&key=a960da1bb9f64f289b4432445e0c1974";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"获取天气信息失败",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText=response.body().string();
                final Weather weather=Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather!=null&&"ok".equals(weather.status)){
                            SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather",responseText);
                            editor.apply();
                            showWeatherInfo(weather);
                        }else {
                            Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void showWeatherInfo(Weather weather) {
        String city1 =weather.basic.city;
        String titleUpdateTime1=weather.basic.update.updateTime;
        String humText1="湿度:"+ weather.now.hum +"%";

        String todayWeather1="天气:"+weather.now.cond.txt;
        String temperature1=weather.now.tmp+"℃";
        String climate1="舒适度:"+weather.suggestion.comf.brf;
        String wind1="风强:"+weather.now.wind.deg;

        if (weather!=null&&"ok".equals(weather.status)){
            Intent intent=new Intent(this, AutoUpdateService.class);
            startService(intent);
        }else {
            Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
        }

        if (weather.aqi!=null){
            String pmdeg1=weather.aqi.city.aqi;
            String pm25quality1="空气质量:"+weather.aqi.city.qlty;
            pmdeg.setText(pmdeg1);
            pm25quality.setText(pm25quality1);
        }

        titleCity.setText(city1);
        titleUpdateTime.setText(titleUpdateTime1);
        humText.setText(humText1);
        todayWeather.setText(todayWeather1);
        temperature.setText(temperature1);
        climate.setText(climate1);
        wind.setText(wind1);
        pm25Image.setImageResource(R.drawable.pm);
        weatherImage.setImageResource(R.drawable.pm);
    }

    private void initDots() {
        dots = new ImageView[views.size()];
        for (int i=0; i<views.size(); i++) {
            dots[i] = (ImageView)findViewById(ids[i]);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int a=0; a<ids.length; a++) {
            if (a==position) {
                dots[a].setImageResource(R.drawable.page_indicator_focused);
            } else {
                dots[a].setImageResource(R.drawable.page_indicator_unfocused);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View view) {

    }
}
