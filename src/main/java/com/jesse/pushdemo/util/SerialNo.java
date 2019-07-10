package com.jesse.pushdemo.util;

import lombok.extern.slf4j.Slf4j;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author jesse
 * 取序列号类
 */
@Slf4j
public class SerialNo {
    private static long sequence;
    private static String compareTime;
    private static NumberFormat numberFormat;

    static {
        numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(false);
        numberFormat.setMinimumIntegerDigits(5);
        numberFormat.setMaximumIntegerDigits(5);
    }

    /**
     * 生成唯一序列号
     * 根据当前时间加五位序号，一共20位
     * @return 序列号
     */
    public static synchronized String getUNID() {
        String currentTime = getCurrentDateString("yyMMddHHmmssSSS");
        if (compareTime == null || compareTime.compareTo(currentTime) != 0) {
            compareTime = currentTime;
            sequence = 1;
        } else {
            sequence++;
        }
        int i = (int) (Math.random() * 9000 + 1000);
        return currentTime + i + sequence;
    }

    public static String getCurrentDateString(String fmt) {
        String str = "";
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(fmt, Locale.PRC);
            str = dateFormat.format(new Date());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("date2str 日期" + str + "换字符 " + fmt + "错误");
        }
        return str;
    }


}
