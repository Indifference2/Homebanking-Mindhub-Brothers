package com.Mindhub.homebanking.services;

import com.Mindhub.homebanking.dtos.AccountDTO;
import com.Mindhub.homebanking.models.Account;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface AccountService {
    Account findByNumber(String number);
    Account findById(long id);
    void saveAccount(Account account);
    List<AccountDTO> getAccounts();
    List<AccountDTO> getAccountsAuthenticated(Authentication authentication);
    String randomNumberAccount();
    Boolean accountBelongToClient(Authentication authentication, Account account);
}
