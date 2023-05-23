package com.Mindhub.homebanking.controllers;

import com.Mindhub.homebanking.dtos.LoanApplicationDTO;
import com.Mindhub.homebanking.dtos.LoanCreationDTO;
import com.Mindhub.homebanking.dtos.LoanDTO;
import com.Mindhub.homebanking.models.*;
import com.Mindhub.homebanking.services.*;
import com.Mindhub.homebanking.utils.Utils;
import com.Mindhub.homebanking.utils.UtilsLoan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

    @GetMapping("/loans")
    public List<LoanDTO> getLoans(){
        return loanService.getLoans();
    }
    @PostMapping("/manager/loans")
    public ResponseEntity<Object> createLoan(@RequestBody LoanCreationDTO loanCreationDTO, Authentication authentication){
        if(loanCreationDTO.getName().isBlank()){
            return new ResponseEntity<>("Loan name can't be on blank", HttpStatus.FORBIDDEN);
        }
        if (loanService.getLoans().stream().anyMatch(loanDTO -> loanDTO.getName().equals(loanCreationDTO.getName()))){
            return new ResponseEntity<>("There is a loan name it that", HttpStatus.FORBIDDEN);
        }
        if(loanCreationDTO.getPayments().isEmpty()){
            return new ResponseEntity<>("Payments can't be empty", HttpStatus.FORBIDDEN);
        }
        if(loanCreationDTO.getInterest() == 0 ){
            return new ResponseEntity<>("Interest can't be zero",HttpStatus.FORBIDDEN);
        }
        if(loanCreationDTO.getInterest() < 0 ){
            return new ResponseEntity<>("Interest can't be negative", HttpStatus.FORBIDDEN);
        }
        if(loanCreationDTO.getMaxAmount() == 0){
            return new ResponseEntity<>("Max amount can't be zero", HttpStatus.FORBIDDEN);
        }
        if(loanCreationDTO.getMaxAmount() < 0){
            return new ResponseEntity<>("Max amount can't be zero", HttpStatus.FORBIDDEN);
        }

        List<Integer> paymentsOrder = UtilsLoan.paymentsOrder(loanCreationDTO.getPayments());

        Loan newLoan = new Loan(Utils.capitalize(loanCreationDTO.getName()), loanCreationDTO.getMaxAmount(), paymentsOrder, loanCreationDTO.getInterest());
        loanService.saveLoan(newLoan);
        return new ResponseEntity<>("Loan created successfully", HttpStatus.CREATED);
    }
    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> AskLoan (@RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication) {
        Client clientAuthenticated = clientService.findByEmail(authentication.getName());
        Loan loanRequested = loanService.findById(loanApplicationDTO.getId());
        Account accountReceiver = accountService.findByNumber(loanApplicationDTO.getAccountNumber().toUpperCase());

        Set<ClientLoan> clientLoanSet;

        if (loanRequested == null) {
            return new ResponseEntity<>("Loan doesn't exist", HttpStatus.FORBIDDEN);
        } else {
            clientLoanSet = clientLoanService.getClientLoansAuthenticated(clientAuthenticated, loanRequested);
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
        if (loanApplicationDTO.getAmount() > loanRequested.getMaxAmount()) {
            return new ResponseEntity<>("Amount can't be greater than max amount permitted", HttpStatus.FORBIDDEN);
        }
        if (!loanRequested.getPayments().contains(loanApplicationDTO.getPayments())) {
            return new ResponseEntity<>("That payment isn't permitted", HttpStatus.FORBIDDEN);
        }
        if (!clientAuthenticated.getAccounts().contains(accountReceiver)) {
            return new ResponseEntity<>("The Account doesn't belong you", HttpStatus.FORBIDDEN);
        }
        if (clientLoanSet.size() > 0) {
            return new ResponseEntity<>("You already have this loan", HttpStatus.FORBIDDEN);
        }

        ClientLoan clientLoan = new ClientLoan(UtilsLoan.totalInterest(loanApplicationDTO.getAmount(), loanRequested.getInterest(), loanApplicationDTO.getPayments()), loanApplicationDTO.getPayments());
        clientLoanService.saveClientLoan(clientLoan);

        Transaction transaction = new Transaction(TransactionType.CREDIT, loanApplicationDTO.getAmount(), loanRequested.getName() + " loan approved", LocalDateTime.now(), accountReceiver.getBalance() + loanApplicationDTO.getAmount(), true);
        transactionService.saveTransaction(transaction);

        //ADD BALANCE TO ACCOUNT RECEIVER
        accountReceiver.setBalance(accountReceiver.getBalance() + loanApplicationDTO.getAmount());
        accountReceiver.addTransaction(transaction);

        loanRequested.addClientLoan(clientLoan);
        clientAuthenticated.addClientLoan(clientLoan);
        clientService.saveClient(clientAuthenticated);

        return new ResponseEntity<>("Loan approved successfully", HttpStatus.CREATED);
    }
}
