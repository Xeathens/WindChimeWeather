package com.xeathen.windchimeweather.bean.db;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

/**
 * @author: 蛋清蛋黄
 * @date: 2018/12/31
 * @description:
 */
public class CityDB extends LitePalSupport{

    private int id;

    private String name;

    @Column(unique = true, nullable = false)
    private String cityId;

    private String parentCity;

    private String adminArea;

    private String country;


    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAdminArea() {
        return adminArea;
    }

    public void setAdminArea(String adminArea) {
        this.adminArea = adminArea;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getParentCity() {
        return parentCity;
    }

    public void setParentCity(String parentCity) {
        this.parentCity = parentCity;
    }
}
