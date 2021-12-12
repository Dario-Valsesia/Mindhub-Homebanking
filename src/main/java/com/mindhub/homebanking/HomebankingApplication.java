package com.mindhub.homebanking;


import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@SpringBootApplication
public class HomebankingApplication {
	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);

	}
	@Bean
	public CommandLineRunner initData(ClientRepository repositoryClient, AccountRepository repositoryAccount, TransactionRepository repositoryTransaction, LoanRepository repositoryLoan, ClientLoanRepository clientLoanRepository, CardRepository cardRepository){
		return (args) -> {
			// save a couple of customers

			//CREO LOS CLIENTES
			Client clienteMelba = new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("melba"));
			Client clienteDario = new Client("Dario", "Valsesia","dariovalsesia14@gmail.com",  passwordEncoder.encode("dario"));
			repositoryClient.save(clienteMelba);
			repositoryClient.save(clienteDario);

			//CREO LAS CUENTAS
			Account  VIN001 =  new Account("14", LocalDateTime.now(),10000);
			Account  VIN002= new Account("28", LocalDateTime.now().plusDays(1),5000);
			Account cuentaDario1= new Account("VIN-"+ (int) ((Math.random() * (99999999 - 10000000)) + 10000000),LocalDateTime.now().plusDays(2), 25000);
			Account cuentaDario2= new Account("VIN-"+ (int) ((Math.random() * (99999999 - 10000000)) + 10000000),LocalDateTime.now(),25000);
			clienteDario.addAccount(cuentaDario1);
			clienteDario.addAccount(cuentaDario2);
			clienteMelba.addAccount(VIN001);
			clienteMelba.addAccount(VIN002);
			repositoryAccount.save(VIN001);
			repositoryAccount.save(VIN002);
			repositoryAccount.save(cuentaDario1);
			repositoryAccount.save(cuentaDario2);


			//CREO LOS CLIENTES Y LES ASIGNO SUS CUENTAS






			//CREO LAS TRANSACCIONES
			Transaction transactionVIN001 = new Transaction(-2000.0,"Car fee",LocalDateTime.now(),TransactionType.DEBIT);
			Transaction transaction2VIN001 = new Transaction(1000.0,"Party last night",LocalDateTime.now(),TransactionType.CREDIT);
			VIN001.addTransactions(transactionVIN001);
			VIN001.addTransactions(transaction2VIN001);
			Transaction transactionVIN002 = new Transaction(-3000.0,"Airplane ticket",LocalDateTime.now(),TransactionType.DEBIT);
			Transaction transaction2VIN002 = new Transaction(2000.0,"Salary work",LocalDateTime.now().plusDays(-1),TransactionType.CREDIT);
			VIN002.addTransactions(transactionVIN002);
			VIN002.addTransactions(transaction2VIN002);
			Transaction transactionDario = new Transaction(-999.9, "Car fee", LocalDateTime.now(), TransactionType.DEBIT);
			Transaction transactionDario2 = new Transaction(5000.0, "Salary work", LocalDateTime.now(), TransactionType.CREDIT);
			cuentaDario1.addTransactions(transactionDario);
			cuentaDario2.addTransactions(transactionDario2);
			repositoryTransaction.save(transactionVIN001);
			repositoryTransaction.save(transaction2VIN001);
			repositoryTransaction.save(transactionVIN002);
			repositoryTransaction.save(transaction2VIN002);
			repositoryTransaction.save(transactionDario);
			repositoryTransaction.save(transactionDario2);

			//CREO LOS PRESTAMOS
			List<Integer> payments = Arrays.asList(12,24,36,48,60);
			List<Integer> payments2 = Arrays.asList(6,12,24);
			List<Integer> payments3 = Arrays.asList(6,12,24,36);
			Loan hipotecario = new Loan("Mortgage",500000,payments);
			Loan personal = new Loan("Personal",100000,payments2);
			Loan automotriz = new Loan("Automotive",300000,payments3);
			repositoryLoan.save(hipotecario);
			repositoryLoan.save(personal);
			repositoryLoan.save(automotriz);

			//Creo entidades de ClientLoad para Melba
			ClientLoan clientLoan = new ClientLoan(400000,hipotecario.getPayments().get(4),clienteMelba, hipotecario);
			ClientLoan clientLoan2 = new ClientLoan(50000,personal.getPayments().get(1),clienteMelba, personal);
			//Creo entidades de ClientLoad para Dario
			ClientLoan clientLoan3 = new ClientLoan(100000,personal.getPayments().get(2),clienteDario,personal);
			ClientLoan clientLoan4 = new ClientLoan(200000,automotriz.getPayments().get(3),clienteDario,automotriz);
			clientLoanRepository.save(clientLoan);
			clientLoanRepository.save(clientLoan2);
			clientLoanRepository.save(clientLoan3);
			clientLoanRepository.save(clientLoan4);
			//Agrego ClientLoan al cliente
			clienteMelba.addClientLoan(clientLoan);
			clienteMelba.addClientLoan(clientLoan2);
			clienteDario.addClientLoan(clientLoan3);
			clienteDario.addClientLoan(clientLoan4);

			//Agrego ClientLoan al prestamo
			hipotecario.addClientLoan(clientLoan);
			personal.addClientLoan(clientLoan2);
			personal.addClientLoan(clientLoan3);
			automotriz.addClientLoan(clientLoan4);

			//Creo las tarjetas
			Card cardMelbaGold = new Card("3325-6745-7876-4445",123,LocalDateTime.now(),LocalDateTime.now().plusYears(5), clienteMelba,CardType.DEBIT,CardColor.GOLD);
			Card cardMelbaTitanium = new Card("0214-4645-5046-9090",456, LocalDateTime.now(), LocalDateTime.now().plusYears(5), clienteMelba, CardType.CREDIT,CardColor.TITANIUM);
			//Card cardMelbaSilver = new Card("1414-9574-4768-2873",714, LocalDateTime.now(), LocalDateTime.now().plusYears(5), clienteMelba, CardType.CREDIT,CardColor.SILVER);
			Card cardDarioSilver = new Card("0000-1111-2222-3333",414,LocalDateTime.now(), LocalDateTime.now().plusYears(4), clienteDario, CardType.CREDIT, CardColor.SILVER);
			cardRepository.save(cardMelbaGold);
			cardRepository.save(cardMelbaTitanium);
			cardRepository.save(cardDarioSilver);




		};
	}

}
