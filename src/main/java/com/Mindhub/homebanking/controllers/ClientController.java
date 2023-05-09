package com.Mindhub.homebanking.controllers;

import com.Mindhub.homebanking.dtos.ClientDTO;
import com.Mindhub.homebanking.models.Account;
import com.Mindhub.homebanking.models.Client;
import com.Mindhub.homebanking.services.AccountService;
import com.Mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ClientController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping("/clients")
    public List<ClientDTO> getClients(){
        return clientService.getClients();
    }
    @RequestMapping("/clients/current")
    public ClientDTO getClient(Authentication authentication){
        return clientService.getCurrentClient(authentication);
    }
    @RequestMapping("/accounts/clients")
    public ResponseEntity<String> getName(@RequestParam String numberAccount){
        Account accountDestiny = accountService.findByNumber(numberAccount);
        Client clientDestiny = accountDestiny.getClient();
        if (clientDestiny == null){
            return new ResponseEntity<>("Client destiny not found", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(clientDestiny.getFirstName() + " " + clientDestiny.getLastName(), HttpStatus.OK);
    }
    @PostMapping("/clients")
    public ResponseEntity<Object> register(
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password) {

        if (firstName.isBlank()) {
            return new ResponseEntity<>("First Name can't be on blank", HttpStatus.FORBIDDEN);
        }
        if(lastName.isBlank()){
            return new ResponseEntity<>("Last name can't be on blank", HttpStatus.FORBIDDEN);
        }
        if(email.isBlank()){
            return new ResponseEntity<>("Email can't be on blank", HttpStatus.FORBIDDEN);
        }
        if(password.isBlank()){
            return new ResponseEntity<>("Password can't be on blank", HttpStatus.FORBIDDEN);
        }
        if (clientService.findByEmail(email) !=  null) {
            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);
        }
        Client newClient = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        clientService.saveClient(newClient);

        Account newAccount = new Account("VIN-" + accountService.randomNumberAccount() , 0, LocalDateTime.now());
        newClient.addAccount(newAccount);
        accountService.saveAccount(newAccount);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
