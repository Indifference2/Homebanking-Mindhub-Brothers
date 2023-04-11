package com.Mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
public class ClientLoan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private double amount;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client client;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="loan_id")
    private Loan loan;

    public ClientLoan(){}
    public ClientLoan(Client client, double amount, Loan loan){
        this.client = client;
        this.amount = amount;
        this.loan = loan;
    }

    public long getId() {return id;}
    public void setId(long id) {this.id = id;}
    public double getAmount() {return amount;}
    public void setAmount(double amount) {this.amount = amount;}
    public Client getClient() {return client;}
    public void setClient(Client client) {this.client = client;}
    public Loan getLoan() {return loan;}
    public void setLoan(Loan loan) {this.loan = loan;}
}
