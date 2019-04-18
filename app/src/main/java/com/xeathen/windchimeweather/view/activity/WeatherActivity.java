package com.xeathen.windchimeweather.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xeathen.lib.utils.DateUtil;
import com.xeathen.lib.utils.LogUtil;
import com.xeathen.windchimeweather.R;
import com.xeathen.windchimeweather.bean.db.CityDB;
import com.xeathen.windchimeweather.bean.gson.DailyForecast;
import com.xeathen.windchimeweather.bean.gson.DailyLifeStyle;
import com.xeathen.windchimeweather.bean.gson.HourlyGson;
import com.xeathen.windchimeweather.bean.gson.WeatherAir;
import com.xeathen.windchimeweather.bean.gson.WeatherBasic;
import com.xeathen.windchimeweather.controller.ActivityCollector;
import com.xeathen.windchimeweather.util.SharedPreferencesUtil;
import com.xeathen.windchimeweather.util.WeatherDrawableUtil;

import org.litepal.LitePal;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import interfaces.heweather.com.interfacesmodule.bean.Lang;
import interfaces.heweather.com.interfacesmodule.bean.Unit;
import interfaces.heweather.com.interfacesmodule.bean.air.Air;
import interfaces.heweather.com.interfacesmodule.bean.weather.Weather;
import interfaces.heweather.com.interfacesmodule.bean.weather.hourly.HourlyBase;
import interfaces.heweather.com.interfacesmodule.bean.weather.lifestyle.Lifestyle;
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

    @BindView(R.id.lifestyle_uv)
    TextView uv;

    @BindView(R.id.lifestyle_sport)
    TextView sport;

    @BindView(R.id.lifestyle_hum)
    TextView hum;

    @BindView(R.id.lifestyle_cw)
    TextView cw;


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

    @BindView(R.id.hor_view)
    HorizontalScrollView hourlyLayout;

    @BindView(R.id.hor_cont)
    LinearLayout horContent;

    @BindView(R.id.ct_img)
    ImageView ctImg;


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
            weatherBasicJson = mSpfu.getString("json_weather_basic_" + currentCityId, null);
            weatherAirJson = mSpfu.getString("json_weather_air_" + currentCityId, null);
            if (weatherBasicJson != null && weatherAirJson != null
                    && !weatherBasicJson.equals("") && !weatherAirJson.equals("")) { //默认城市有缓存的情况下，直接读取缓存数据
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
            //currentId为null,*****test****
//            toastLong(this, "无选择城市，默认选择福州");
//            Intent intent = new Intent(this, SearchActivity.class);
//            startActivity(intent);
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
            toastShort(this, "刷新成功");
        }


    }

    private void showWeatherBasicInfo(WeatherBasic weatherBasic) {
//        LogUtil.i("hours", weatherBasic.);
        collapsingToolbarLayout.setTitle(weatherBasic.basic.cityName);
        weatherTmp.setText(weatherBasic.now.tmp + "°");
        hum.setText(weatherBasic.now.hum);
        weatherCondText.setText(weatherBasic.now.cond_txt);
        if (Build.VERSION.SDK_INT >= 21) {
            Drawable drawable = WeatherDrawableUtil.getDrawableByTxt(weatherBasic.now.cond_txt);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            weatherCondText.setCompoundDrawables(drawable, null, null, null);
        }
        weatherCityName.setText(weatherBasic.update.time.split(" ")[1]);
        // 3-7日预报
        forecastLayout.removeAllViews();
        int cnt = 0;
        for (DailyForecast dailyForecast : weatherBasic.forecastList) {
            View view = LayoutInflater.from(this).inflate(R.layout.weather_forecast_item, forecastLayout, false);
            TextView dataText = view.findViewById(R.id.date_text);
            TextView condTextD = view.findViewById(R.id.cond_txt_d);
            TextView tmp = view.findViewById(R.id.tmp);
            dataText.setText(cnt++ == 0 ? "今天" : DateUtil.getDayofWeekInCh(DateUtil.getDayofWeekInNumber(dailyForecast.date)) + "/" + dailyForecast.date.substring(5));
            condTextD.setText(dailyForecast.cond_txt_d);
            tmp.setText(dailyForecast.tmp_max + "°~" + dailyForecast.tmp_min + "°");
            if (Build.VERSION.SDK_INT >= 21) {
                Drawable drawable = WeatherDrawableUtil.getDrawableByTxt(dailyForecast.cond_txt_d);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                condTextD.setCompoundDrawables(drawable, null, null, null);
            }
            forecastLayout.addView(view);
        }
        //隔3小时预报
        horContent.removeAllViews();
        for (HourlyGson hourlyGson : weatherBasic.hourlyGsonList) {
            View view = LayoutInflater.from(this).inflate(R.layout.weather_hourly_item, hourlyLayout, false);
            TextView hourlyTime = view.findViewById(R.id.hourly_time);
            ImageView condImage = view.findViewById(R.id.hourly_cond_txt);
            TextView hourlyTmp = view.findViewById(R.id.hourly_tmp);
            int time = Integer.parseInt(cutHour(hourlyGson.time.substring(11)));
            hourlyTime.setText(time + "时");
            if ((time >= 0 && time <= 5) || (time >= 20 && time <= 24)) {
                condImage.setImageDrawable(WeatherDrawableUtil.getMoonDrawable());

            } else {
                condImage.setImageDrawable(WeatherDrawableUtil.getDrawableByTxt(hourlyGson.cond_txt));

            }
            hourlyTmp.setText(hourlyGson.tmp);
            horContent.addView(view);
        }

        //lifestyle
        for (DailyLifeStyle lifeStyle : weatherBasic.lifeStyleList) {
            switch (lifeStyle.type) {
                case "uv":
                    uv.setText(lifeStyle.brf);
                    break;
                case "sport":
                    sport.setText(lifeStyle.brf);
                    break;
                case "cw":
                    cw.setText(lifeStyle.brf);
            }
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
                        case R.id.nav_about:
                            Intent intent2 = new Intent(WeatherActivity.this, AboutActivity.class);
                            startActivity(intent2);
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


    @Override
    protected void onResume() {
        super.onResume();
//        if (drawerLayout.isDrawerOpen(Gravity.START)) {
//            drawerLayout.closeDrawer(Gravity.START);
//
//        }
    }

    private String cutHour(String oriHour) {
        return (oriHour.charAt(0) == '0' ? oriHour.substring(1, 2) : oriHour.substring(0, 2));
    }


}
