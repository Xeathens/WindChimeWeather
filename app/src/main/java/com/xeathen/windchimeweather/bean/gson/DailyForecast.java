package com.xeathen.windchimeweather.bean.gson;

/**
 * @author: 蛋清蛋黄
 * @date: 2018/12/30
 * @description:
 */
public class DailyForecast {

    public String cond_code_d; //日间天气代码

    public String cond_code_n; //夜间天气代码

    public String cond_txt_d;  //日间天气描述

    public String cond_txt_n;  //夜间天气描述

    public String date;        //预报时间

    public String tmp_max;     //当日最高温

    public String tmp_min;     //当日最低温

    public String vis;         //能见度

}
