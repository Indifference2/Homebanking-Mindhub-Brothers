package com.Mindhub.homebanking.utils;

import com.Mindhub.homebanking.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


public class Utils {
    public static int randomNumber(int min, int max){
        return (int) (Math.random() * max + min );
    }
}
