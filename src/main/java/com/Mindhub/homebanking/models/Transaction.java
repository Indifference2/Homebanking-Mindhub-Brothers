package com.Mindhub.homebanking.models;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
public class Transaction {
    private Long id;
    private TransactionType type;
    private double amount;
    private String description;
    private LocalDateTime date;
}

