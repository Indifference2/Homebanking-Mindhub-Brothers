package com.Mindhub.homebanking.dtos;

import java.util.List;

public class LoanCreationDTO {
    private String name;
    private double maxAmount;
    private List<Integer> payments;
    private double interest;

    public LoanCreationDTO(){}


    public String getName() {return name;}
    public double getMaxAmount() {return maxAmount;}

    public List<Integer> getPayments() {return payments;}
    public double getInterest() {return interest;}
}
