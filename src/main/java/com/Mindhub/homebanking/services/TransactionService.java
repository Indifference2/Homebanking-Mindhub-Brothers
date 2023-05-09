package com.Mindhub.homebanking.services;

import com.Mindhub.homebanking.dtos.TransactionDTO;
import com.Mindhub.homebanking.models.Transaction;

import java.util.List;

public interface TransactionService {
    List<TransactionDTO> getTransactions();
    void saveTransaction(Transaction transaction);
}
