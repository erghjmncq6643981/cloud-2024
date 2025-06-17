package com.chandler.instance.client.example.util;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 类功能描述
 *
 * @author 钱丁君-chandler 2023/8/16 09:00
 * @since 1.8
 */
public class DateUtil {
    public String formatHourMinuteSecond = "HH:mm:ss";
    public static String formatDate = "yyyy-MM-dd";
    public String formatTime = "yyyy-MM-dd HH:mm:ss";
    public SimpleDateFormat dateFormatter = new SimpleDateFormat(formatDate);
    public SimpleDateFormat timeFormatter = new SimpleDateFormat(formatTime);
    public SimpleDateFormat dayStartDateFormatter = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
    public SimpleDateFormat dayEndDateFormatter = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
    public static DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static DateTimeFormatter YMD_HMS_DF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public DateUtil() {
    }

    public static Date getDayStartTime() {
        return getDayStartTime(new Date());
    }

    public static Date getDayStartTime(Date date) {
        String format = new DateUtil().dayStartDateFormatter.format(date);
        return stringToDay(format);
    }

    public static Date getDayMinutes(String format) {
        if (format.length() == new DateUtil().formatHourMinuteSecond.length()) {
            return append(new DateUtil().dateFormatter.format(new Date()), format, "");
        }
        return append(new DateUtil().dateFormatter.format(new Date()), format, "00");
    }

    public static Date getDayEndTime() {
        return getDayEndTime(new Date());
    }

    public static Date getDayEndTime(Date date) {
        String format = new DateUtil().dayEndDateFormatter.format(date);
        return stringToDay(format);
    }

    public static Date stringToDay(String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(new DateUtil().formatTime);
        try {
            return dateFormat.parse(format);
        } catch (ParseException e) {
            throw new RuntimeException("获取时间发生异常！", e);
        }
    }

    public static Date append(String first, String second, String third) {
        String format = java.lang.String.format("%s %s:%s", first, second, third);
        if (StringUtils.isEmpty(third)) {
            format = java.lang.String.format("%s %s", first, second);
        }
        return stringToDay(format);
    }

    public static String  getTodayDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatDate);
        return formatter.format(date);
    }

    public static void main(String[] args) {
        LocalDate date= LocalDate.now();
        System.out.println(String.format("%s %s",getTodayDate(date),"00:00:00"));
        System.out.println(String.format("%s %s",getTodayDate(date.plusDays(2).plusMonths(1)),"00:00:00"));

//        System.out.println(new DateUtil().timeFormatter.format(append(new DateUtil().dateFormatter.format(new Date()), "09:00", "00")));
    }
}
