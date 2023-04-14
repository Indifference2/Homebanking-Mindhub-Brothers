package com.Mindhub.homebanking.controllers;

import com.Mindhub.homebanking.dtos.AccountDTO;
import com.Mindhub.homebanking.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;

    @RequestMapping("/accounts")
    public List<AccountDTO> getAccounts(){
        return accountRepository.findAll()
                .stream()
                .map(account -> new AccountDTO(account))
                .collect(toList());
    }
    @RequestMapping("/accounts/{id}")
    public Optional<AccountDTO> getAccount(@PathVariable Long id){
        return Optional.of(new AccountDTO(accountRepository.findById(id).orElse(null)));
    }
}
