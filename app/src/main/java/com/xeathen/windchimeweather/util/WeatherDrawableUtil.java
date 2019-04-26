package com.xeathen.windchimeweather.util;

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


    public static Drawable getDrawableByTxt(int code) {
        Context mContext = MyApplication.getContext();
        if (Build.VERSION.SDK_INT >= 21) {
            if (code == 100) {
                return mContext.getDrawable(R.drawable.ic_qintian);

            } else if (code >= 101 && code <= 103) {
                return mContext.getDrawable(R.drawable.ic_duoyun);
            } else if (code == 104) {
                return mContext.getDrawable(R.drawable.ic_yintian);

            } else if (code >= 200 && code <= 213) {
                return mContext.getDrawable(R.drawable.ic_wind);

            } else if (code >= 300 && code <= 301) {
                return mContext.getDrawable(R.drawable.ic_zhongyu);

            } else if (code >= 302 && code <= 304) {
                return mContext.getDrawable(R.drawable.ic_leiyu);
            } else if (code == 305) {
                return mContext.getDrawable(R.drawable.ic_xiaoyu);

            } else if (code == 306) {
                return mContext.getDrawable(R.drawable.ic_zhongyu);
            } else if (code >= 307 && code <= 308) {
                return mContext.getDrawable(R.drawable.ic_dayu);

            } else if (code == 309) {
                return mContext.getDrawable(R.drawable.ic_xiaoyu);

            } else if (code >= 310 && code <= 312) {
                return mContext.getDrawable(R.drawable.ic_baoyu);

            } else if (code >= 313 && code <= 318) {
                return mContext.getDrawable(R.drawable.ic_zhongyu);

            } else if (code == 399) {
                return mContext.getDrawable(R.drawable.ic_zhongyu);

            } else if (code == 400) {
                return mContext.getDrawable(R.drawable.ic_xiaoxue);

            } else if (code == 401) {
                return mContext.getDrawable(R.drawable.ic_zhongxue);
            } else if (code >= 402 && code <= 410) {
                return mContext.getDrawable(R.drawable.ic_daxue);
            } else if (code == 499) {
                return mContext.getDrawable(R.drawable.ic_zhongxue);

            } else if (code >= 500 && code <= 515) {
                return mContext.getDrawable(R.drawable.ic_wumai);

            } else {
                return mContext.getDrawable(R.drawable.ic_yintian);
            }
        }

        return null;
    }

    public static Drawable getMoonDrawable(String txt) {
        Context mContext = MyApplication.getContext();
        if (Build.VERSION.SDK_INT >= 21) {
            switch (txt) {
                case "多云":
                    return mContext.getDrawable(R.drawable.ic_moon_duoyun);
                case "暴雨":
                    return mContext.getDrawable(R.drawable.ic_baoyu);
                case "大雨":
                    return mContext.getDrawable(R.drawable.ic_dayu);
                case "小雨":
                    return mContext.getDrawable(R.drawable.ic_xiaoyu);
                case "中雨":
                    return mContext.getDrawable(R.drawable.ic_zhongyu);
                case "雾":
                case "大雾":
                    return mContext.getDrawable(R.drawable.ic_wumai);
                case "雪":
                    return mContext.getDrawable(R.drawable.ic_xiaoxue);
                case "大雪":
                    return mContext.getDrawable(R.drawable.ic_daxue);
                case "中雪":
                    return mContext.getDrawable(R.drawable.ic_zhongxue);
                case "小雪":
                    return mContext.getDrawable(R.drawable.ic_xiaoxue);
                default:
                    return mContext.getDrawable(R.drawable.ic_moon);

            }
        }
        return null;
    }
}
