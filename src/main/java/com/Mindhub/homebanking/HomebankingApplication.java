package com.Mindhub.homebanking;

import com.Mindhub.homebanking.models.Account;
import com.Mindhub.homebanking.models.Client;
import com.Mindhub.homebanking.repositories.ClientRepository;
import com.Mindhub.homebanking.repositories.AccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}
	@Bean
	public CommandLineRunner initData(ClientRepository repository, AccountRepository accountRepository) {
		return (args) -> {
			// save a couple of customers
			Client cliente1 = new Client("Melba", "Morel", "melba@mindhub.com");
			repository.save(cliente1);

			Account account1 = new Account("VIN001", 5000, LocalDateTime.now());
			Account account2 = new Account("VIN002", 7500, LocalDateTime.now().plusDays(1));

			cliente1.addAccount(account1);
			cliente1.addAccount(account2);
			accountRepository.save(account1);
			accountRepository.save(account2);


		};
	}
}
