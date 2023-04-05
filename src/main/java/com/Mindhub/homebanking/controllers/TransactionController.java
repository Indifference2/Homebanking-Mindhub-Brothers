package com.Mindhub.homebanking.controllers;

import com.Mindhub.homebanking.dtos.TransactionDTO;
import com.Mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;


@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    private TransactionRepository transactionRepository;
    @RequestMapping("/transactions")
    public List<TransactionDTO> getTransactions(){
        return transactionRepository.findAll()
                .stream()
                .map(transaction -> new TransactionDTO(transaction))
                .collect(toList());
    }
    @RequestMapping("/transactions/{id}")
    public Optional<TransactionDTO> getTransaction(@PathVariable Long id){
        return transactionRepository.findById(id).map(transaction -> new TransactionDTO(transaction));
    }
}
