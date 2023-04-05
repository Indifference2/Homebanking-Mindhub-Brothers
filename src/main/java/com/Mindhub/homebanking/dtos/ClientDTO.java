package com.Mindhub.homebanking.dtos;

import com.Mindhub.homebanking.models.Client;
import java.util.Set;
import java.util.stream.Collectors;
public class ClientDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Set<AccountDTO> account;

    public ClientDTO(Client client){
        this.id = client.getId();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.email = client.getEmail();
        this.account = client.getAccounts().stream().map(account -> new AccountDTO(account)).collect(Collectors.toSet());
    }

    public Long getId() {
        return id;
    }
    public Set<AccountDTO> getAccount() {
        return account;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getEmail() {
        return email;
    }
}
