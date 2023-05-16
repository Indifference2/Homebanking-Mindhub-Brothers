package com.Mindhub.homebanking.utils;

import com.Mindhub.homebanking.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;


public class Utils {
    public static int randomNumber(int min, int max){
        return (int) (Math.random() * max + min );
    }

    public static LocalDateTime dateToLocalDateTime (Date date){
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public static String getStringDateFromLocalDateTime(LocalDateTime date){
        int year = date.getYear();
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();

        return year + "-" + month + "-" + day;
    }

    public static String getStringHourFromLocalDateTime(LocalDateTime date){
        int hour = date.getHour();
        int min = date.getMinute();
        int sec = date.getSecond();

        return String.valueOf(hour<10?'0':"") + hour + ":" + (min<10?'0':"") + min + ":" + (sec<10?'0':"") + sec;
    }

    public static Date stringtoDate (String string) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd").parse(string);
    }
}
