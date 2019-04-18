package com.xeathen.windchimeweather.util;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;

import com.xeathen.windchimeweather.R;
import com.xeathen.windchimeweather.common.MyApplication;

/**
 * @author: 蛋清蛋黄
 * @date: 2019/3/12
 * @description:
 */
public class WeatherDrawableUtil {


    public static Drawable getDrawableByTxt(String txt) {
        Context mContext = MyApplication.getContext();
        if (Build.VERSION.SDK_INT >= 21) {
            switch (txt) {
                case "阴":
                    return mContext.getDrawable(R.drawable.ic_yintian);
                case "多云":
                    return mContext.getDrawable(R.drawable.ic_duoyun);
                case "暴雨":
                case "大雨":
                    return mContext.getDrawable(R.drawable.ic_baoyu);
                case "小雨":
                case "中雨":
                    return mContext.getDrawable(R.drawable.ic_xiaoyu);
                case "晴":
                    return mContext.getDrawable(R.drawable.ic_qintian);
//                case "晴间多云":
//                case "少云":
//                case "阵雨":
                case "雾":
                case "大雾":
                case "雪":
                case "大雪":
                case "中雪":
                case "小雪":

                default:
                    return mContext.getDrawable(R.drawable.ic_weather_default);


            }
        }

        return null;
    }

    public static Drawable getMoonDrawable() {
        Context mContext = MyApplication.getContext();
        if (Build.VERSION.SDK_INT >= 21) {
            return mContext.getDrawable(R.drawable.ic_moon_1);
        }
        return null;
    }
}
