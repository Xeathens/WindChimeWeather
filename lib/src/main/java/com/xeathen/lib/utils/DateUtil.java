package com.xeathen.lib.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author: 蛋清蛋黄
 * @date: 2019/2/19
 * @description:
 */
public class DateUtil {
    public static int getDayofWeekInNumber(String dateTime) {
        Calendar cal = Calendar.getInstance();
        if (dateTime.equals("")) {
            cal.setTime(new Date(System.currentTimeMillis()));
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date;
            try {
                date = sdf.parse(dateTime);
            } catch (ParseException e) {
                date = null;
                e.printStackTrace();
            }
            if (date != null) {
                cal.setTime(new Date(date.getTime()));
            }
        }
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    public static String getDayofWeekInCh(int number){
        switch (number){
            case 1: return "周日";
            case 2: return "周一";
            case 3: return "周二";
            case 4: return "周三";
            case 5: return "周四";
            case 6: return "周五";
            case 7: return "周六";
        }
        return "Error";
    }
}
