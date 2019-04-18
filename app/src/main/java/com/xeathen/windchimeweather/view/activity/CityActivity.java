package com.xeathen.windchimeweather.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.xeathen.windchimeweather.R;
import com.xeathen.windchimeweather.adapter.CityCardAdapter;
import com.xeathen.windchimeweather.bean.db.CityDB;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class CityActivity extends BaseActivity {

    @BindView(R.id.feb)
    FloatingActionButton feb;


    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recycler_city_card)
    RecyclerView recyclerView;


    private List<CityDB> cityList = new ArrayList<>();

    private CityCardAdapter cityCardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (Build.VERSION.SDK_INT >= 21) {
//            View decorView = getWindow().getDecorView();
//            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }
        toolbar.setTitle("城市列表");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//左侧添加一个默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //卡片式布局
//        recyclerView = findViewById(R.id.recycler_city_card);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);


    }


    @OnClick(R.id.feb)
    public void toSearch() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Log.i(TAG, "onResume");
        cityList = LitePal.findAll(CityDB.class);
        cityCardAdapter = new CityCardAdapter(this, cityList);
        recyclerView.setAdapter(cityCardAdapter);

    }

//    public void getCityList() {
//        cityList = LitePal.findAll(CityDB.class);
//        cityCardAdapter.notifyDataSetChanged();
//
//    }

    @Override
    public int initLayout() {
        return R.layout.activity_city;
    }


}
