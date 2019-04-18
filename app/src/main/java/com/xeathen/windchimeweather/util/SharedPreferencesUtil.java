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

    private static SharedPreferencesUtil spfu;

    public static final String AUTO_UPDATE_TIME = "auto_update_time";//自动更新频率

    public static final String CLEAR_CACHE = "clear_cache"; //清空缓存

    private SharedPreferences mPrefs;


    public  void saveString(String key, String value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext()).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public  String getString(String key, String defValue) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
        return prefs.getString(key, defValue);
    }

    public void setAutoUpdateTime(int time) {
        mPrefs.edit().putInt(AUTO_UPDATE_TIME, time).apply();
    }

    public int getAutoUpdateTime() {
        return mPrefs.getInt(AUTO_UPDATE_TIME, 12);
    }



    private SharedPreferencesUtil() {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
    }

    public static SharedPreferencesUtil getInstance() {
        if (spfu == null ){
            synchronized (SharedPreferencesUtil.class){
                if (spfu == null){
                    spfu = new SharedPreferencesUtil();
                }
            }
        }
        return spfu;
    }

}
