package com.Mindhub.homebanking.dtos;



import com.Mindhub.homebanking.models.Account;
import com.Mindhub.homebanking.models.Transaction;
import com.Mindhub.homebanking.models.TransactionType;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.Locale;

public class TransactionDTO {
    private long id;
    private double amount;
    private String description;
    private LocalDateTime date;
    private TransactionType type;
    private double balanceAccount;
    public TransactionDTO(Transaction transaction){
        this.id = transaction.getId();
        this.amount = transaction.getAmount();
        this.description = transaction.getDescription();
        this.date = transaction.getDate();
        this.type = transaction.getType();
        this.balanceAccount = transaction.getBalanceAccount();
    }
    public long getId() {return id;}
    public double getAmount() {return amount;}
    public String getDescription() {return description;}
    public LocalDateTime getDate() {return date;}
    public TransactionType getType() {return type;}
    public double getBalanceAccount() {return balanceAccount;}
}
