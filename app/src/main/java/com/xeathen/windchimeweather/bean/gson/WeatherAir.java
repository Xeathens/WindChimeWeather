package com.xeathen.windchimeweather.bean.gson;

import com.google.gson.annotations.SerializedName;

/**
 * @author: 蛋清蛋黄
 * @date: 2019/2/22
 * @description:
 */
public class WeatherAir {
    @SerializedName("basic")
    public BasicGson basic;

    @SerializedName("status")
    public String status;

    public UpdateGson update;

    @SerializedName("air_now_city")
    public AirCityGson airCity;
}
