package com.mindhub.homebanking.dtos;

public class LoanApplicationDTO {
        private String name;
        private Double amount;
        private int payments;
        private String numberAccount;

        public LoanApplicationDTO() {
        }

        public LoanApplicationDTO(String name,Double amount, int payments, String numberAccount) {
                this.name = name;
                this.amount = amount;
                this.payments = payments;
                this.numberAccount = numberAccount;
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
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

        public String getNumberAccount() {
                return numberAccount;
        }

        public void setNumberAccount(String numberAccount) {
                this.numberAccount = numberAccount;
        }
}

