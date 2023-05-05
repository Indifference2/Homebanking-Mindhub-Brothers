package com.Mindhub.homebanking.dtos;

import com.Mindhub.homebanking.models.ClientLoan;

public class ClientLoanDTO {
    private long id;
    private String nameLoan;
    private double amount;
    private int payments;

    public ClientLoanDTO(ClientLoan clientLoan){
        this.id = clientLoan.getId();
        this.nameLoan = clientLoan.getLoan().getName();
        this.amount = clientLoan.getAmount();
        this.payments = clientLoan.getPayments();
    }

    public long getId() {return id;}
    public String getNameLoan() {return nameLoan;}
    public double getAmount() {return amount;}
    public int getPayments() {return payments;}
}
