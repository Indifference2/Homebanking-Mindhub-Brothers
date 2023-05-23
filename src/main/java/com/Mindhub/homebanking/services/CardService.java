package com.Mindhub.homebanking.services;

import com.Mindhub.homebanking.dtos.CardDTO;
import com.Mindhub.homebanking.models.Card;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface CardService {
    Card findByNumber(String number);
    Card findByCvv(int cvv);
    List<CardDTO> getCardsActive(Authentication authentication);
    void saveCard(Card card);
    String randomNumberCard();
    int randomCvvCard();
}
