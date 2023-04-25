package com.Mindhub.homebanking.controllers;

import com.Mindhub.homebanking.dtos.ClientDTO;
import com.Mindhub.homebanking.models.Account;
import com.Mindhub.homebanking.models.Client;
import com.Mindhub.homebanking.repositories.AccountRepository;
import com.Mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class ClientController {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    public String randomNumber(){
        String randomNumber;
        do {
            int number = (int) (Math.random() * 999999 + 100000);
            randomNumber = String.valueOf(number);
        } while (accountRepository.findByNumber(randomNumber) != null);
        return randomNumber;
    }

    @RequestMapping("/clients")
    public List<ClientDTO> getClients(){
        return clientRepository.findAll()
                .stream()
                .map(client -> new ClientDTO(client))
                .collect(toList());
    }
    @RequestMapping("/clients/current")
    public ClientDTO getClient(Authentication authentication){
        return new ClientDTO(clientRepository.findByEmail(authentication.getName()));
    }
    @RequestMapping(path = "/clients", method = RequestMethod.POST)

    public ResponseEntity<Object> register(
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password) {

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if (clientRepository.findByEmail(email) !=  null) {
            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);
        }
        Client newClient = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        clientRepository.save(newClient);
        String accountNumber = randomNumber();
        Account newAccount = new Account("VIN-" + accountNumber, 0, LocalDateTime.now());
        newClient.addAccount(newAccount);
        accountRepository.save(newAccount);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
