package com.xeathen.windchimeweather.bean.gson;

import com.google.gson.annotations.SerializedName;

/**
 * @author: 蛋清蛋黄
 * @date: 2018/12/29
 * @description:
 */
public class BasicGson {

    public String cid;

    @SerializedName("location")
    public String cityName;

}
