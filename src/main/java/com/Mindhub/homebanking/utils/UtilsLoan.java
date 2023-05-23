package com.Mindhub.homebanking.utils;

import java.util.List;

import static java.util.stream.Collectors.toList;

public final class UtilsLoan {
    public static Double calculateInterest (double amount, double interest){
        return amount + (amount * (interest /100));
    }
    public static List<Integer> paymentsOrder (List<Integer> payments){
        return payments.stream().sorted().collect(toList());
    }
}
