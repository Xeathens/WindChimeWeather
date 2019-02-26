package com.xeathen.windchimeweather.util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.xeathen.windchimeweather.common.MyApplication;
import com.xeathen.windchimeweather.view.activity.WeatherActivity;

/**
 * @author: 蛋清蛋黄
 * @date: 2019/2/26
 * @description:
 */
public class SharedPreferencesUtil {

    public static void saveString(String key, String value){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext()).edit();
        editor.putString(key , value);
        editor.apply();
    }

    public static String getString(String key, String defValue){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
        return prefs.getString(key, null);
    }
}
