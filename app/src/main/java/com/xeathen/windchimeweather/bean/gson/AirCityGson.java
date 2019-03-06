package com.xeathen.windchimeweather.bean.gson;

import com.google.gson.annotations.SerializedName;

/**
 * @author: 蛋清蛋黄
 * @date: 2019/1/19
 * @description:
 */
public class AirCityGson {

    @SerializedName("aqi")
    public String aqi; //空气质量指数

    @SerializedName("pub_time")
    public String time; //数据发布时间

    public String qlty; //空气质量描述,如'轻度污染'

    public String pm10;

    public String pm25;

}
