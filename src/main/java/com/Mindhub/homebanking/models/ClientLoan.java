package com.Mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
public class ClientLoan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String nameLoan;
    private int payments;
    private double amount;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client client;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="loan_id")
    private Loan loan;

    public ClientLoan(){}
    public ClientLoan(String nameLoan, double amount, int payments){
        this.amount = amount;
        this.nameLoan = nameLoan;
        this.payments = payments;
    }
    public int getPayments() {return payments;}
    public void setPayments(int payments) {this.payments = payments;}
    public String getNameLoan() {return nameLoan;}
    public void setNameLoan(String nameLoan) {this.nameLoan = nameLoan;}
    public long getId() {return id;}
    public double getAmount() {return amount;}
    public void setAmount(double amount) {this.amount = amount;}
    public Client getClient() {return client;}
    public void setClient(Client client) {this.client = client;}
    public Loan getLoan() {return loan;}
    public void setLoan(Loan loan) {this.loan = loan;}
}
