package com.xeathen.windchimeweather.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xeathen.lib.utils.DateUtil;
import com.xeathen.lib.utils.LogUtil;
import com.xeathen.windchimeweather.R;
import com.xeathen.windchimeweather.bean.db.CityDB;
import com.xeathen.windchimeweather.bean.gson.DailyForecast;
import com.xeathen.windchimeweather.bean.gson.WeatherAir;
import com.xeathen.windchimeweather.bean.gson.WeatherBasic;
import com.xeathen.windchimeweather.common.MyApplication;
import com.xeathen.windchimeweather.controller.ActivityCollector;
import com.xeathen.windchimeweather.util.SharedPreferencesUtil;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import interfaces.heweather.com.interfacesmodule.bean.Lang;
import interfaces.heweather.com.interfacesmodule.bean.Unit;
import interfaces.heweather.com.interfacesmodule.bean.air.Air;
import interfaces.heweather.com.interfacesmodule.bean.weather.Weather;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;

public class WeatherActivity extends BaseActivity {


    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;

    @BindView(R.id.weather_tmp)
    TextView weatherTmp;

    @BindView(R.id.weather_cond_text)
    TextView weatherCondText;

    @BindView(R.id.update_time)
    TextView weatherCityName;

    @BindView(R.id.qlty)
    TextView qlty;


    @BindView(R.id.nav_view)
    NavigationView navView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;


    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.forecast_layout)
    LinearLayout forecastLayout;


    private String currentCityId;

    private CityDB currentCityDB;

    private String weatherBasicJson;

    private String weatherAirJson;

    private SharedPreferencesUtil mSpfu;


    private long exitTime = 0;

    private boolean success;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);

        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        mSpfu = SharedPreferencesUtil.getInstance();
        initView();
        initDrawer();

        currentCityId = mSpfu.getString("current_city_id", "CN101230101");
        if (currentCityId != null) { //有默认城市
            currentCityDB = LitePal.where("cityId = ?", currentCityId).find(CityDB.class).get(0);
            weatherBasicJson = mSpfu.getString("json_weather_basic_"+ currentCityId, null);
            weatherAirJson = mSpfu.getString("json_weather_air_" + currentCityId, null);
            if (weatherBasicJson != null && weatherAirJson != null
                    && !weatherBasicJson.equals("") && !weatherAirJson.equals("") ) { //默认城市有缓存的情况下，直接读取缓存数据
                LogUtil.d(TAG, "默认城市有缓存");
                WeatherBasic weatherBasic = new Gson().fromJson(weatherBasicJson, WeatherBasic.class);
                WeatherAir weatherAir = new Gson().fromJson(weatherAirJson, WeatherAir.class);
                showWeatherBasicInfo(weatherBasic);
                showWeatherAirInfo(weatherAir);


            } else { //无缓存的情况下向网络请求数据
                LogUtil.d(TAG, "默认城市缺少缓存");

                requestWeather(currentCityId);

            }

            LogUtil.i(TAG, "onCreate");
            LogUtil.i(TAG, "current:" + currentCityId);

        } else {
            //currentId为null,**********test****
            toastLong(this, "无选择城市，默认选择福州");
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
        }

    }


    private void requestWeather(final String cityId) {

        success = true; //初始化布尔值success

        HeWeather.getWeather(this, cityId, Lang.CHINESE_SIMPLIFIED, Unit.METRIC, new HeWeather.OnResultWeatherDataListBeansListener() {
            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
                success = false;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onSuccess(List<Weather> list) {
                String content = new Gson().toJson(list.get(0));
                if (content != null && !content.equals("")) {
                    final WeatherBasic weatherBasic = new Gson().fromJson(new Gson().toJson(list.get(0)), WeatherBasic.class);

                    mSpfu.saveString("json_weather_basic_" + currentCityId, content);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showWeatherBasicInfo(weatherBasic);
//                            swipeRefresh.setRefreshing(false);

                        }
                    });
                }


            }
        });

        HeWeather.getAir(this, cityId, Lang.CHINESE_SIMPLIFIED, Unit.METRIC, new HeWeather.OnResultAirBeanListener() {

            @Override
            public void onError(Throwable throwable) {

                HeWeather.getAir(WeatherActivity.this, currentCityDB.getParentCity(), Lang.CHINESE_SIMPLIFIED, Unit.METRIC, new HeWeather.OnResultAirBeanListener() {
                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                        success = false;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(WeatherActivity.this, "获取空气信息失败", Toast.LENGTH_SHORT).show();
                                swipeRefresh.setRefreshing(false);
                            }
                        });
                    }

                    @Override
                    public void onSuccess(List<Air> list) {

                        String content = new Gson().toJson(list.get(0));
                        if (content != null && !content.equals("")) {
                            final WeatherAir weatherAir = new Gson().fromJson(new Gson().toJson(list.get(0)), WeatherAir.class);
//                            LogUtil.i(TAG, new Gson().toJson(list.get(0)));
                            mSpfu.saveString("json_weather_air_" + currentCityId, content);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showWeatherAirInfo(weatherAir);
                                }
                            });
                        }

                    }
                });


            }

            @Override
            public void onSuccess(List<Air> list) {
                String content = new Gson().toJson(list.get(0));
                if (content != null && !content.equals("")) {
                    final WeatherAir weatherAir = new Gson().fromJson(new Gson().toJson(list.get(0)), WeatherAir.class);
//                    LogUtil.i(TAG, new Gson().toJson(list.get(0)));
                    mSpfu.saveString("json_weather_air_" + currentCityId, content);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showWeatherAirInfo(weatherAir);
                        }
                    });
                }

            }
        });


        if (success) {
            swipeRefresh.setRefreshing(false);

        }


    }

    private void showWeatherBasicInfo(WeatherBasic weatherBasic) {
        collapsingToolbarLayout.setTitle(weatherBasic.basic.cityName);
        weatherTmp.setText(weatherBasic.now.tmp + "°");
        weatherCondText.setText(weatherBasic.forecasts.get(0).cond_txt_d);
        weatherCityName.setText(weatherBasic.update.time.split(" ")[1]);
        // 3-7日预报
        forecastLayout.removeAllViews();
        for (DailyForecast dailyForecast : weatherBasic.forecasts) {
            View view = LayoutInflater.from(this).inflate(R.layout.weather_forecast_item, forecastLayout, false);
            TextView dataText = view.findViewById(R.id.date_text);
            TextView condTextD = view.findViewById(R.id.cond_txt_d);

            TextView tmp = view.findViewById(R.id.tmp);
            dataText.setText(DateUtil.getDayofWeekInCh(DateUtil.getDayofWeekInNumber(dailyForecast.date)));
            condTextD.setText(dailyForecast.cond_txt_d);
            tmp.setText(dailyForecast.tmp_min + "°~" + dailyForecast.tmp_max + "°");
            forecastLayout.addView(view);

        }
    }

    private void showWeatherAirInfo(WeatherAir weatherAir) {
        qlty.setText(weatherAir.airCity.aqi + " " + weatherAir.airCity.qlty);
    }



    @OnClick(R.id.fab)
    public void toCard() {
        Intent intent = new Intent(this, CityActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (drawerLayout.isDrawerOpen(Gravity.START)) {
            drawerLayout.closeDrawer(Gravity.START);

        }
    }

    /**
     * 初始化基础View
     */
    private void initView() {

        setSupportActionBar(toolbar);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary/*,R.color.coral, R.color.bisque*/);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(currentCityId);
            }
        });
    }

    /**
     * 初始化抽屉
     */
    private void initDrawer() {
        if (navView != null) {
            navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()) {
                        case R.id.nav_cities:
                            Intent intent = new Intent(WeatherActivity.this, CityActivity.class);
                            startActivity(intent);
                            return true;

                        case R.id.nav_setting:
                            Intent intent1 = new Intent(WeatherActivity.this, SettingsActivity.class);
                            startActivity(intent1);
                            return true;
                    }
                    return true;
                }
            });
            ActionBarDrawerToggle toggle =
                    new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open,
                            R.string.navigation_drawer_close);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (drawerLayout.isDrawerOpen(Gravity.START)) {
            drawerLayout.closeDrawer(Gravity.START);
            return true;
        } else {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                if ((System.currentTimeMillis() - exitTime) > 2000) {
                    toastShort(this, "再按一次退出程序");
                    exitTime = System.currentTimeMillis();
                } else {

                    ActivityCollector.finishAll();
                    System.exit(0);
                }
                return true;
            }
            return super.onKeyDown(keyCode, event);
        }


    }

    @Override
    public int initLayout() {
        return R.layout.activity_weather;
    }
}
