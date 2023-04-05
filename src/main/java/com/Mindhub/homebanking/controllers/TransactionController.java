package com.Mindhub.homebanking.controllers;

import com.Mindhub.homebanking.models.Transaction;
import com.Mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    private TransactionRepository transactionRepository;
    @RequestMapping("/transactions")
    public List<Transaction> getTransactions(){return transactionRepository.findAll();}
}
