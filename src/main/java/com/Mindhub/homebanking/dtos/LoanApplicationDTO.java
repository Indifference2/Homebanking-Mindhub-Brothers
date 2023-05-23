package com.Mindhub.homebanking.dtos;
public class LoanApplicationDTO {
    private long id;
    private Double amount;
    private Integer payments;
    private String accountNumber;

    public long getId() {return id;}
    public Double getAmount() {return amount;}
    public Integer getPayments() {return payments;}
    public String getAccountNumber() {return accountNumber;}
}
