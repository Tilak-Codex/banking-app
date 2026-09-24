package com.banfico.banking.dto;

import java.math.BigDecimal;

public class BankAccountResponse {

    private Long id;
    private String accountNumber;
    private BigDecimal balance;
    private String accountType;

    public BankAccountResponse() {
    }

    public BankAccountResponse(
            Long id,
            String accountNumber,
            BigDecimal balance,
            String accountType) {

        this.id = id;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.accountType = accountType;
    }

    public Long getId() {
        return id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getAccountType() {
        return accountType;
    }
}