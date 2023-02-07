package com.crewmeister.cmcodingchallenge.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static Date convertStringToDate(String inputDate) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date =  formatter.parse(inputDate);
        } catch (ParseException e) {
            e.getMessage();
        }
        return date;
    }
}
