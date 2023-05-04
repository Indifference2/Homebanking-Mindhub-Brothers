package com.Mindhub.homebanking.controllers;

import com.Mindhub.homebanking.dtos.AccountDTO;
import com.Mindhub.homebanking.models.Account;
import com.Mindhub.homebanking.models.Client;
import com.Mindhub.homebanking.repositories.AccountRepository;
import com.Mindhub.homebanking.repositories.ClientRepository;
import com.Mindhub.homebanking.utils.Utils;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;
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

    @RequestMapping("/clients/current/accounts")
    public List<AccountDTO> getAccountAuthenticated(Authentication authentication){
        return clientRepository.findByEmail(
                authentication.getName())
                .getAccounts()
                .stream()
                .map(account -> new AccountDTO(account))
                .collect(toList());
    }
    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication){
        Client clientAuthenticated = clientRepository.findByEmail(authentication.getName());
        if(clientAuthenticated
                .getAccounts()
                .size() == 3) {
            return new ResponseEntity<>("Max accounts reached", HttpStatus.FORBIDDEN);
        }
        Account newAccount = new Account("VIN-" + randomNumberAccount(), 0, LocalDateTime.now());
        accountRepository.save(newAccount);
        clientAuthenticated.addAccount(newAccount);
        clientRepository.save(clientAuthenticated);
        return new ResponseEntity<>("Account created successfully", HttpStatus.CREATED);
    }
    @Bean
    private String randomNumberAccount(){
        String randomNumberAccount;
        do{
            randomNumberAccount = String.valueOf(Utils.randomNumber(899999, 100000));
        }while(accountRepository.findByNumber(randomNumberAccount)!= null);
        return randomNumberAccount;
    }
}
