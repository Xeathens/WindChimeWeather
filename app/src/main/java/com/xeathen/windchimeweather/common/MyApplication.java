package com.xeathen.windchimeweather.common;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.xeathen.windchimeweather.custom.OkHttpUpdateHttpService;
import com.xeathen.windchimeweather.view.activity.MainActivity;
import com.xuexiang.xupdate.XUpdate;
import com.xuexiang.xupdate.entity.UpdateError;
import com.xuexiang.xupdate.listener.OnUpdateFailureListener;
import com.xuexiang.xupdate.utils.UpdateUtils;

import org.litepal.LitePal;

import interfaces.heweather.com.interfacesmodule.view.HeConfig;

/**
 * @author: 蛋清蛋黄
 * @date: 2018/12/31
 * @description:
 */
public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        //数据库相关
        LitePal.initialize(context);
        LitePal.getDatabase();
        //和风天气初始化设置
        HeConfig.init("HE1807241619061769", "d8d9b6e2afde458e90d135b1bdf4e767"); //init,全局处理一次即可
        HeConfig.switchToFreeServerNode(); //切换至免费节点


        //初始化友盟SDK
        UMConfigure.init(this, "5c9d87fe61f564a788000e97", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, null);
        // 选用AUTO页面采集模式
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);

        //应用内更新
        XUpdate.get()
                .debug(true) //开启debug模式，可用于问题的排查
                .isWifiOnly(true)     //默认设置只在wifi下检查版本更新
                .isGet(false)          //默认设置使用get请求检查版本
                .isAutoMode(false)    //默认设置非自动模式，可根据具体使用配置
                .param("versionCode", UpdateUtils.getVersionCode(this)) //设置默认公共请求参数
                .param("appKey", getPackageName())
                .setOnUpdateFailureListener(new OnUpdateFailureListener() { //设置版本更新出错的监听
                    @Override
                    public void onFailure(UpdateError error) {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
                .setIUpdateHttpService(new OkHttpUpdateHttpService()) //这个必须设置！实现网络请求功能。
                .init(this);   //这个必须初始化



    }

    public static Context getContext() {
        return context;
    }


}
