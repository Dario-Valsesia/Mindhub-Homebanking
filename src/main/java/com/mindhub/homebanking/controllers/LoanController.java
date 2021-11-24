package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class LoanController {
    @Autowired
    LoanRepository loanRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ClientLoanRepository clientLoanRepository;
    @Autowired
    TransactionRepository transactionRepository;

    @GetMapping("/loans")
    public List<LoanDTO>  getLoans(){
           return loanRepository.findAll().stream().map(loan -> new LoanDTO(loan)).collect(Collectors.toList());
    }

    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> addLoan(@RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication){

            if(loanApplicationDTO.getName().isEmpty()||loanApplicationDTO.getNumberAccount().isEmpty()){
                return new ResponseEntity<>("Missing data",HttpStatus.FORBIDDEN);
            }
            if(loanApplicationDTO.getAmount()<=0){
                return  new ResponseEntity<>("The amount must be greater than 0", HttpStatus.FORBIDDEN);
            }
            if (loanApplicationDTO.getPayments()<=0){
                return  new ResponseEntity<>("Cannot be in 0 installments", HttpStatus.FORBIDDEN);
            }
            Loan loan = loanRepository.findByName(loanApplicationDTO.getName());
            if(loan == null){
                return  new ResponseEntity<>("The loan does not exist", HttpStatus.FORBIDDEN);
            }
            if(loanApplicationDTO.getAmount()>loan.getMaxAmount()){
                return  new ResponseEntity<>("The amount requested exceeds the maximum amount of the loan", HttpStatus.FORBIDDEN);
            }
            if (loan.getPayments().stream().filter(payment->payment.equals(loanApplicationDTO.getPayments())).collect(Collectors.toList()).size()==0){
                return  new ResponseEntity<>("Amount of installments not available", HttpStatus.FORBIDDEN);
            }
            if(accountRepository.findByNumber(loanApplicationDTO.getNumberAccount())==null){
                return  new ResponseEntity<>("Destination account does not exist", HttpStatus.FORBIDDEN);
            }
            Client client = clientRepository.findByEmail(authentication.getName());
            if(client.getAccounts().stream().filter(account -> account.getNumber().equals(loanApplicationDTO.getNumberAccount())).collect(Collectors.toList()).size()==0){
                return  new ResponseEntity<>("The target account does not belong to the user", HttpStatus.FORBIDDEN);
            }

            //Creo el prestamo del cliente le sumo el 20% y lo guardo
            ClientLoan clientLoan = new ClientLoan(loanApplicationDTO.getAmount(),loanApplicationDTO.getPayments(),client,loan);
            clientLoan.setAmount(clientLoan.getAmount()*1.20);
            clientLoanRepository.save(clientLoan);

            //Creo la transaccion y la guardo
            Transaction transaction = new Transaction(loanApplicationDTO.getAmount(),loanApplicationDTO.getName()+" loan approved", LocalDateTime.now(), TransactionType.CREDIT);
            transactionRepository.save(transaction);

            //Le asigno la transaccion a la cuenta de destino, le agrego el balance y la guardo
            Account account =  accountRepository.findByNumber(loanApplicationDTO.getNumberAccount());
            account.addTransactions(transaction);
            account.setBalance(account.getBalance()+transaction.getAmount());
            accountRepository.save(account);

            return  new ResponseEntity<>("Create", HttpStatus.CREATED);





    }

}
