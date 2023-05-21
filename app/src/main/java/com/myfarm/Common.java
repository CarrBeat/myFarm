package com.myfarm;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Common {
    public static boolean isNotify = true;
    public static boolean showAnimalID = false;
    @SuppressLint("SimpleDateFormat")
    public static String getNormalDate(String requiredDate) throws ParseException {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = formatter.parse(requiredDate);
        formatter = new SimpleDateFormat("dd.MM.yyyy");
        assert date != null;
        return formatter.format(date);
    }
}
