package com.synarion.rrr.Service;

import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;


@Service
public class Utils {
    public static Long getCurrentTime(){
        Date date = new Date(System.currentTimeMillis());
        return date.getTime();
    }

    public static long convertDateToMillis(String dateStr)throws Exception {
        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateStr);

        if (dateStr == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        return date.getTime(); // Returns epoch time in milliseconds
    }
}
