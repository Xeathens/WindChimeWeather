package com.xeathen.windchimeweather.component;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.xeathen.windchimeweather.R;
import com.xeathen.windchimeweather.view.activity.MainActivity;

import interfaces.heweather.com.interfacesmodule.bean.weather.Weather;

/**
 * @author: 蛋清蛋黄
 * @date: 2019/4/24
 * @description:
 */
public class NotificationHelper {
    private static final int NOTIFICATION_ID = 233;

//    public static void showWeatherNotification(Context context, @NonNull Weather weather) {
//        Intent intent = new Intent(context, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        PendingIntent pendingIntent =
//                PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        Notification.Builder builder = new Notification.Builder(context);
//        Notification notification = builder.setContentIntent(pendingIntent)
//                .setContentTitle(weather.basic.city)
//                .setContentText(String.format("%s 当前温度: %s℃ ", weather.now.cond.txt, weather.now.tmp))
//                .setSmallIcon(SharedPreferenceUtil.getInstance().getInt(weather.now.cond.txt, R.mipmap.none))
//                .build();
//        notification.flags = SharedPreferenceUtil.getInstance().getNotificationModel();
//        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        manager.notify(NOTIFICATION_ID, notification);
//    }
}
