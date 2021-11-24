package com.mindhub.homebanking.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

@Entity //Crea una tabla con el nombre de la Clase (la tabla que se va a mostrar en la base de datos)
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String number;
    private LocalDateTime creationDate;
    private double balance;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="accountOwner_id")
    private Client accountOwner;

    @OneToMany(mappedBy="accountTransaction", fetch=FetchType.EAGER)
    private Set<Transaction> transactions = new HashSet<>();


    public Account() {
    }

    public Account(String number, LocalDateTime date, double balance) {
        this.number = number;
        this.creationDate = date;
        this.balance = balance;
    }

    //OBTENER LISTA DE TRANSACCIONES
    public Set<Transaction> getTransactions() {
        return transactions;
    }

    //AGREGAR TRANSACCION A DETERMINADA CUENTA Y AGREGAR TRANSACCION A LA LISTA DE TRANSACCIONES
    public void addTransactions( Transaction transaction) {
        transaction.setAccountTransaction(this);
        transactions.add(transaction);
    }
    //SETTER AND GETTER

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCreationDate() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formatDateTime = creationDate.format(formatter);

        return formatDateTime;
    }
    /*public LocalDateTime getCreationDate() {
        return creationDate;
    }*/

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    //GET DUEÑO CUENTA
    @JsonIgnore
    public Client getOwner() {
        return accountOwner;
    }

    //SET DUEÑO CUENTA
    public void setOwner(Client owner) {
        this.accountOwner = owner;
    }
}
