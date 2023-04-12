package com.Mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Entity
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String name;
    private double maxAmount;
    @ElementCollection
    private List<Integer> payments = new ArrayList<Integer>();
    @OneToMany(mappedBy = "loan", fetch = FetchType.EAGER)
    private Set<ClientLoan> clientLoans = new HashSet<>();
    public Loan(){}
    public Loan(String name, double maxAmount, List<Integer> payments){
        this.name = name;
        this.maxAmount = maxAmount;
        this.payments = payments;
    }
    public void addClientLoan(ClientLoan clientLoan){
        clientLoan.setLoan(this);
        clientLoans.add(clientLoan);
    }
    public List<Client> getClients(){
        return clientLoans
                .stream()
                .map(currentClientLoan -> currentClientLoan.getClient())
                .collect(toList());
    }
    public long getId() {return id;}
    public Set<ClientLoan> getClienLoans() {return clientLoans;}
    public void setClienLoans(Set<ClientLoan> clienLoans) {this.clientLoans = clienLoans;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public double getMaxAmount() {return maxAmount;}
    public void setMaxAmount(double maxAmount) {this.maxAmount = maxAmount;}
    public List<Integer> getPayments() {return payments;}
    public void setPayments(List<Integer> payments) {this.payments = payments;}
}
