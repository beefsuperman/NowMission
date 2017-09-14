package com.kunyang.android.nowmission;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 坤阳 on 2017/9/14.
 */

public class guide extends AppCompatActivity implements View.OnClickListener,ViewPager.OnPageChangeListener {

    private ImageView[] dots;
    private int[] ids = {R.id.iv1, R.id.iv2,R.id.iv3};

    private ViewPagerAdapter vpAdapter;
    private ViewPager vp;
    private List<View> views;

    private Button guide_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences first = (SharedPreferences)getSharedPreferences("first",MODE_PRIVATE);
        Boolean isFirst = first.getBoolean("isFirst", true);
        if(isFirst){
            setContentView(R.layout.guide);
            initViews();
            initDots();
            guide_btn = (Button)views.get(2).findViewById(R.id.guide_btn);

            guide_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(guide.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            SharedPreferences.Editor editor = first.edit();
            editor.putBoolean("isFirst",false);
            editor.commit();
        }
        else{
            Intent intent = new Intent(guide.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        setContentView(R.layout.guide);

        initViews();
        initDots();

        guide_btn = (Button)views.get(2).findViewById(R.id.guide_btn);
        guide_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(guide.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    void initDots(){
        dots = new ImageView[views.size()];
        for(int i=0;i<views.size();i++){
            dots[i]=(ImageView)findViewById(ids[i]);
        }
    }

    private void initViews(){
        LayoutInflater inflater = LayoutInflater.from(this);
        views =new ArrayList<View>();
        views.add(inflater.inflate(R.layout.guidepage1,null));
        views.add(inflater.inflate(R.layout.guidepage2,null));
        views.add(inflater.inflate(R.layout.guidepage3,null));
        vpAdapter = new ViewPagerAdapter(views,this);
        vp = (ViewPager)findViewById(R.id.viewpager_guide);
        vp.setAdapter(vpAdapter);
        vp.setOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for(int a=0;a<ids.length;a++){
            if(a ==position){
                dots[a].setImageResource(R.drawable.page_indicator_focused);
            }else{
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
