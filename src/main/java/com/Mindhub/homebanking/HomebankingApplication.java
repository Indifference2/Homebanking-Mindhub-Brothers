package com.Mindhub.homebanking;

import com.Mindhub.homebanking.models.*;
import com.Mindhub.homebanking.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

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
	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository,
									  AccountRepository accountRepository,
									  TransactionRepository transactionRepository,
									  LoanRepository loanRepository,
									  ClientLoanRepository clientLoanRepository) {
		return (args) -> {
			// save a couple of customers
			Client cliente1 = new Client("Melba", "Morel", "melba@mindhub.com");
			clientRepository.save(cliente1);

			Account account1 = new Account("VIN001", 5000, LocalDateTime.now());
			Account account2 = new Account("VIN002", 7500, LocalDateTime.now().plusDays(1));
			cliente1.addAccount(account1);
			cliente1.addAccount(account2);

			Transaction transaction1 = new Transaction(TransactionType.CREDIT, 3250.50, "Ebay", LocalDateTime.now());
			Transaction transaction2 = new Transaction(TransactionType.DEBIT, 50.5, "Amazon", LocalDateTime.now().plusDays(2));
			Transaction transaction3 = new Transaction(TransactionType.DEBIT, 550.5, "Amazon", LocalDateTime.now().plusDays(2));
			Transaction transaction4 = new Transaction(TransactionType.CREDIT, 3000.10, "Google", LocalDateTime.now().plusDays(2));
			Transaction transaction5 = new Transaction(TransactionType.DEBIT, 660.20, "Alibaba", LocalDateTime.now().plusDays(3));

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

			Loan loan1 = new Loan("Hipotecario", 500000.00, List.of(12, 24, 36, 48, 60));
			Loan loan2 = new Loan("Personal", 100000.00, List.of(6, 12, 24));
			Loan loan3 = new Loan("Automotriz", 300000.00, List.of(6, 12, 24, 36));

			loanRepository.save(loan1);
			loanRepository.save(loan2);
			loanRepository.save(loan3);

			ClientLoan clientLoan1 = new ClientLoan("Hipotecario", 400000.00, 60);
			ClientLoan clientLoan2 = new ClientLoan("Personal", 50000.00, 12);
			loan1.addClientLoan(clientLoan1);
			loan2.addClientLoan(clientLoan2);

			cliente1.addClientLoan(clientLoan1);
			cliente1.addClientLoan(clientLoan2);

			clientLoanRepository.save(clientLoan1);
			clientLoanRepository.save(clientLoan2);

			loanRepository.save(loan1);
			loanRepository.save(loan2);

			clientRepository.save(cliente1);

		};
	}
}
