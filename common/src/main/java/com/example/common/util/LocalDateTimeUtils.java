package com.example.common.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * LocalDateTime工具类
 */
public class LocalDateTimeUtils {
    /**
     * Date 转 LocalDateTime
     *
     * @return
     */
    public static LocalDateTime convert(Date date) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
    }

    /**
     * LocalDateTime 转 Date
     *
     * @return
     */
    public static Date convert(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * LocalDateTime转时间格式字符串
     *
     * @param localDateTime 时间
     * @param format        日期格式
     * @return string
     */
    public static String formatToString(LocalDateTime localDateTime, String format) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        return localDateTime.format(dateTimeFormatter);
    }

    /**
     * 时间字符串 转LocalDateTime
     *
     * @param localDateTime 时间字符串
     * @param format        日期格式
     * @return LocalDateTime
     */
    public static LocalDateTime stringToFormat(String localDateTime, String format) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        return LocalDateTime.parse(localDateTime, dateTimeFormatter);
    }

}
