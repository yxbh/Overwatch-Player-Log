package me.benh.overwatchplayerlog.helpers;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by benhuang on 25/11/16.
 */

public final class DateTimeHelper {
    private DateTimeHelper() {}

    public static String getDateTimeFormat() {
        return "yyyy-MM-dd HH:mm:ss";
    }

    public static Date toSqlDate(String dateTime) throws ParseException {
        return new java.sql.Date(toUtilDate(dateTime).getTime());
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
