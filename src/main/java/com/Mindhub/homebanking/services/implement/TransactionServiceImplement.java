package com.Mindhub.homebanking.services.implement;

import com.Mindhub.homebanking.dtos.TransactionDTO;
import com.Mindhub.homebanking.models.Account;
import com.Mindhub.homebanking.models.Transaction;
import com.Mindhub.homebanking.repositories.AccountRepository;
import com.Mindhub.homebanking.repositories.TransactionRepository;
import com.Mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class TransactionServiceImplement implements TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Override
    public List<TransactionDTO> getAllTransactions() {
        return transactionRepository.findAll()
                .stream()
                .map(transaction -> new TransactionDTO(transaction))
                .collect(toList());
    }

    @Override
    public void saveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }
    @Override
    public List<TransactionDTO> findByAccountId(Long accountId) {
        return transactionRepository.findByAccountId(accountId)
                .stream()
                .map(transaction -> new TransactionDTO(transaction))
                .collect(toList());
    }

    @Override
    public List<TransactionDTO> getTransactionsByIdAndDateBetween(Account account, LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.findByIdAndDateBetween(account, startDate, endDate)
                .stream()
                .map(transaction -> new TransactionDTO(transaction))
                .collect(toList());
    }
}
