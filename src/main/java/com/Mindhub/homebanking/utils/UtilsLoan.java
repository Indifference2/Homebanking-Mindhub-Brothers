package com.Mindhub.homebanking.utils;

import java.util.List;

import static java.util.stream.Collectors.toList;

public final class UtilsLoan {
    public static double calculateInterest (double amount, double interest){
        return (amount * (interest /100)) + amount;
    }
    public static double totalInterest(double amount, double interest, int payments) {
        return ((amount * (interest / 100)) * payments + amount);
    }
    public static List<Integer> paymentsOrder (List<Integer> payments){
        return payments.stream().sorted().collect(toList());
    }
}
