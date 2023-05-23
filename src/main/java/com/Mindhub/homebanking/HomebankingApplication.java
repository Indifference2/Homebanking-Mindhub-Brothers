package com.Mindhub.homebanking;

import com.Mindhub.homebanking.models.*;
import com.Mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.Mindhub.homebanking.models.TransactionType.CREDIT;
import static com.Mindhub.homebanking.models.TransactionType.DEBIT;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository,
									  AccountRepository accountRepository,
									  TransactionRepository transactionRepository,
									  LoanRepository loanRepository,
									  ClientLoanRepository clientLoanRepository,
									  CardRepository cardRepository) {
		return (args) -> {
			// save a couple of customers
			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("Melba8577"));

			Client admin = new Client("Ivan", "Morel", "admin@gmail.com", passwordEncoder.encode("admin"));
			clientRepository.save(admin);
			clientRepository.save(client1);

			Account account1 = new Account("VIN-001", 10000, LocalDateTime.now(),true, AccountType.CURRENT);
			Account account2 = new Account("VIN-002", 7500, LocalDateTime.now().plusDays(1),true, AccountType.CURRENT);
			client1.addAccount(account1);
			client1.addAccount(account2);

			Transaction transaction1 = new Transaction(TransactionType.CREDIT, 3250.50, "Ebay", LocalDateTime.now(), account1.getBalance() + 3250.50,true);
			Transaction transaction2 = new Transaction(TransactionType.DEBIT, 50.5, "Amazon", LocalDateTime.now(),account1.getBalance() - 50.5,true);
			Transaction transaction3 = new Transaction(TransactionType.DEBIT, 550.5, "Amazon", LocalDateTime.now().minusDays(2),account1.getBalance() - 550.5,true);
			Transaction transaction4 = new Transaction(TransactionType.CREDIT, 3000.10, "Google", LocalDateTime.now().minusDays(4),account2.getBalance() + 3000.10,true);
			Transaction transaction5 = new Transaction(TransactionType.DEBIT, 660.20, "Alibaba", LocalDateTime.now().minusDays(5),account2.getBalance() - 660.20,true);

			accountRepository.save(account1);
			accountRepository.save(account2);

			account1.addTransaction(transaction1);
			account1.addTransaction(transaction2);
			account1.addTransaction(transaction3);
			account2.addTransaction(transaction4);
			account2.addTransaction(transaction5);

			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);
			transactionRepository.save(transaction4);
			transactionRepository.save(transaction5);

			accountRepository.save(account1);
			accountRepository.save(account2);

			Loan loan1 = new Loan("Mortgage", 500000.00, List.of(12, 24, 36, 48, 60), 2);
			Loan loan2 = new Loan("Personal", 100000.00, List.of(6, 12, 24), 10);
			Loan loan3 = new Loan("Automotive", 300000.00, List.of(6, 12, 24, 36), 4);

			loanRepository.save(loan1);
			loanRepository.save(loan2);
			loanRepository.save(loan3);

			ClientLoan clientLoan1 = new ClientLoan(400000.00, 60);
			ClientLoan clientLoan2 = new ClientLoan(50000.00, 12);
			loan1.addClientLoan(clientLoan1);
			loan2.addClientLoan(clientLoan2);

			client1.addClientLoan(clientLoan1);
			client1.addClientLoan(clientLoan2);

			clientLoanRepository.save(clientLoan1);
			clientLoanRepository.save(clientLoan2);

			loanRepository.save(loan1);
			loanRepository.save(loan2);

			clientRepository.save(client1);

			Card card1 = new Card("Melba Morel", CardType.DEBIT, CardColor.GOLD,
					"1234-5678-9012-3456" ,123, LocalDate.now(), LocalDate.now().plusYears(5),true);
			Card card2 = new Card("Melba Morel",CardType.DEBIT, CardColor.TITANIUM,
					"1234-5678-9012-4446", 456, LocalDate.now(), LocalDate.now().plusYears(5),true);
			client1.addCard(card1);
			client1.addCard(card2);
			cardRepository.save(card1);
			cardRepository.save(card2);
			clientRepository.save(client1);

		};
	}
}
