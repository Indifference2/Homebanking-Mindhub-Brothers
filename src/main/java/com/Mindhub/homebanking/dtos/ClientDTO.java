package com.Mindhub.homebanking.dtos;

import com.Mindhub.homebanking.models.Client;
import java.util.Set;
import static java.util.stream.Collectors.toSet;

public class ClientDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Set<AccountDTO> accounts;
    private Set<ClientLoanDTO> clientLoans;

    public ClientDTO(Client client){
        this.id = client.getId();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.email = client.getEmail();
        this.accounts = client.getAccounts().
                stream()
                .map(account -> new AccountDTO(account))
                .collect(toSet());
        this.clientLoans = client.getClientLoans()
                .stream()
                .map(clientLoan -> new ClientLoanDTO(clientLoan))
                .collect(toSet());
    }

    public Long getId() {
        return id;
    }
    public Set<AccountDTO> getAccount() {
        return accounts;
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

    public Set<ClientLoanDTO> getClientLoans() {return clientLoans;}
}
