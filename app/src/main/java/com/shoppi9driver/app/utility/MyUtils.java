package com.shoppi9driver.app.utility;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class MyUtils {

    public static String converDateTime(String timestampStr) {
        long unixSeconds = Long.parseLong(timestampStr);
        Date date = new Date(unixSeconds * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy | HH:mm a");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
        String formattedDate = sdf.format(date);
        return formattedDate;
    }
}
