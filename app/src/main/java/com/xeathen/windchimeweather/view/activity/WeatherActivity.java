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

//    @BindView(R.id.spn_cities_list)
//    Spinner citiesListSpn;

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

//    @BindView(R.id.menu_button)
//    Button menuButton;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.forecast_layout)
    LinearLayout forecastLayout;

    private List<String> cityNameList = new ArrayList<>();

    private ArrayAdapter citiesListAdapter;

    private List<CityDB> cityDBList = new ArrayList<>();

    private String currentCityId;

    private CityDB currentCityDB;

    private String weatherBasicJson;

    private String weatherAirJson;


    private long exitTime = 0;

    private boolean success = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);

        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        initView();
        initDrawer();
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//        currentCityId = prefs.getString("current_city_id", null);
        currentCityId = SharedPreferencesUtil.getString("current_city_id", "CN101230101");
        if (currentCityId != null) { //有默认城市
            currentCityDB = LitePal.where("cityId = ?", currentCityId).find(CityDB.class).get(0);

//            weatherBasicJson = prefs.getString("json_weather_basic_" + currentCityId, null);
            weatherBasicJson = SharedPreferencesUtil.getString("json_weather_basic_"+ currentCityId, null);
//            weatherAirJson = prefs.getString("json_weather_air_" + currentCityId, null);
            weatherAirJson = SharedPreferencesUtil.getString("json_weather_air_" + currentCityId, null);
            if (weatherBasicJson != null && weatherAirJson != null) { //默认城市有缓存的情况下，直接读取缓存数据
                LogUtil.d(TAG, "默认城市有缓存");
                WeatherBasic weatherBasic = new Gson().fromJson(weatherBasicJson, WeatherBasic.class);
                WeatherAir weatherAir = new Gson().fromJson(weatherAirJson, WeatherAir.class);
                showWeatherBasicInfo(weatherBasic);
                showWeatherAirInfo(weatherAir);


            } else { //无缓存的情况下向网络请求数据
                LogUtil.d(TAG, "默认城市缺少缓存");

                requestWeather(currentCityId);

            }
//            cityDBList = LitePal.findAll(CityDB.class);
//
//            cityNameList = new ArrayList<>();
//            for (CityDB cityDB : cityDBList) {
////                City city = new City(cityDB.getName(), cityDB.getCityId(), cityDB.getAdminArea(), cityDB.getCountry());
////                cityList.add(city);
//                cityNameList.add(cityDB.getName());
//            }
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
//                    LogUtil.i(TAG, "onWeatherBasic:" + weatherBasic.lifeStyles.get(0).txt);

//                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
//                    editor.putString("json_weather_basic_" + currentCityId, content);
//                    editor.apply();
                    SharedPreferencesUtil.saveString("json_weather_basic_" + currentCityId, content);
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
                            LogUtil.i(TAG, new Gson().toJson(list.get(0)));
                            SharedPreferencesUtil.saveString("json_weather_air_" + currentCityId, content);

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
                    LogUtil.i(TAG, new Gson().toJson(list.get(0)));
                    SharedPreferencesUtil.saveString("json_weather_air_" + currentCityId, content);

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

//    public void initCitiesListAdapter() {
//
//
//        try {
//            citiesListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cityNameList);
//            citiesListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            citiesListSpn.setAdapter(citiesListAdapter);
//            for (int i = 0; i < cityDBList.size(); i++) {
//                if (currentCityId.equals(cityDBList.get(i).getCityId())) {
//                    citiesListSpn.setSelection(i, true);
//                    break;
//                }
//            }
//
//            citiesListSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
//                    HeWeather.getWeatherForecast(WeatherActivity.this, /*citiesListAdapter.getItem(position).toString()*/cityDBList.get(position).getCityId(), Lang.CHINESE_SIMPLIFIED, Unit.METRIC, new HeWeather.OnResultWeatherForecastBeanListener() {
//                        @Override
//                        public void onError(Throwable throwable) {
//
//                        }
//
//                        @Override
//                        public void onSuccess(List<Forecast> list) {
//
//                            WeatherForecast weatherForecast = new Gson().fromJson(new Gson().toJson(list.get(0)), WeatherForecast.class);
//                            LogUtil.i(TAG, "onSuccess1:" + new Gson().toJson(weatherForecast));
//
//                            //显示天气信息
//                            showWeatherInfo(weatherForecast);
//
//
//                        }
//                    });
//
//                    //改变current
//                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext()).edit();
//                    editor.putString("current_city_id", cityDBList.get(position).getCityId());
//                    currentCityId = cityDBList.get(position).getCityId();
//                    editor.apply();
//
//                    LogUtil.i(TAG, "onSelected" + currentCityId);
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {
//
//                }
//            });
//
//        } catch (Exception e) {
//            LogUtil.e(TAG, e.getMessage());
//        }
//    }


    @OnClick(R.id.fab)
    public void toCard() {
        Intent intent = new Intent(this, CityActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        currentCityId = prefs.getString("current_city_id", null);
        if (currentCityId != null) {
            cityDBList = LitePal.findAll(CityDB.class);
//            cityList = new ArrayList<>();
            cityNameList = new ArrayList<>();
            for (CityDB cityDB : cityDBList) {
//                City city = new City(cityDB.getName(), cityDB.getCityId(), cityDB.getAdminArea(), cityDB.getCountry());
//                cityList.add(city);
                cityNameList.add(cityDB.getName());
                //spinner设置默认选择
                //记得改变spinner大小
//                LogUtil.i(TAG, cityDB.getName() + ", "+ cityDB.getCityId() + ","+cityDBS.size());
            }
            LogUtil.i(TAG, "onResume");
            LogUtil.i(TAG, "current:" + currentCityId);
//            initCitiesListAdapter();

        } else {

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
//                toastShort(WeatherActivity.this, "test");
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
                            toastShort(WeatherActivity.this, "setting");
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
