package me.benh.overwatchplayerlog.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by benhuang on 25/11/16.
 */

public final class DateTimeHelper {
    private DateTimeHelper() {}

    public static String getDateTimeFormat() {
        return "yyyy-MM-dd HH:mm:ss";
    }

    public static java.util.Date getCurrentUtilDate() {
        return Calendar.getInstance().getTime();
    }

    public static java.sql.Date getCurrentSqlDate() {
        return toSqlDate(getCurrentUtilDate());
    }

    public static java.sql.Date toSqlDate(java.util.Date dateTime) {
        return new java.sql.Date(dateTime.getTime());
    }

    public static java.sql.Date toSqlDate(String dateTime) throws ParseException {
        return toSqlDate(toUtilDate(dateTime));
    }

    public static java.util.Date toUtilDate(String dateTime) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(getDateTimeFormat(), Locale.getDefault());
        return formatter.parse(dateTime);
    }

    public static String toString(java.sql.Date dateTime) {
        SimpleDateFormat formatter = new SimpleDateFormat(getDateTimeFormat(), Locale.getDefault());
        return formatter.format(dateTime);
    }

    public static String toString(java.util.Date dateTime) {
        SimpleDateFormat formatter = new SimpleDateFormat(getDateTimeFormat(), Locale.getDefault());
        return formatter.format(dateTime);
    }

}
