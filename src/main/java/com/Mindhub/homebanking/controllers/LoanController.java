package com.Mindhub.homebanking.controllers;

import com.Mindhub.homebanking.dtos.LoanApplicationDTO;
import com.Mindhub.homebanking.dtos.LoanDTO;
import com.Mindhub.homebanking.models.*;
import com.Mindhub.homebanking.repositories.AccountRepository;
import com.Mindhub.homebanking.repositories.ClientLoanRepository;
import com.Mindhub.homebanking.repositories.ClientRepository;
import com.Mindhub.homebanking.repositories.LoanRepository;
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


import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class LoanController {
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientLoanRepository clientLoanRepository;
    @RequestMapping("/loans")
    public List<LoanDTO> getLoans(){
        return loanRepository.findAll()
                .stream()
                .map(loan -> new LoanDTO(loan))
                .collect(toList());
    }
    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> AskLoan (@RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication){
        Client clientAuthenticated = clientRepository.findByEmail(authentication.getName());
        Loan loan = loanRepository.findById(loanApplicationDTO.getId()).orElse(null);
        Account accountReceiver = accountRepository.findByNumber(loanApplicationDTO.getAccountNumber().toUpperCase());

        if(loan == null){
            return new ResponseEntity<>("Loan doesn't exist", HttpStatus.FORBIDDEN);
        }
        if(accountReceiver == null){
            return new ResponseEntity<>("Account doesn't exist", HttpStatus.FORBIDDEN);
        }
        if(String.valueOf(loanApplicationDTO.getAmount()).isBlank()){
            return new ResponseEntity<>("Amount can't be on blank", HttpStatus.FORBIDDEN);
        }
        if(String.valueOf(loanApplicationDTO.getPayments()).isBlank()){
            return new ResponseEntity<>("Payment can't be on blank", HttpStatus.FORBIDDEN);
        }
        if(loanApplicationDTO.getAmount() < 0){
            return new ResponseEntity<>("Amount can't be negative",HttpStatus.FORBIDDEN);
        }
        if(loanApplicationDTO.getAmount() > loan.getMaxAmount()){
            return new ResponseEntity<>("Amount can't be greater than max amount permitted", HttpStatus.FORBIDDEN);
        }
        if(!loan.getPayments().contains(loanApplicationDTO.getPayments())){
            return new ResponseEntity<>("That payment isn't permitted",HttpStatus.FORBIDDEN);
        }
        if(!clientAuthenticated.getAccounts().contains(accountReceiver)){
            return new ResponseEntity<>("The Account doesn't belong you", HttpStatus.FORBIDDEN);
        }
        ClientLoan clientLoan = new ClientLoan(loanApplicationDTO.getAmount() + (loanApplicationDTO.getAmount() * 0.2), loanApplicationDTO.getPayments());
        clientLoanRepository.save(clientLoan);

        Transaction transaction = new Transaction(TransactionType.CREDIT, loanApplicationDTO.getAmount(), loan.getName() + " loan approved", LocalDateTime.now());

        //ADD BALANCE TO ACCOUNT RECEIVER
        accountReceiver.setBalance(accountReceiver.getBalance() + loanApplicationDTO.getAmount());
        accountReceiver.addTransaction(transaction);

        loan.addClientLoan(clientLoan);
        clientAuthenticated.addClientLoan(clientLoan);
        clientRepository.save(clientAuthenticated);

        return new ResponseEntity<>("Loan approved successfully",HttpStatus.CREATED);
    }
}
