package com.chandler.location.example.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 类功能描述
 *
 * @author 钱丁君-chandler 2023/8/16 09:00
 * @since 1.8
 */
public class DateUtil {
    public static String formatDate = "yyyy-MM-dd";
    public static String formatDate2 = "yyyy/MM/dd";
    public static String formatDate3 = "yyyy/M/d";
    public static String formatTime = "yyyy-MM-dd HH:mm:ss";
    public static String formatTime2 = "yyyy/MM/dd HH:mm:ss";
    public static String formatTime3 = "yyyy/M/d HH:mm:ss";
    public static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(formatDate);
    public static SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat(formatTime);
    public static SimpleDateFormat DAY_START_DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
    public static SimpleDateFormat DAY_END_DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd 00:00:00");

    private DateUtil() {
    }

    public static Date getDayStartTime() {
        return getDayStartTime(new Date());
    }

    public static Date getDayStartTime(Date date) {
        String format = DAY_START_DATE_FORMATTER.format(date);
        return stringToDay(format);
    }

    public static Date getDayEndTime() {
        return getDayEndTime(new Date());
    }

    public static Date getDayEndTime(Date date) {
        String format = DAY_END_DATE_FORMATTER.format(date);
        return stringToDay(format);
    }

    public static Date stringToDay(String format) {
        format = format.trim();
        String f = formatTime;
        if (formatTime.length() == format.length()) {
            if (format.contains("/")) {
                f = formatTime2;
            }
        } else if(formatDate.length()==format.length()){
            f = formatDate;
            if(format.contains("/")){
                f = formatDate2;
            }
        }else if(formatDate.length()<format.length()&&format.length()<formatTime.length()){
            f = formatTime3;
        } else if (format.length()<formatDate.length()) {
            f = formatDate3;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(f);
        try {
            return dateFormat.parse(format);
        } catch (ParseException e) {
            throw new RuntimeException(String.format("获取%s时间发生异常！", format), e);
        }
    }

    public static void main(String[] args) {
        System.out.println(stringToDay("2023/01/02 18:00:00"));
        System.out.println(stringToDay("2023/11/2 18:00:00"));
        System.out.println(stringToDay("2023/1/12 18:00:00"));
        System.out.println(stringToDay("2023-01-02 18:00:00"));
        System.out.println(stringToDay("2023-01-02"));
        System.out.println(stringToDay("2023/01/02"));
        System.out.println(stringToDay("2023/1/2"));
    }
}
