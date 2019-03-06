package com.xeathen.windchimeweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xeathen.lib.utils.LogUtil;
import com.xeathen.windchimeweather.bean.db.CityDB;
import com.xeathen.windchimeweather.bean.gson.WeatherAir;
import com.xeathen.windchimeweather.common.MyApplication;
import com.xeathen.windchimeweather.util.SharedPreferencesUtil;
import com.xeathen.windchimeweather.view.activity.WeatherActivity;

import org.litepal.LitePal;

import java.util.List;

import interfaces.heweather.com.interfacesmodule.bean.Lang;
import interfaces.heweather.com.interfacesmodule.bean.Unit;
import interfaces.heweather.com.interfacesmodule.bean.air.Air;
import interfaces.heweather.com.interfacesmodule.bean.weather.Weather;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;

/**
 * @author: 蛋清蛋黄
 * @date: 2019/2/14
 * @description:
 */
public class AutoUpdateService extends Service {


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateWeather();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long triggerAtTime = SystemClock.elapsedRealtime() + SharedPreferencesUtil.getInstance().getAutoUpdateTime();
        Intent i = new Intent(this, AutoUpdateService.class);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return START_REDELIVER_INTENT;
    }

    /**
     * 更新天气信息
     */
    private void updateWeather(){
        List<CityDB> cityDBList = LitePal.findAll(CityDB.class);
        for (final CityDB cityDB : cityDBList){
            final String cityId = cityDB.getCityId();
            HeWeather.getWeather(this, cityId, Lang.CHINESE_SIMPLIFIED, Unit.METRIC, new HeWeather.OnResultWeatherDataListBeansListener() {
                @Override
                public void onError(Throwable throwable) {
                    LogUtil.e("AutoUpdateService", "更新失败");
                }

                @Override
                public void onSuccess(List<Weather> list) {
                    String content = new Gson().toJson(list.get(0));
                    if (content != null && !content.equals("")) {
                        SharedPreferencesUtil.getInstance().saveString("json_weather_basic_" + cityId, content);
                    }
                }
            });
            HeWeather.getAir(this, cityId, Lang.CHINESE_SIMPLIFIED, Unit.METRIC, new HeWeather.OnResultAirBeanListener() {

                @Override
                public void onError(Throwable throwable) {

                    HeWeather.getAir(MyApplication.getContext(), cityDB.getParentCity(), Lang.CHINESE_SIMPLIFIED, Unit.METRIC, new HeWeather.OnResultAirBeanListener() {
                        @Override
                        public void onError(Throwable throwable) {
                            throwable.printStackTrace();

                        }

                        @Override
                        public void onSuccess(List<Air> list) {

                            String content = new Gson().toJson(list.get(0));
                            if (content != null && !content.equals("")) {
                                SharedPreferencesUtil.getInstance().saveString("json_weather_air_" + cityId, content);


                            }

                        }
                    });


                }

                @Override
                public void onSuccess(List<Air> list) {
                    String content = new Gson().toJson(list.get(0));
                    if (content != null && !content.equals("")) {

                        SharedPreferencesUtil.getInstance().saveString("json_weather_air_" + cityId, content);

                    }

                }
            });
        }
//        String currentCityId = SharedPreferencesUtil.getString("currentCityId", "CN101230101");
//        HeWeather.getWeather(this, currentCityId, Lang.CHINESE_SIMPLIFIED, Unit.METRIC, new HeWeather.OnResultWeatherDataListBeansListener() {
//            @Override
//            public void onError(Throwable throwable) {
//                Log.e("AutoUpdateService", "更新失败");
//            }
//
//            @Override
//            public void onSuccess(List<Weather> list) {
//
//            }
//        });
    }
}
