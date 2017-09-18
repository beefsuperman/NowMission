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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kunyang.android.nowmission.Service.AutoUpdateService;
import com.kunyang.android.nowmission.gson.Forecast;
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
    private TextView[] FurDate = new TextView[6];
    private TextView[] FurTemp = new TextView[6];
    private TextView[] FurWea = new TextView[6];
    private TextView[] FurWinds = new TextView[6];
    private ImageView[] FurImages = new ImageView[6];


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

        FurDate[0] = (TextView)one_page.findViewById(R.id.weekDay1);
        FurTemp[0] = (TextView)one_page.findViewById(R.id.temperatureDay1);
        FurWea[0] = (TextView)one_page.findViewById(R.id.climateDay1);
        FurWinds[0] = (TextView)one_page.findViewById(R.id.windDay1);
        FurImages[0] = (ImageView)one_page.findViewById(R.id.imageDay1);
        FurDate[1] = (TextView)one_page.findViewById(R.id.weekDay2);
        FurTemp[1] = (TextView)one_page.findViewById(R.id.temperatureDay2);
        FurWea[1] = (TextView)one_page.findViewById(R.id.climateDay2);
        FurWinds[1] = (TextView)one_page.findViewById(R.id.windDay2);
        FurImages[1] = (ImageView)one_page.findViewById(R.id.imageDay2);
        FurDate[2] = (TextView)one_page.findViewById(R.id.weekDay3);
        FurTemp[2] = (TextView)one_page.findViewById(R.id.temperatureDay3);
        FurWea[2] = (TextView)one_page.findViewById(R.id.climateDay3);
        FurWinds[2] = (TextView)one_page.findViewById(R.id.windDay3);
        FurImages[2] = (ImageView)one_page.findViewById(R.id.imageDay3);
        FurDate[3] = (TextView)two_page.findViewById(R.id.weekDay4);
        FurTemp[3] = (TextView)two_page.findViewById(R.id.temperatureDay4);
        FurWea[3] = (TextView)two_page.findViewById(R.id.climateDay4);
        FurWinds[3] = (TextView)two_page.findViewById(R.id.windDay4);
        FurImages[3] = (ImageView)two_page.findViewById(R.id.imageDay4);
        FurDate[4] = (TextView)two_page.findViewById(R.id.weekDay5);
        FurTemp[4] = (TextView)two_page.findViewById(R.id.temperatureDay5);
        FurWea[4] = (TextView)two_page.findViewById(R.id.climateDay5);
        FurWinds[4] = (TextView)two_page.findViewById(R.id.windDay5);
        FurImages[4] = (ImageView)two_page.findViewById(R.id.imageDay5);
        FurDate[5] = (TextView)two_page.findViewById(R.id.weekDay6);
        FurTemp[5] = (TextView)two_page.findViewById(R.id.temperatureDay6);
        FurWea[5] = (TextView)two_page.findViewById(R.id.climateDay6);
        FurWinds[5] = (TextView)two_page.findViewById(R.id.windDay6);
        FurImages[5] = (ImageView)two_page.findViewById(R.id.imageDay6);


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
        mToolbar.setTitle(getIntent().getStringExtra("weather_city")+"天气");

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
        String humText1="相对湿度:"+ weather.now.hum +"%";

        String todayWeather1="天气:"+weather.now.cond.txt;
        String temperature1="温度:"+weather.now.tmp+"℃";
        String climate1="舒适度:"+weather.suggestion.comf.brf;
        String wind1="风向(360°):"+weather.now.wind.deg;

        //未来天气预报。
        updateFutureWeather(weather.forecastList);


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

    void updateFutureWeather( List<Forecast> forecastList){
        for (int i=0; i<forecastList.size(); i++) {
            Forecast forecast = forecastList.get(i);
            FurDate[i].setText(forecast.date);
            FurTemp[i].setText(forecast.tmp.min +"℃"+ "~" + forecast.tmp.max+ "℃");
            FurWea[i].setText("天气:"+forecast.cond.txt_d);
            FurWinds[i].setText("风速:"+forecast.wind.spd+"kmph");


        }
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
