package com.xeathen.windchimeweather.view.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xeathen.lib.utils.LogUtil;
import com.xeathen.windchimeweather.R;
import com.xeathen.windchimeweather.adapter.CityListAdapter;
import com.xeathen.windchimeweather.bean.model.City;
import com.xeathen.windchimeweather.controller.ActivityCollector;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import interfaces.heweather.com.interfacesmodule.bean.Lang;
import interfaces.heweather.com.interfacesmodule.bean.basic.Basic;
import interfaces.heweather.com.interfacesmodule.bean.search.Search;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;

public class SearchActivity extends BaseActivity {

    @BindView(R.id.bt_search_city)
    Button btSearchCity;

    @BindView(R.id.edit_search_city)
    EditText etSearchCity;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private List<City> cityList = new ArrayList<>();

    RecyclerView recyclerView;

    private LinearLayoutManager layoutManager;

    private CityListAdapter cityListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);

//        if (Build.VERSION.SDK_INT >= 21) {
//            View decorView = getWindow().getDecorView();
//            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }
        toolbar.setTitle("添加城市");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//左侧添加一个默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView = findViewById(R.id.recycler_city_list);
        layoutManager = new LinearLayoutManager(SearchActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        cityListAdapter = new CityListAdapter(cityList);
        recyclerView.setAdapter(cityListAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        etSearchCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchCity();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick(R.id.bt_search_city)
    public void searchCity() {

        HeWeather.getSearch(this, etSearchCity.getText().toString(), "world", 20, Lang.CHINESE_SIMPLIFIED, new HeWeather.OnResultSearchBeansListener() {
            @Override
            public void onError(Throwable throwable) {
                toastShort(getApplicationContext(), "查询失败");
                LogUtil.i(TAG, "查询失败");
            }

            @Override
            public void onSuccess(Search search) {
                toastShort(getApplicationContext(), "查询成功");
                LogUtil.i(TAG, "查询成功");
                cityList.clear();
                for (Basic basic : search.getBasic()) {
//                    LogUtil.i(TAG, basic.getLocation());
//                    LogUtil.i(TAG, basic.getCid());

                    City city = new City(basic.getLocation(), basic.getCid(), basic.getParent_city(), basic.getAdmin_area(), basic.getCnty());
                    cityList.add(city);
                    /*
                    填入数据库
                     */
                }
                cityListAdapter.notifyDataSetChanged();






            }
        });
    }



    @Override
    public int initLayout() {
        return R.layout.activity_search;
    }
}
