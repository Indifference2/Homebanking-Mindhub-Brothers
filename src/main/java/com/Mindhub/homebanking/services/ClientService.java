package com.Mindhub.homebanking.services;

import com.Mindhub.homebanking.dtos.ClientDTO;
import com.Mindhub.homebanking.models.Client;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ClientService {
    List<ClientDTO> getClients();
    ClientDTO getCurrentClient(Authentication authentication);
    Client findByEmail(String email);
    void saveClient(Client client);
}
