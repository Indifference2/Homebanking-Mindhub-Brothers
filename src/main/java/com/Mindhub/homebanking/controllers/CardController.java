package com.Mindhub.homebanking.controllers;

import com.Mindhub.homebanking.dtos.CardDTO;
import com.Mindhub.homebanking.models.Card;
import com.Mindhub.homebanking.models.CardColor;
import com.Mindhub.homebanking.models.CardType;
import com.Mindhub.homebanking.models.Client;
import com.Mindhub.homebanking.services.CardService;
import com.Mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;


@RestController
public class CardController {
    @Autowired
    private CardService cardService;
    @Autowired
    private ClientService clientService;
    @RequestMapping("/api/clients/current/cards")
    public List<CardDTO> getCards(Authentication authentication){
        return cardService.getCards(authentication);
    }
    @PostMapping("/api/clients/current/cards")
    public ResponseEntity<Object> createCard(
            @RequestParam String color, @RequestParam String type,
            Authentication authentication){
        Client clientAuthenticated = clientService.findByEmail(authentication.getName());

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
                cardService.randomNumberCard(),
                cardService.randomCvvCard(),
                LocalDate.now(),
                LocalDate.now().plusYears(5));
        cardService.saveCard(newCard);

        clientAuthenticated.addCard(newCard);
        clientService.saveClient(clientAuthenticated);

        return new ResponseEntity<>("Card created successfully",HttpStatus.CREATED);
    }

}
