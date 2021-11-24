package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.utils.CardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class CardController {
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private ClientRepository clientRepository;

    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> create(@RequestParam CardColor color, @RequestParam CardType type, Authentication authentication){
        if(color.toString().isEmpty()){
            return new ResponseEntity<>("Select color", HttpStatus.FORBIDDEN);
        }
        if(type.toString().isEmpty()){
            return new ResponseEntity<>("Select type", HttpStatus.FORBIDDEN);
        }
       Client client =  clientRepository.findByEmail(authentication.getName());
       Set<Card> cards = client.getCards();
       int cont = 0;
       for(Card card : cards ){
           if(card.getCardType().equals(type)){
               cont++;
           }
       }
       if(cont==3){
            return new ResponseEntity<>("Maximum 3 cards of the same type",HttpStatus.FORBIDDEN);
       }
       int cvv= CardUtils.getRandomNumber(100,999);
        cardRepository.save(new Card(CardUtils.getRandomNumber(1000,9999)+"-"+CardUtils.getRandomNumber(1000,9999)+"-"+CardUtils.getRandomNumber(1000,9999)+"-"+CardUtils.getRandomNumber(1000,9999),cvv,LocalDateTime.now(),LocalDateTime.now().plusYears(5),client,type,color)) ;
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


}
