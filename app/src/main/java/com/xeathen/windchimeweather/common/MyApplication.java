package com.xeathen.windchimeweather.common;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;

/**
 * @author: 蛋清蛋黄
 * @date: 2018/12/31
 * @description:
 */
public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        LitePal.initialize(context);
    }

    public static Context getContext(){
        return context;
    }


}
