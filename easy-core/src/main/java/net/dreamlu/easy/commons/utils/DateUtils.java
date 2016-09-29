package net.dreamlu.easy.commons.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by L.cm on 2016/5/18.
 */
public abstract class DateUtils {
    /**
     * 设置年
     * @param date 时间
     * @param amount 年数，-1表示减少
     * @return 设置后的时间
     */
    public static Date setYears(Date date, int amount) {
        return set(date, Calendar.YEAR, amount);
    }
    
    /**
     * 设置月
     * @param date 时间
     * @param amount 月数，-1表示减少
     * @return 设置后的时间
     */
    public static Date setMonths(Date date, int amount) {
        return set(date, Calendar.MONTH, amount);
    }
    
    /**
     * 设置周
     * @param date 时间
     * @param amount 周数，-1表示减少
     * @return 设置后的时间
     */
    public static Date setWeeks(Date date, int amount) {
        return set(date, Calendar.WEEK_OF_YEAR, amount);
    }
    
    /**
     * 设置天
     * @param date 时间
     * @param amount 天数，-1表示减少
     * @return 设置后的时间
     */
    public static Date setDays(Date date, int amount) {
        return set(date, Calendar.DATE, amount);
    }
    
    /**
     * 设置小时
     * @param date 时间
     * @param amount 小时数，-1表示减少
     * @return 设置后的时间
     */
    public static Date setHours(Date date, int amount) {
        return set(date, Calendar.HOUR_OF_DAY, amount);
    }
    
    /**
     * 设置分钟
     * @param date 时间
     * @param amount 分钟数，-1表示减少
     * @return 设置后的时间
     */
    public static Date setMinutes(Date date, int amount) {
        return set(date, Calendar.MINUTE, amount);
    }
    
    /**
     * 设置秒
     * @param date 时间
     * @param amount 秒数，-1表示减少
     * @return 设置后的时间
     */
    public static Date setSeconds(Date date, int amount) {
        return set(date, Calendar.SECOND, amount);
    }
    
    /**
     * 设置毫秒
     * @param date 时间
     * @param amount 毫秒数，-1表示减少
     * @return 设置后的时间
     */
    public static Date setMilliseconds(Date date, int amount) {
        return set(date, Calendar.MILLISECOND, amount);
    }
    
    /**
     * 设置日期属性
     * @param date 时间
     * @param calendarField 更改的属性
     * @param amount 更改数，-1表示减少
     * @return 设置后的时间
     */
    private static Date set(Date date, int calendarField, int amount) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar c = Calendar.getInstance();
        c.setLenient(false);
        c.setTime(date);
        c.add(calendarField, amount);
        return c.getTime();
    }
    
    public static final String PATTERN_DATETIME      = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN_DATE          = "yyyy-MM-dd";
    public static final String PATTERN_TIME          = "HH:mm:ss";
    
    /**
     * 日期格式化
     * @param date 时间
     * @param pattern 表达式
     * @return 格式化后的时间
     */
    public static String format(Date date, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }
    
    /**
     * 将字符串转换为时间
     * @param dateStr 时间字符串
     * @param pattern 表达式
     * @return 时间
     */
    public static Date parse(String dateStr, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        try {
            return format.parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
