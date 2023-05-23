package com.Mindhub.homebanking.services.implement;

import com.Mindhub.homebanking.dtos.CardDTO;
import com.Mindhub.homebanking.models.Card;
import com.Mindhub.homebanking.repositories.CardRepository;
import com.Mindhub.homebanking.services.CardService;
import com.Mindhub.homebanking.services.ClientService;
import com.Mindhub.homebanking.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class CardServiceImplement implements CardService {
    @Autowired
    private ClientService clientService;
    @Autowired
    private CardRepository cardRepository;

    @Override
    public Card findByNumber(String number) {
        return cardRepository.findByNumber(number);
    }
    @Override
    public Card findByCvv(int cvv) {
        return cardRepository.findByCvv(cvv);
    }
    @Override
    public List<CardDTO> getCardsActive(Authentication authentication) {
        return clientService.findByEmail(authentication.getName()).getCards()
                .stream()
                .filter(card -> card.isActive())
                .map(card -> new CardDTO(card))
                .collect(toList());
    }
    @Override
    public void saveCard(Card card) {
        cardRepository.save(card);
    }

    @Override
    public String randomNumberCard() {
        String randomNumberCard;
        do{
            String firstNumber = String.valueOf(Utils.randomNumber(1000, 9999));
            String secondNumber = String.valueOf(Utils.randomNumber(1000, 9999));
            String thirdNumber = String.valueOf(Utils.randomNumber(1000, 9999));
            String fourthNumber = String.valueOf(Utils.randomNumber(1000, 9999));
            randomNumberCard = firstNumber + "-" + secondNumber + "-" + thirdNumber + "-" + fourthNumber;
        }while(findByNumber(randomNumberCard)!= null);
        return randomNumberCard;
    }

    @Override
    public int randomCvvCard() {
        int randomNumberCvv;
        do{
            randomNumberCvv = Utils.randomNumber(999, 100);
        }while(findByCvv(randomNumberCvv) != null);
        return randomNumberCvv;
    }

}
