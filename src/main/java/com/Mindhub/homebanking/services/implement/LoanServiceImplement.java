package com.Mindhub.homebanking.services.implement;

import com.Mindhub.homebanking.dtos.LoanDTO;
import com.Mindhub.homebanking.models.Loan;
import com.Mindhub.homebanking.repositories.LoanRepository;
import com.Mindhub.homebanking.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
public class LoanServiceImplement implements LoanService {
    @Autowired
    private LoanRepository loanRepository;
    @Override
    public Optional<Loan> findById(long id) {
        return loanRepository.findById(id);
    }
    @Override
    public List<LoanDTO> getLoans() {
        return loanRepository.findAll()
                .stream()
                .map(loan -> new LoanDTO(loan))
                .collect(toList());
    }
}
