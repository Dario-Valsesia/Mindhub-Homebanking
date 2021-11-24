package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.aspectj.apache.bcel.generic.InstructionConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController // hace más fácil definir un servicio web que devuelva un recurso JSON al cliente en vez de un HTML
@RequestMapping("/api")
public class ClientController {
    @Autowired
    private ClientRepository repositoryClient;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AccountRepository accountRepository;
    @GetMapping("/clients")
    public List<ClientDTO> getClients() {
        return repositoryClient.findAll().stream().map(client -> new ClientDTO(client)).collect(Collectors.toList());
    }
    /*@RequestMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){
        return repositoryClient.findById(id).map(client -> new ClientDTO(client)).orElse(null);
    }*/
    @GetMapping("/clients/current")
    public ClientDTO getClient(Authentication authentication){
        return new ClientDTO(repositoryClient.findByEmail(authentication.getName()));
    }
   // @RequestMapping(path = "/clients", method = RequestMethod.POST)
    @PostMapping("/clients")
    public ResponseEntity<Object> register(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String email, @RequestParam String password){
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if (repositoryClient.findByEmail(email) !=  null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }

        Client client = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        repositoryClient.save(client);
        Account account = new Account("VIN-"+(int) (Math.random()*100000000 +1), LocalDateTime.now(),0);
        client.addAccount(account);
        accountRepository.save(account);


        return new ResponseEntity<>(HttpStatus.CREATED);
    }



}
