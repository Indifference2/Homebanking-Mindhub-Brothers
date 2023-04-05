package com.Mindhub.homebanking.dtos;

import com.Mindhub.homebanking.models.Account;
import com.Mindhub.homebanking.models.Transaction;

import java.time.LocalDateTime;
import java.util.Set;

public class AccountDTO {
    private String number;
    private LocalDateTime creationMode;
    private double balance;
    private Set<Transaction> transaction;
    public AccountDTO(Account account){
        this.number = account.getNumber();
        this.creationMode = account.getCreationDate();
        this.balance = account.getBalance();
        this.transaction = account.getTransactions();
    }
    public String getNumber() {return number;}
    public void setNumber(String number) {this.number = number;}
    public LocalDateTime getCreationMode() {return creationMode;}
    public void setCreationMode(LocalDateTime creationMode) {this.creationMode = creationMode;}
    public double getBalance() {return balance;}
    public void setBalance(double balance) {this.balance = balance;}
    public Set<Transaction> getTransaction() {return transaction;}
}
