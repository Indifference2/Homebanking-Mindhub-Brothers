package com.Mindhub.homebanking.controllers;

import com.Mindhub.homebanking.dtos.LoanApplicationDTO;
import com.Mindhub.homebanking.dtos.LoanDTO;
import com.Mindhub.homebanking.models.*;
import com.Mindhub.homebanking.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class LoanController {
    @Autowired
    private LoanService loanService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientLoanService clientLoanService;
    @Autowired
    private TransactionService transactionService;

    @RequestMapping("/loans")
    public List<LoanDTO> getLoans(){
        return loanService.getLoans();
    }
    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> AskLoan (@RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication) {
        Client clientAuthenticated = clientService.findByEmail(authentication.getName());
        Optional<Loan> loan = loanService.findById(loanApplicationDTO.getId());
        Account accountReceiver = accountService.findByNumber(loanApplicationDTO.getAccountNumber().toUpperCase());

        Set<ClientLoan> clientLoanSet;

        if (loan.isEmpty()) {
            return new ResponseEntity<>("Loan doesn't exist", HttpStatus.FORBIDDEN);
        } else {
            clientLoanSet = clientLoanService.getClientLoansAuthenticated(clientAuthenticated, loan.get());
        }
        if (accountReceiver == null) {
            return new ResponseEntity<>("Account doesn't exist", HttpStatus.FORBIDDEN);
        }
        if (loanApplicationDTO.getAmount().isNaN()) {
            return new ResponseEntity<>("Amount can't be on blank", HttpStatus.FORBIDDEN);
        }
        if (loanApplicationDTO.getAmount() < 1) {
            return new ResponseEntity<>("Amount can't be negative or below 1", HttpStatus.FORBIDDEN);
        }
        if (loanApplicationDTO.getAmount() > loan.get().getMaxAmount()) {
            return new ResponseEntity<>("Amount can't be greater than max amount permitted", HttpStatus.FORBIDDEN);
        }
        if (!loan.get().getPayments().contains(loanApplicationDTO.getPayments())) {
            return new ResponseEntity<>("That payment isn't permitted", HttpStatus.FORBIDDEN);
        }
        if (!clientAuthenticated.getAccounts().contains(accountReceiver)) {
            return new ResponseEntity<>("The Account doesn't belong you", HttpStatus.FORBIDDEN);
        }
        if (clientLoanSet.size() > 0) {
            return new ResponseEntity<>("You already have this loan", HttpStatus.FORBIDDEN);
        }

        ClientLoan clientLoan = new ClientLoan(loanApplicationDTO.getAmount() + (loanApplicationDTO.getAmount() * 0.2), loanApplicationDTO.getPayments());
        clientLoanService.saveClientLoan(clientLoan);

        Transaction transaction = new Transaction(TransactionType.CREDIT, loanApplicationDTO.getAmount(), loan.get().getName() + " loan approved", LocalDateTime.now());
        transactionService.saveTransaction(transaction);

        //ADD BALANCE TO ACCOUNT RECEIVER
        accountReceiver.setBalance(accountReceiver.getBalance() + loanApplicationDTO.getAmount());
        accountReceiver.addTransaction(transaction);

        loan.get().addClientLoan(clientLoan);
        clientAuthenticated.addClientLoan(clientLoan);
        clientService.saveClient(clientAuthenticated);

        return new ResponseEntity<>("Loan approved successfully", HttpStatus.CREATED);
    }
}
