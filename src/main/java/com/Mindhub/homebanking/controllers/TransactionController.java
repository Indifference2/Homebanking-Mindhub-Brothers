package com.Mindhub.homebanking.controllers;

import com.Mindhub.homebanking.dtos.AccountDTO;
import com.Mindhub.homebanking.dtos.ClientDTO;
import com.Mindhub.homebanking.dtos.TransactionDTO;
import com.Mindhub.homebanking.models.Account;
import com.Mindhub.homebanking.models.Client;
import com.Mindhub.homebanking.models.Transaction;
import com.Mindhub.homebanking.models.TransactionType;
import com.Mindhub.homebanking.services.AccountService;
import com.Mindhub.homebanking.services.ClientService;
import com.Mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;



@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientService clientService;
    @RequestMapping("/transactions")
    public List<TransactionDTO> getTransactions(){
        return transactionService.getAllTransactions();
    }

    @RequestMapping("/accounts/{id}/transactions")
    public ResponseEntity<Object> getTransactionsByAccountId(@PathVariable Long id, Authentication authentication){
        Account accountRequest = accountService.findById(id);
        ClientDTO clientAuthenticated = clientService.getCurrentClient(authentication);

        if(accountRequest == null){
            return new ResponseEntity<>("The account doesn't exist", HttpStatus.FORBIDDEN);
        }
        AccountDTO accountDTORequest = new AccountDTO(accountRequest);

        if(clientAuthenticated.getAccount()
                .stream()
                .noneMatch(account -> account.getNumber().equals(accountRequest.getNumber()))){
            return new ResponseEntity<>("This account doesn't belong to you", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(accountDTORequest.getTransaction(), HttpStatus.ACCEPTED);
    }

    @RequestMapping("/transactions/date")
    public ResponseEntity<Object> getTransactionsByDates (@RequestParam Date startDate, Date endDate){
        if (startDate == null){
            return new ResponseEntity<>("Start date can't be null", HttpStatus.FORBIDDEN);
        }
        if(endDate == null){
            return new ResponseEntity<>("End date can't be null", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> createTransaction(
            @RequestParam double balance, @RequestParam String description,
            @RequestParam String numberAccountOrigin, @RequestParam String numberAccountDestiny,
            Authentication authentication){

        Account accountOrigin = accountService.findByNumber(numberAccountOrigin);
        Account accountDestiny = accountService.findByNumber(numberAccountDestiny);
        Client clientAuthenticated = clientService.findByEmail(authentication.getName());

        if(accountOrigin == null){
            return new ResponseEntity<>("Account origin doesn't exist", HttpStatus.FORBIDDEN);
        }
        if(accountDestiny == null){
            return new ResponseEntity<>("Account destiny doesn't exist", HttpStatus.FORBIDDEN);
        }
        if(clientAuthenticated.getAccounts()
                .stream().noneMatch(account -> account.getNumber().contains(numberAccountOrigin))){
            return new ResponseEntity<>("This account doesn't belong you", HttpStatus.FORBIDDEN);
        }
        if(accountOrigin.getBalance() <= balance){
            return new ResponseEntity<>("You don't have enough balance", HttpStatus.FORBIDDEN);
        }
        if(numberAccountOrigin.equals(numberAccountDestiny)){
            return new ResponseEntity<>("Account origin number and account destiny number can't be the same", HttpStatus.FORBIDDEN);
        }
        if (balance < 1.00){
            return new ResponseEntity<>("Balance has be greater than US$1.00", HttpStatus.FORBIDDEN);
        }
        if(description.isBlank()){
            return new ResponseEntity<>("Description is in blank", HttpStatus.FORBIDDEN);
        }
        if(numberAccountOrigin.isBlank()){
            return new ResponseEntity<>("Account origin is in blank", HttpStatus.FORBIDDEN);
        }
        if(numberAccountDestiny.isBlank()){
            return new ResponseEntity<>("Account destiny is in blank", HttpStatus.FORBIDDEN);
        }

        Transaction transactionDebit = new Transaction(TransactionType.DEBIT, balance, description, LocalDateTime.now());
        accountOrigin.addTransaction(transactionDebit);
        accountOrigin.setBalance(accountOrigin.getBalance() - balance);
        transactionService.saveTransaction(transactionDebit);

        Transaction transactionCredit = new Transaction(TransactionType.CREDIT, balance, description, LocalDateTime.now());
        accountDestiny.addTransaction(transactionCredit);
        accountDestiny.setBalance(accountDestiny.getBalance() + balance);
        transactionService.saveTransaction(transactionCredit);

        accountService.saveAccount(accountOrigin);
        accountService.saveAccount(accountDestiny);

        return new ResponseEntity<>("The transactions was successfully", HttpStatus.CREATED);
    }
}
