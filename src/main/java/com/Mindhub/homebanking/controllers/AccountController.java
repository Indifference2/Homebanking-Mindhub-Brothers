package com.Mindhub.homebanking.controllers;

import com.Mindhub.homebanking.dtos.AccountDTO;
import com.Mindhub.homebanking.models.Account;
import com.Mindhub.homebanking.models.Client;

import com.Mindhub.homebanking.services.AccountService;
import com.Mindhub.homebanking.services.ClientService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;



@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientService clientService;
    @RequestMapping("/accounts")
    public List<AccountDTO> getAccounts(){
        return accountService.getAccount();
    }
    @RequestMapping("/accounts/{id}")
    public Optional<AccountDTO> getAccount(@PathVariable Long id){
        return accountService.getAccountsById(id);
    }
    @RequestMapping("/clients/current/accounts")
    public List<AccountDTO> getAccountsAuthenticated(Authentication authentication){
        return accountService.getAccountsAuthenticated(authentication);
    }
    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication){
        Client clientAuthenticated = clientService.findByEmail(authentication.getName());
        if(clientAuthenticated
                .getAccounts()
                .size() == 3) {
            return new ResponseEntity<>("Max accounts reached", HttpStatus.FORBIDDEN);
        }
        Account newAccount = new Account("VIN-" + accountService.randomNumberAccount(), 0, LocalDateTime.now());
        accountService.saveAccount(newAccount);

        clientAuthenticated.addAccount(newAccount);
        clientService.saveClient(clientAuthenticated);

        return new ResponseEntity<>("Account created successfully", HttpStatus.CREATED);
    }
}
