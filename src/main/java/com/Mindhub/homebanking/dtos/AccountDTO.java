package com.Mindhub.homebanking.dtos;

import com.Mindhub.homebanking.models.Account;

import java.time.LocalDateTime;

public class AccountDTO {
    private String number;
    private LocalDateTime creationMode;
    private double balance;

    public AccountDTO(Account account){
        this.number = account.getNumber();
        this.creationMode = account.getCreationDate();
        this.balance = account.getBalance();
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDateTime getCreationMode() {
        return creationMode;
    }

    public void setCreationMode(LocalDateTime creationMode) {
        this.creationMode = creationMode;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
