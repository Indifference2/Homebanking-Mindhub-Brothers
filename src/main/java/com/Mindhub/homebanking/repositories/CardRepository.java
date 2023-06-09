package com.Mindhub.homebanking.repositories;

import com.Mindhub.homebanking.models.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface CardRepository extends JpaRepository<Card, Long>{
    Card findByCvv(int cvv);
    Card findByNumber(String number);

}
