package com.Mindhub.homebanking.services;

import com.Mindhub.homebanking.dtos.TransactionDTO;
import com.Mindhub.homebanking.models.Transaction;

import java.util.Date;
import java.util.List;

public interface TransactionService {
    List<TransactionDTO> getAllTransactions();
    void saveTransaction(Transaction transaction);
    List<TransactionDTO> findByAccountId(Long accoundId);
//    List<Transaction> getTransactionsDateBetween(Date startDate, Date endDate);
}
