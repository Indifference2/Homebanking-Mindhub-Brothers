package com.Mindhub.homebanking.services.implement;

import com.Mindhub.homebanking.dtos.ClientDTO;
import com.Mindhub.homebanking.models.Client;
import com.Mindhub.homebanking.repositories.ClientRepository;
import com.Mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class ClientServiceImplement implements ClientService {
    @Autowired
    ClientRepository clientRepository;
    @Override
    public List<ClientDTO> getClients() {
        return clientRepository.findAll()
                .stream()
                .map(client -> new ClientDTO(client))
                .collect(toList());
    }

    @Override
    public ClientDTO getCurrentClient(Authentication authentication) {
        return new ClientDTO(findByEmail(authentication.getName()));
    }

    @Override
    public Client findByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    @Override
    public void saveClient(Client client) {
        clientRepository.save(client);
    }

    @Override
    public GrantedAuthority getClientRol(Authentication authentication) {
        return authentication.getAuthorities().stream().collect(toList()).get(0);
    }

    @Override
    public boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ADMIN"));
    }



}
