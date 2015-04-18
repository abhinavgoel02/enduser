package com.wheels2spin.enduser.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Abhinav Goel on 4/1/2015.
 */
public class DateFormatter {

    public static String getFormattedDate(int year, int month, int dayOfMonth) {
        Calendar calendar = new GregorianCalendar(year, month, dayOfMonth);
        SimpleDateFormat format = new SimpleDateFormat("EEE, MMM dd");
        return format.format(calendar.getTime());
    }

    public static String getFormattedDate(GregorianCalendar c) {
        SimpleDateFormat format = new SimpleDateFormat("EEE, MMM dd");
        return format.format(c.getTime());
    }
}
