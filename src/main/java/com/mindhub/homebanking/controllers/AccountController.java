package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountRepository repositoryAccount;
    @Autowired
    private ClientRepository repositoryClient;
    @GetMapping("/accounts")
    public List<AccountDTO> getAccounts(){
        return repositoryAccount.findAll().stream().map(account -> new AccountDTO(account)).collect(Collectors.toList());
    }
    @GetMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id, Authentication authentication){
        return repositoryAccount.findById(id).map(account -> new AccountDTO(account)).orElse(null);
    }
    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    //@RequestMapping(path = "/clients/current/accounts", method = RequestMethod.POST)
    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication){
            if(repositoryClient.findByEmail(authentication.getName()).getAccounts().size() == 3){
                return new ResponseEntity<>("You cannot have more than 3 accounts",HttpStatus.FORBIDDEN);
            }
            String number;
            do{
                 number = "VIN-"+getRandomNumber(10000000,99999999);
            }while (repositoryAccount.findByNumber(number) != null);
            Account account = new Account(number,LocalDateTime.now(),0);
            repositoryClient.findByEmail(authentication.getName()).addAccount(account);
            repositoryAccount.save(account);
            return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
