package com.Mindhub.homebanking.models;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String cardHolder;
    private CardType cardType;
    private CardColor cardColor;
    private String number;
    private int cvv;
    private LocalDate fromDate;
    private LocalDate thruDate;
    @ManyToOne(fetch = FetchType.EAGER)
    private Client client;
    public Card(){}
    public Card(String cardHolder, CardType cardType, CardColor cardColor,
                String number, int cvv, LocalDate fromDate, LocalDate thruDate){
        this.cardHolder = cardHolder;
        this.cardType = cardType;
        this.cardColor = cardColor;
        this.number = number;
        this.cvv = cvv;
        this.fromDate = fromDate;
        this.thruDate = thruDate;
    }

    public long getId() {return id;}
    public String getCardHolder() {return cardHolder;}
    public void setCardHolder(String cardHolder) {this.cardHolder = cardHolder;}
    public CardType getCardType() {return cardType;}
    public void setCardType(CardType cardType) {this.cardType = cardType;}
    public CardColor getCardColor() {return cardColor;}
    public void setCardColor(CardColor cardColor) {this.cardColor = cardColor;}
    public String getNumber() {return number;}
    public void setNumber(String number) {this.number = number;}
    public int getCvv() {return cvv;}
    public void setCvv(int cvv) {this.cvv = cvv;}
    public LocalDate getFromDate() {return fromDate;}
    public void setFromDate(LocalDate fromDate) {this.fromDate = fromDate;}
    public LocalDate getThruDate() {return thruDate;}
    public void setThruDate(LocalDate thruDate) {this.thruDate = thruDate;}
    public Client getClient() {return client;}
    public void setClient(Client client) {this.client = client;}
}
