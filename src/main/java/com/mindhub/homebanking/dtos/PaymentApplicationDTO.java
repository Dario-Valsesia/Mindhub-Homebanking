package com.mindhub.homebanking.dtos;

public class PaymentApplicationDTO {
    private String number;
    private int cvv;
    private Double amount;
    private String description;
    private String numberAccount;


    public PaymentApplicationDTO() {
    }

    public PaymentApplicationDTO(String number, int cvv, Double amount, String description, String numberAccount) {
        this.number = number;
        this.cvv = cvv;
        this.amount = amount;
        this.description = description;
        this.numberAccount = numberAccount;
    }

    public String getNumberAccount() {
        return numberAccount;
    }

    public void setNumberAccount(String numberAccount) {
        this.numberAccount = numberAccount;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getCvv() {
        return cvv;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
