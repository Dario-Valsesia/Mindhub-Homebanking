package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Loan;

import java.util.List;

public class LoanDTO {
    private String name;
    private double maxAmount;
    private List<Integer> payments;

    public LoanDTO() {
    }

    public LoanDTO(Loan loan) {
        this.name = loan.getName();
        this.maxAmount = loan.getMaxAmount();
        this.payments=loan.getPayments();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(double maxAmount) {
        this.maxAmount = maxAmount;
    }

    public List<Integer> getPayments() {
        return payments;
    }

    public void setPayments(List<Integer> payments) {
        this.payments = payments;
    }
}

