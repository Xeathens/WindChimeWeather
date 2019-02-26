package com.xeathen.windchimeweather.bean.model;

import org.litepal.crud.LitePalSupport;

/**
 * @author: 蛋清蛋黄
 * @date: 2018/12/28
 * @description: 城市类
 */
public class City {

    private String name;

    private String ID;

    private String parentCity;

    private String adminArea;

    private String country;

    public City(String name, String ID, String parentCity, String adminArea, String country) {
        this.name = name;
        this.ID = ID;
        this.parentCity = parentCity;
        this.adminArea = adminArea;
        this.country = country;
    }

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

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getParentCity() {
        return parentCity;
    }

    public void setParentCity(String parentCity) {
        this.parentCity = parentCity;
    }
}
