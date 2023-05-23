package com.Mindhub.homebanking.controllers;

import com.Mindhub.homebanking.dtos.AccountDTO;
import com.Mindhub.homebanking.dtos.ClientDTO;
import com.Mindhub.homebanking.models.Account;
import com.Mindhub.homebanking.models.AccountType;
import com.Mindhub.homebanking.models.Client;

import com.Mindhub.homebanking.models.Transaction;
import com.Mindhub.homebanking.services.AccountService;
import com.Mindhub.homebanking.services.ClientService;

import com.Mindhub.homebanking.services.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toSet;


@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private TransactionService transactionService;
    @GetMapping("/accounts/{id}")
    public ResponseEntity<Object> getAccount(@PathVariable Long id, Authentication authentication){
        Account accountRequested = accountService.findById(id);
        ClientDTO client = clientService.getCurrentClient(authentication);
        if(accountRequested == null){
            return new ResponseEntity<>("Account doesn't exist", HttpStatus.NOT_FOUND);
        }
        if(!clientService.isAdmin(authentication)){
            if(!accountRequested.isActive()){
                return new ResponseEntity<>("Account isn't active" ,HttpStatus.NOT_ACCEPTABLE);
            }
            if(accountService.accountBelongToClient(authentication, accountRequested)){
                return new ResponseEntity<>("This account doesn't belong to you", HttpStatus.FORBIDDEN);
            }
        }
        return new ResponseEntity<>(new AccountDTO(accountRequested), HttpStatus.ACCEPTED);
    }
    @GetMapping("/accounts/clients/name")
    public ResponseEntity<String> getName(@RequestParam String numberAccount){
        Account accountDestiny = accountService.findByNumber(numberAccount);
        Client clientDestiny = accountDestiny.getClient();
        if (clientDestiny == null){
            return new ResponseEntity<>("Client destiny not found", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(clientDestiny.getFirstName() + " " + clientDestiny.getLastName(), HttpStatus.OK);
    }
    @GetMapping("/clients/current/accounts")
    public List<AccountDTO> getAccountsAuthenticated(Authentication authentication){
        return accountService.getAccountsAuthenticated(authentication);
    }
    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication, @RequestParam AccountType accountType){
        Client clientAuthenticated = clientService.findByEmail(authentication.getName());
        if(clientAuthenticated
                .getAccounts()
                .size() == 6) {
            return new ResponseEntity<>("Max accounts reached", HttpStatus.FORBIDDEN);
        }
        Account newAccount = new Account("VIN-" + accountService.randomNumberAccount(), 0, LocalDateTime.now(), true, accountType);
        accountService.saveAccount(newAccount);

        clientAuthenticated.addAccount(newAccount);
        clientService.saveClient(clientAuthenticated);

        return new ResponseEntity<>("Account created successfully", HttpStatus.CREATED);
    }
    @PutMapping("/clients/current/accounts")
    public ResponseEntity<Object> deleteAccount(Authentication authentication, long id){
        Account accountToDelete = accountService.findById(id);
        Client client = clientService.findByEmail(authentication.getName());

        if(accountToDelete == null){
            return new ResponseEntity<>("This account doesn't exist", HttpStatus.NOT_FOUND);
        }
        if(!clientService.isAdmin(authentication)){
            if(accountService.accountBelongToClient(authentication, accountToDelete)){
                return new ResponseEntity<>("This account doesn't belong to you", HttpStatus.FORBIDDEN);
            }
        }

        accountToDelete.setActive(false);
        transactionService.changeAllTransactionsActive(accountToDelete.getTransactions());
        transactionService.saveAllTransactions(accountToDelete.getTransactions());
        accountService.saveAccount(accountToDelete);

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
