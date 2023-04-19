package com.Mindhub.homebanking.dtos;

import com.Mindhub.homebanking.models.Card;
import com.Mindhub.homebanking.models.CardColor;
import com.Mindhub.homebanking.models.CardType;

import java.time.LocalDate;

public class CardDTO {
    private long id;
    private String cardHolder;
    private CardType cardType;
    private CardColor cardColor;
    private String number;
    private int cvv;
    private LocalDate fromDate;
    private LocalDate thruDate;
    public CardDTO(Card card){
        this.id = card.getId();
        this.cardHolder = card.getCardHolder();
        this.cardType = card.getCardType();
        this.cardColor = card.getCardColor();
        this.number = card.getNumber();
        this.cvv = card.getCvv();
        this.fromDate = card.getFromDate();
        this.thruDate = card.getThruDate();
    }

    public long getId() {return id;}
    public String getCardHolder() {return cardHolder;}
    public CardType getCardType() {return cardType;}
    public CardColor getCardColor() {return cardColor;}
    public String getNumber() {return number;}
    public int getCvv() {return cvv;}
    public LocalDate getFromDate() {return fromDate;}
    public LocalDate getThruDate() {return thruDate;}
}
