package com.Mindhub.homebanking.services;

import com.Mindhub.homebanking.dtos.TransactionDTO;
import com.Mindhub.homebanking.models.Account;
import com.Mindhub.homebanking.models.Transaction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

public interface TransactionService {
    List<TransactionDTO> getAllTransactions();
    void saveTransaction(Transaction transaction);
    List<TransactionDTO> findByAccountId(Long accountId);
    List<TransactionDTO> getTransactionsByIdAndDateBetween(Account account, LocalDateTime  startDate, LocalDateTime endDate);
    void changeAllTransactionsActive (Set<Transaction> transactions);
    void saveAllTransactions(Set<Transaction> transactions);
}
