package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dtos.PaymentApplicationDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class PaymentController {
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    @PostMapping("/payments")
    public ResponseEntity<Object> createPayment(@RequestBody PaymentApplicationDTO paymentApplicationDTO, Authentication authentication){
        if(paymentApplicationDTO.getDescription().isEmpty()||paymentApplicationDTO.getNumberAccount().isEmpty()||paymentApplicationDTO.getNumber().isEmpty()){
           return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if(paymentApplicationDTO.getAmount()<=0){
            return new ResponseEntity<>("The amount must be greater than 0", HttpStatus.FORBIDDEN);
        }

        Client client = clientRepository.findByEmail(authentication.getName());
        if(client.getCards().stream().filter(card -> card.getNumber().equals(paymentApplicationDTO.getNumber())).collect(Collectors.toList()).size()==0){
            return new ResponseEntity<>("The card does not belong to you", HttpStatus.FORBIDDEN);
        }
        Account account = accountRepository.findByNumber(paymentApplicationDTO.getNumberAccount());
        if(paymentApplicationDTO.getAmount()>account.getBalance()){
            return new ResponseEntity<>("It does not have this amount", HttpStatus.FORBIDDEN);
        }
        if(account==null){
            return new ResponseEntity<>("the account does not exist", HttpStatus.FORBIDDEN);
        }
        Card card = cardRepository.findByNumber(paymentApplicationDTO.getNumber());
        if(card.getCvv()!=paymentApplicationDTO.getCvv()){
            return new ResponseEntity<>("The cvv does not match", HttpStatus.FORBIDDEN);
        }
        String expirationDate = card.getThruDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime expiration = LocalDateTime.parse (expirationDate, formatter );
        LocalDateTime dateNow =  LocalDateTime.now();

        if(dateNow.isAfter(expiration)){
            return new ResponseEntity<>("The card is expired", HttpStatus.FORBIDDEN);
        }

        Transaction transaction = new Transaction(paymentApplicationDTO.getAmount(),paymentApplicationDTO.getDescription(), LocalDateTime.now(), TransactionType.DEBIT);
        transactionRepository.save(transaction);

        account.addTransactions(transaction);
        account.setBalance(account.getBalance()-paymentApplicationDTO.getAmount());
        accountRepository.save(account);

        return new ResponseEntity<>("payment made correctly", HttpStatus.CREATED);

    }


}
