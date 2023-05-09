package com.Mindhub.homebanking.services.implement;

import com.Mindhub.homebanking.dtos.TransactionDTO;
import com.Mindhub.homebanking.models.Transaction;
import com.Mindhub.homebanking.repositories.TransactionRepository;
import com.Mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class TransactionServiceImplement implements TransactionService {
    @Autowired
    TransactionRepository transactionRepository;
    @Override
    public List<TransactionDTO> getTransactions() {
        return transactionRepository.findAll()
                .stream()
                .map(transaction -> new TransactionDTO(transaction))
                .collect(toList());
    }

    @Override
    public void saveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

}
