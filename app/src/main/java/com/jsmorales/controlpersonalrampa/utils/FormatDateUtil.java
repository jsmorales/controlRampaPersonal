package com.jsmorales.controlpersonalrampa.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FormatDateUtil {

    private static final String TAG = FormatDateUtil.class.getSimpleName();

    public static String getDateFormatted(String dateString){

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String format = null;

        try {

            Calendar cal = Calendar.getInstance(); // creates calendar

            dateString = dateString.replaceAll("(.000Z)","");
            dateString = dateString.replaceAll("T"," ");

            Log.d(TAG, "String solo:"+dateString);

            cal.setTime(dateFormat.parse(dateString)); // sets calendar time/date

            cal.add(Calendar.HOUR_OF_DAY, -5); // subtract 5 hours

            format = dateFormat.format(cal.getTime());

        } catch (ParseException e) {

            e.printStackTrace();
        }

        return format;

    }

    public static String getActualDate(){

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String format = null;

        Calendar cal = Calendar.getInstance(); // creates calendar
        //String dateString = servicesArray.get(position).getStartDate();

        //format = dateFormat.parse(dateString).toString();

        cal.setTime(new Date()); // sets calendar time/date

        //cal.add(Calendar.HOUR_OF_DAY, 5); // adds hours

        format = dateFormat.format(cal.getTime());

        return format;
    }

    public static String getActualDateFormatted(String formatType){

        SimpleDateFormat dateFormat = new SimpleDateFormat(formatType);

        String format = null;

        Calendar cal = Calendar.getInstance(); // creates calendar
        //String dateString = servicesArray.get(position).getStartDate();

        //format = dateFormat.parse(dateString).toString();

        cal.setTime(new Date()); // sets calendar time/date

        //cal.add(Calendar.HOUR_OF_DAY, 5); // adds hours

        format = dateFormat.format(cal.getTime());

        return format;
    }

    public static String getActualDatePlusFive(String hours){

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String format;

        Calendar cal = Calendar.getInstance(); // creates calendar

        try {

            cal.setTime(dateFormat.parse(FormatDateUtil.getActualDate()+hours)); // sets calendar time/date

        } catch (ParseException e) {
            e.printStackTrace();
        }

        cal.add(Calendar.HOUR_OF_DAY, 5); // adds hours

        format = dateFormat.format(cal.getTime());

        return format;
    }

    public static String getFormattedDateString(String pattern, int offsetHours, boolean getDate) {

        SimpleDateFormat dateFormatDefault = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        String format;

        Calendar cal = Calendar.getInstance(); // creates calendar

        try {

            Date timeForSet = pattern.equals("") ? dateFormatDefault.parse(FormatDateUtil.getActualDate()) : dateFormat.parse(FormatDateUtil.getActualDate() + " " +pattern);

            cal.setTime(timeForSet); // sets calendar time/date

        } catch (ParseException e) {
            e.printStackTrace();
        }

        cal.add(Calendar.HOUR_OF_DAY, offsetHours); // adds hours

        format = dateFormat.format(cal.getTime());

        return getDate ? format : format.split(" ")[1];
    }

    public static String getFormattedDateCompleteString(String pattern, int offsetHours, boolean getDate) {

        SimpleDateFormat dateFormatDefault = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        String format;

        Calendar cal = Calendar.getInstance(); // creates calendar

        try {

            Date timeForSet = pattern.equals("") ? dateFormatDefault.parse(FormatDateUtil.getActualDate()) : dateFormat.parse(pattern);

            cal.setTime(timeForSet); // sets calendar time/date

        } catch (ParseException e) {
            e.printStackTrace();
        }

        cal.add(Calendar.HOUR_OF_DAY, offsetHours); // adds hours

        format = dateFormat.format(cal.getTime());

        return getDate ? format : format.split(" ")[1];
    }
}
