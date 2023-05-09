package com.Mindhub.homebanking.services;

import com.Mindhub.homebanking.models.Client;
import com.Mindhub.homebanking.models.ClientLoan;
import com.Mindhub.homebanking.models.Loan;

import java.util.Set;
public interface ClientLoanService {
    Set<ClientLoan> getClientLoansAuthenticated(Client client, Loan loan);

    void saveClientLoan(ClientLoan clientLoan);
}
