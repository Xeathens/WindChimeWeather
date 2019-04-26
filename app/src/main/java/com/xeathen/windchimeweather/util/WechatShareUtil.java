package com.xeathen.windchimeweather.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

/**
 * @author: 蛋清蛋黄
 * @date: 2019/4/21
 * @description:
 */
public class WechatShareUtil {

    public static void toFriends(Context context, String text) {
        Intent intent = new Intent();
        ComponentName comp = new ComponentName("com.tencent.mm",
                "com.tencent.mm.ui.tools.ShareImgUI");
        intent.setComponent(comp);
        intent.setAction("android.intent.action.SEND");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        context.startActivity(intent);
    }

    public static void toTimeLine(Context context, String text) {
        Intent intent = new Intent();
        ComponentName comp = new ComponentName("com.tencent.mm",
                "com.tencent.mm.ui.tools.ShareToTimeLineUI");
        intent.setComponent(comp);
        intent.setAction("android.intent.action.SEND");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        context.startActivity(intent);
    }
}
