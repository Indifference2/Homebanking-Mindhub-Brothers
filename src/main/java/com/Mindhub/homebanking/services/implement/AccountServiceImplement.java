package com.Mindhub.homebanking.services.implement;

import com.Mindhub.homebanking.dtos.AccountDTO;
import com.Mindhub.homebanking.models.Account;
import com.Mindhub.homebanking.repositories.AccountRepository;
import com.Mindhub.homebanking.repositories.ClientRepository;
import com.Mindhub.homebanking.services.AccountService;
import com.Mindhub.homebanking.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
public class AccountServiceImplement implements AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;

    @Override
    public Account findByNumber(String number) {
        return accountRepository.findByNumber(number);
    }

    @Override
    public Account findById(long id) {
        return accountRepository.findById(id).orElse(null);
    }

    @Override
    public Optional<AccountDTO> getAccountsById(long id) {
        return Optional.of(new AccountDTO(findById(id)));
    }

    @Override
    public void saveAccount(Account account) {
        accountRepository.save(account);
    }

    @Override
    public List<AccountDTO> getAccount() {
        return accountRepository.findAll()
                .stream()
                .map(account -> new AccountDTO(account))
                .collect(toList());
    }

    @Override
    public List<AccountDTO> getAccountsAuthenticated(Authentication authentication) {
        return clientRepository.findByEmail(
                        authentication.getName())
                .getAccounts()
                .stream()
                .map(account -> new AccountDTO(account))
                .collect(toList());
    }

    @Override
    public String randomNumberAccount() {
        String randomNumberAccount;
        do{
            randomNumberAccount = String.valueOf(Utils.randomNumber(899999, 100000));
        }while(accountRepository.findByNumber(randomNumberAccount)!= null);
        return randomNumberAccount;
    }

}
