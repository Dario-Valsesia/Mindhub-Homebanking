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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Set;
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
    @CrossOrigin("*")
    @PostMapping("/pagar")
    public ResponseEntity<Object> toPay(@RequestParam String numCard,@RequestParam Integer cvv,@RequestParam String name,@RequestParam String thruDate,@RequestParam Double total ){
            if(numCard.isEmpty()||name.isEmpty()||thruDate.isEmpty()){
                return new ResponseEntity<>("Data vacia", HttpStatus.FORBIDDEN);
            }
            Card card = cardRepository.findByNumber(numCard);
            if (card==null){
                return new ResponseEntity<>("La tarjeta no existe", HttpStatus.FORBIDDEN);
            }
            String nombre = card.getClientCard().getFirstName()+" "+card.getClientCard().getLastName();
            if (!nombre.equals(name)){
                return new ResponseEntity<>("Nombre del titular incorrecto", HttpStatus.FORBIDDEN);
            }
            if (card.getCvv()!=cvv){
                return new ResponseEntity<>("El cvv es incorrecto", HttpStatus.FORBIDDEN);
            }
            String fechaTarjeta = card.getThruDate();
            String fechaAComparar = fechaTarjeta.substring(0,7);
            if (!fechaAComparar.equals(thruDate)){
                return new ResponseEntity<>("La fecha de vencimiento es incorrecta", HttpStatus.FORBIDDEN);
            }
            Client cliente = card.getClientCard();
            Account cuenta = cliente.getAccounts().iterator().next();
            if(cuenta.getBalance()<total){
                return new ResponseEntity<>("Saldo insuficiente", HttpStatus.FORBIDDEN);
            }
            Transaction transaccion = new Transaction(total,"To pay with card",LocalDateTime.now(),TransactionType.DEBIT);
            transactionRepository.save(transaccion);

            cuenta.addTransactions(transaccion);
            cuenta.setBalance(cuenta.getBalance()-total);
            accountRepository.save(cuenta);

            return new ResponseEntity<>("Pago exitoso", HttpStatus.CREATED);
    }

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
