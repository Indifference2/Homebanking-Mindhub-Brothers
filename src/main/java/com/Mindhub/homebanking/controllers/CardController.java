package com.Mindhub.homebanking.controllers;

import com.Mindhub.homebanking.dtos.CardDTO;
import com.Mindhub.homebanking.models.Card;
import com.Mindhub.homebanking.models.CardColor;
import com.Mindhub.homebanking.models.CardType;
import com.Mindhub.homebanking.models.Client;
import com.Mindhub.homebanking.repositories.CardRepository;
import com.Mindhub.homebanking.repositories.ClientRepository;
import com.Mindhub.homebanking.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
public class CardController {
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private ClientRepository clientRepository;
    @RequestMapping("/api/clients/current/cards")
    public List<CardDTO> getCards(Authentication authentication){
        return clientRepository.findByEmail(authentication.getName()).getCards()
                .stream()
                .map(card -> new CardDTO(card))
                .collect(toList());
    }
    @PostMapping("/api/clients/current/cards")
    public ResponseEntity<Object> createCard(
            @RequestParam String color, @RequestParam String type,
            Authentication authentication){
        Client clientAuthenticated = clientRepository.findByEmail(authentication.getName());
        if(color.isBlank()){
            return new ResponseEntity<>("Missing Color", HttpStatus.FORBIDDEN);
        }
        if(type.isBlank()){
            return new ResponseEntity<>("Missing Type", HttpStatus.FORBIDDEN);
        }
        if (!color.equalsIgnoreCase("GOLD") && !color.equalsIgnoreCase("TITANIUM") && !color.equalsIgnoreCase("SILVER")){
            return new ResponseEntity<>("Wrong color, the colors available are 'GOLD','TITANIUM','SILVER'",HttpStatus.FORBIDDEN);
        }
        if (!type.equalsIgnoreCase("DEBIT") && !type.equalsIgnoreCase("CREDIT")){
            return new ResponseEntity<>("Wrong type, the types available are 'CREDIT','DEBIT'", HttpStatus.FORBIDDEN);
        }
        for(Card card : clientAuthenticated.getCards()){
            if(card.getCardType().equals(CardType.valueOf(type.toUpperCase()))
                    &&
                    card.getCardColor().equals(CardColor.valueOf(color.toUpperCase()))){
                return new ResponseEntity<>("You already have a " + type + " " + color + " card" , HttpStatus.FORBIDDEN);

            }
        }
        Card newCard = new Card(
                clientAuthenticated.getFirstName() + " " + clientAuthenticated.getLastName(),
                CardType.valueOf(type.toUpperCase()),
                CardColor.valueOf(color.toUpperCase()),
                randomNumberCard(),
                randomCvvCard(),
                LocalDate.now(),
                LocalDate.now().plusYears(5));
        cardRepository.save(newCard);
        clientAuthenticated.addCard(newCard);
        clientRepository.save(clientAuthenticated);

        return new ResponseEntity<>("Card created successfully",HttpStatus.CREATED);
    }
    @Bean
    private String randomNumberCard(){
        String randomNumberCard;
        do{
            String firstNumber = String.valueOf(Utils.randomNumber(1000, 9999));
            String secondNumber = String.valueOf(Utils.randomNumber(1000, 9999));
            String thirdNumber = String.valueOf(Utils.randomNumber(1000, 9999));
            String fourthNumber = String.valueOf(Utils.randomNumber(1000, 9999));
            randomNumberCard = firstNumber + "-" + secondNumber + "-" + thirdNumber + "-" + fourthNumber;
        }while(cardRepository.findByNumber(randomNumberCard)!= null);
        return randomNumberCard;
    }
    @Bean
    private int randomCvvCard(){
        int randomNumberCvv;
        do{
            randomNumberCvv = (int) Utils.randomNumber(999, 100);
        }while(cardRepository.findByCvv(randomNumberCvv) != null);
        return randomNumberCvv;
    }
}
