package com.Mindhub.homebanking.models;

import com.Mindhub.homebanking.dtos.AccountDTO;
import com.Mindhub.homebanking.repositories.AccountRepository;
import com.Mindhub.homebanking.utils.Utils;
import com.jayway.jsonpath.JsonPath;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String number;
    private LocalDateTime creationDate;
    private double balance;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;
    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER)
    Set<Transaction> transactions = new HashSet<>();
    public Account(){}
    public Account(String number, double balance, LocalDateTime creationDate){
        this.number = number;
        this.balance = balance;
        this.creationDate = creationDate;
    }
    public void addTransaction(Transaction transaction){
        transaction.setAccount(this);
        transactions.add(transaction);
    }
    public Set<Transaction> getTransactions(){return transactions;}
    public long getId() {return id;}
    public String getNumber() {return number;}
    public void setNumber(String number) {this.number = number;}
    public LocalDateTime getCreationDate() {return creationDate;}
    public void setCreationDate(LocalDateTime creationDate) {this.creationDate = creationDate;}
    public double getBalance() {return balance;}
    public void setBalance(double balance) {this.balance = balance;}
    public Client getClient() {return client;}
    public void setClient(Client client) {this.client = client;}
}

