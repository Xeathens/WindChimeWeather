package com.xeathen.lib.utils;

import android.util.Log;

/**
 * @author: 蛋清蛋黄
 * @date: 2018/12/14
 * @description: 自定义工具类
 */
public class LogUtil {

    public static final int VERBOSE = 1;

    public static final int DEBUG = 2;

    public static final int INFO = 3;

    public static final int WARN = 4;

    public static final int ERROR = 5;

    public static final int NOTHING = 6;

    //当前等级
    public static int level = VERBOSE;

    public static void v(String tag, String msg){
        if (level <= VERBOSE){
            Log.v(tag, (new StringBuilder("--> ")).append(msg).toString());
        }
    }

    public static void d(String tag, String msg){
        if (level <= DEBUG){
            Log.d(tag, (new StringBuilder("--> ")).append(msg).toString());
        }
    }

    public static void i(String tag, String msg){
        if (level <= INFO){
            Log.i(tag, (new StringBuilder("--> ")).append(msg).toString());
        }

    }
    public static void w(String tag, String msg){
        if (level <= WARN){
            Log.w(tag, (new StringBuilder("--> ")).append(msg).toString());
        }
    }
    public static void e(String tag, String msg){
        if (level <= ERROR){
            Log.e(tag, (new StringBuilder("--> ")).append(msg).toString());
        }
    }
}
