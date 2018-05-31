package com.darren.chinesebrand.app.util;

import java.util.Calendar;

/**
 * description:
 * author: Darren on 2017/12/4 10:37
 * email: 240336124@qq.com
 * version: 1.0
 */
public class DateUtil {
    private DateUtil() {
    }

    public static int currentYear() {
        Calendar now = Calendar.getInstance();
        return now.get(Calendar.YEAR);
    }

    /**
     * 根据年 月 获取对应的月份 天数
     */
    public static int daysByYearMonth(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }
}
