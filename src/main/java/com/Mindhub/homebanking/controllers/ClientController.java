package com.Mindhub.homebanking.controllers;

import com.Mindhub.homebanking.dtos.ClientDTO;
import com.Mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Optional;


import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class ClientController {
    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping("/clients")
    public List<ClientDTO> getClients(){
        return clientRepository.findAll()
                .stream()
                .map(client -> new ClientDTO(client))
                .collect(toList());
    }
    @RequestMapping("/clients/{id}")
    public Optional<ClientDTO> getClient(@PathVariable Long id){
        return clientRepository.findById(id).map(client -> new ClientDTO(client));
    }
}
