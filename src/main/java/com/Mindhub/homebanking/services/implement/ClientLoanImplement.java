package com.Mindhub.homebanking.services.implement;

import com.Mindhub.homebanking.models.Client;
import com.Mindhub.homebanking.models.ClientLoan;
import com.Mindhub.homebanking.models.Loan;
import com.Mindhub.homebanking.repositories.ClientLoanRepository;
import com.Mindhub.homebanking.services.ClientLoanService;
import com.Mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

import static java.util.stream.Collectors.toSet;
@Service

public class ClientLoanImplement implements ClientLoanService {
    @Autowired
    private ClientService clientService;
    @Autowired
    private ClientLoanRepository clientLoanRepository;
    @Override
    public Set<ClientLoan> getClientLoansAuthenticated(Client client, Loan loan) {
        return client.getClientLoans()
                .stream()
                .filter(clientLoan -> clientLoan.getLoan()
                        .getName()
                        .equalsIgnoreCase(loan.getName()))
                .collect(toSet());
    }

    @Override
    public void saveClientLoan(ClientLoan clientLoan) {
        clientLoanRepository.save(clientLoan);
    }
}
