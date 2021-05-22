package com.rvngbrl.adulting.MoneyTracker;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;

import java.io.Console;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {
    //for today
    public static String getSystemFormatDateString(Context context, Date date) {
        java.text.DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(context);
        return  dateFormat.format(date);
    }
//for reports
    public static String getSystemFormatDateString(Context context, String dateString) {
//        java.text.DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(context);
        Log.d("Utils///////",dateString);
     //   return  DateFormat.getDateFormat(context).format(dateString);
        return dateString;

    }

    public static String getDateString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy", Locale.US);
        try {
            return dateFormat.format(date);
        } catch (Exception pe) {
            pe.printStackTrace();
            return "no_date";
        }
    }

    private static Date stringToDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy", Locale.US);
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException pe) {
            pe.printStackTrace();
            return null;
        }
    }


    public static String formatToCurrency(float value) {
        final NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);
        return numberFormat.format(value);
    }
}
