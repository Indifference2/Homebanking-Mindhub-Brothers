package com.Mindhub.homebanking.dtos;

import com.Mindhub.homebanking.models.Account;
import com.Mindhub.homebanking.models.AccountType;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountDTO {
    private long id;
    private String number;
    private LocalDateTime creationMode;
    private double balance;
    private Set<TransactionDTO> transaction;
    private boolean active;
    private AccountType accountType;
    public AccountDTO(Account account){
        this.id = account.getId();
        this.number = account.getNumber();
        this.creationMode = account.getCreationDate();
        this.balance = account.getBalance();
        this.active = account.isActive();
        this.transaction = account.getTransactions()
                .stream()
                .filter(transaction -> transaction.isActive())
                .map(transaction -> new TransactionDTO(transaction))
                .collect(Collectors.toSet());
        this.accountType = account.getAccountType();
    }

    public long getId() {return id;}
    public String getNumber() {return number;}
    public void setNumber(String number) {this.number = number;}
    public LocalDateTime getCreationMode() {return creationMode;}
    public void setCreationMode(LocalDateTime creationMode) {this.creationMode = creationMode;}
    public double getBalance() {return balance;}
    public void setBalance(double balance) {this.balance = balance;}
    public Set<TransactionDTO> getTransaction() {return transaction;}
    public boolean isActive() {return active;}
    public AccountType getAccountType() {return accountType;}
}
