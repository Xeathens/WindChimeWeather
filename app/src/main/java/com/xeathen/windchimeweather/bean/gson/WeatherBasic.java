package com.xeathen.windchimeweather.bean.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author: 蛋清蛋黄
 * @date: 2019/2/22
 * @description:
 */
public class WeatherBasic {
    @SerializedName("basic")
    public BasicGson basic;

    @SerializedName("status")
    public String status;

    public UpdateGson update;

    @SerializedName("daily_forecast")
    public List<DailyForecast> forecastList;

    @SerializedName("lifestyle")
    public List<DailyLifeStyle> lifeStyleList;

    @SerializedName("now")
    public NowGson now;

    @SerializedName("hourly")
    public List<HourlyGson> hourlyGsonList;
}
