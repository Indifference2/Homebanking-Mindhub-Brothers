package com.Mindhub.homebanking.dtos;
public class LoanApplicationDTO {
    private long id;
    private Double amount;
    private Integer payments;
    private String accountNumber;

    public LoanApplicationDTO(long id, double amount, int payments,String accountNumber){
        this.id = id;
        this.amount = amount;
        this.payments = payments;
        this.accountNumber = accountNumber;
    }

    public long getId() {return id;}
    public Double getAmount() {return amount;}
    public Integer getPayments() {return payments;}
    public String getAccountNumber() {return accountNumber;}
}
