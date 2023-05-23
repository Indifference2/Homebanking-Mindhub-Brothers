package com.Mindhub.homebanking.services;

import com.Mindhub.homebanking.dtos.LoanDTO;
import com.Mindhub.homebanking.models.Loan;

import java.util.List;
import java.util.Optional;

public interface LoanService {
    Loan findById(long id);
    List<LoanDTO> getLoans();

    void saveLoan(Loan loan);
}
