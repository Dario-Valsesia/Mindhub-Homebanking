package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.service.impl.PDFGeneratorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class TransactionController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private PDFGeneratorServiceImpl pdfGeneratorService;

    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> transaction(@RequestParam Double amount, @RequestParam String numberAccount, @RequestParam  String numberAccountDestiny,@RequestParam String description, Authentication authentication){
            Account account =  accountRepository.findByNumber(numberAccount);
            Account account1 =  accountRepository.findByNumber(numberAccountDestiny);
            if(numberAccount.isEmpty()||numberAccountDestiny.isEmpty()||description.isEmpty()||amount.toString().isEmpty()){
                return new ResponseEntity<>("Missing data",HttpStatus.FORBIDDEN);
            }
            if(amount<=0){
                return  new ResponseEntity<>("The amount must be greater than 0",HttpStatus.FORBIDDEN);
            }
            if(numberAccount.equals(numberAccountDestiny)){
                return new ResponseEntity<>( "Same account",HttpStatus.FORBIDDEN);
            }
            if(account==null){
                return new ResponseEntity<>("Account origin null",HttpStatus.FORBIDDEN);
            }
            if (account1==null){
                return new ResponseEntity<>("Account destiny null",HttpStatus.FORBIDDEN);
            }
           if(clientRepository.findByEmail(authentication.getName()).getAccounts().stream().filter(accoun -> accoun.getNumber().equals(numberAccount)).collect(Collectors.toList()).size()==0){
               return new ResponseEntity<>("The source account does not belong to the customer",HttpStatus.FORBIDDEN);
           }
           if(account.getBalance() < amount){
               return new ResponseEntity<>("Amount not available",HttpStatus.FORBIDDEN);
           }
            Transaction transactionOrigin = new Transaction(-amount,description, LocalDateTime.now(),TransactionType.DEBIT);
            Transaction transactionDestiny = new Transaction(amount,description, LocalDateTime.now(),TransactionType.CREDIT);
            transactionRepository.save(transactionOrigin);
            transactionRepository.save(transactionDestiny);
            account.addTransactions(transactionOrigin);
            account1.addTransactions(transactionDestiny);
            account.setBalance(account.getBalance()-amount);
            account1.setBalance(account1.getBalance()+amount);
            accountRepository.save(account);
            accountRepository.save(account1);


            return new ResponseEntity<>("created",HttpStatus.CREATED);
    }

    @PostMapping("/pdf/generate")
    public void generatePDF(HttpServletResponse response,@RequestParam String accountOrigin, @RequestParam String accountDestiny,@RequestParam Double amount,@RequestParam String description,Authentication authentication) throws IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue="attachment; filename="+currentDateTime+" "+clientRepository.findByEmail(authentication.getName()).getLastName()+  ".pdf";
        response.setHeader(headerKey,headerValue);
        Account account =  accountRepository.findByNumber(accountDestiny);
        this.pdfGeneratorService.export(response,accountOrigin,accountDestiny,amount,description,account);
    }
}
