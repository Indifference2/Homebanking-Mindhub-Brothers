package com.Mindhub.homebanking.services;

import com.Mindhub.homebanking.dtos.AccountDTO;
import com.Mindhub.homebanking.models.Account;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    Account findByNumber(String number);
    Account findById(long id);
    Optional<AccountDTO> getAccountsById(long id);
    void saveAccount(Account account);
    List<AccountDTO> getAccount();
    List<AccountDTO> getAccountsAuthenticated(Authentication authentication);
    String randomNumberAccount();
}
