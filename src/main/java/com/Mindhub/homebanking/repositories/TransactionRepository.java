package com.Mindhub.homebanking.repositories;

import com.Mindhub.homebanking.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Date;
import java.util.List;

@RepositoryRestResource
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountId(Long accoundId);
    @Query(value = "SELECT * FROM transactions WHERE accountId='3'")
    List<Transaction> findAllTransactions();

}
