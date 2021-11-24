package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.ClientLoan;

public class ClientLoanDTO {
    private long id;
    private long idLoan;
    private String nameLoan;
    private double amount;
    private int payments;


    public ClientLoanDTO() {
    }
    public ClientLoanDTO(ClientLoan clientLoan){
        this.id = clientLoan.getId();
        this.idLoan = clientLoan.getLoan().getId();
        this.nameLoan = clientLoan.getLoan().getName();
        this.amount = clientLoan.getAmount();
        this.payments = clientLoan.getPayments();

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getPayments() {
        return payments;
    }

    public void setPayments(int payments) {
        this.payments = payments;
    }

    public long getIdLoan() {
        return idLoan;
    }

    public void setIdLoan(long idLoan) {
        this.idLoan = idLoan;
    }

    public String getNameLoan() {
        return nameLoan;
    }

    public void setNameLoan(String nameLoan) {
        this.nameLoan = nameLoan;
    }
}

