package com.xeathen.windchimeweather.view.activity;


import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.xeathen.lib.utils.LogUtil;
import com.xeathen.windchimeweather.R;
import com.xeathen.windchimeweather.bean.db.CityDB;
import com.xeathen.windchimeweather.util.SharedPreferencesUtil;


public class MainActivity extends BaseActivity {


    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = null;

    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //读取当前选择城市
        String currentCityId = SharedPreferencesUtil.getInstance().getString("current_city_id", null);
        if (currentCityId != null) { //已选择过城市


            Intent intent = new Intent(this, WeatherActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();

        } else { //未选择过任何城市
//            initAmap();
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
//
//            } else {
//                LogUtil.i("[onLocation]", "启动定位");
//                getLocation();
//            }
            Intent intent = new Intent(this, SearchActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();

        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LogUtil.i("[onLocation]", "启动定位");
                    getLocation();
                } else {
                    toastLong(this, "您拒绝了定位权限");
                    //
                    Intent intent = new Intent(this, SearchActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            default:
        }


    }

    private void initAmap() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //初始化定位回调监听器
        mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                LogUtil.i("[onLocation]", aMapLocation.toString());
                SharedPreferencesUtil.getInstance().saveString("current_city_id", aMapLocation.getAdCode());
                saveCity(aMapLocation);
                Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                mLocationClient.stopLocation();
                mLocationClient.onDestroy();

                finish();

            }
        };
        mLocationClient.setLocationListener(mLocationListener);
        mLocationOption = new AMapLocationClientOption();

        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving)
                .setLocationCacheEnable(false);
//                    .setOnceLocation(true)
//                    .setOnceLocationLatest(true);

        mLocationClient.setLocationOption(mLocationOption);
    }

    private void getLocation() {
        mLocationClient.startLocation();

    }

    private void saveCity(AMapLocation aMapLocation) {
        CityDB cityDB = new CityDB();
        cityDB.setName(aMapLocation.getCity());
        cityDB.setCityId(aMapLocation.getAdCode());
        cityDB.setParentCity(aMapLocation.getCity());
        cityDB.setAdminArea(aMapLocation.getProvince());
        cityDB.setCountry(aMapLocation.getCountry());
        cityDB.save();

    }

    @Override
    public int initLayout() {
        return R.layout.activity_main;
    }


}
