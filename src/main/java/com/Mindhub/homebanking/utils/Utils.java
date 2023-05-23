package com.Mindhub.homebanking.utils;

import com.Mindhub.homebanking.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;


public final class Utils {
    public static int randomNumber(int min, int max){
        return (int) (Math.random() * max + min );
    }
    public static String capitalize(String str){return str.substring(0,1).toUpperCase() + str.substring(1).toLowerCase();}
}
