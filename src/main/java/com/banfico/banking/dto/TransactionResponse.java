package com.banfico.banking.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.banfico.banking.entity.TransactionType;

public class TransactionResponse {

    private Long id;
    private BigDecimal amount;
    private TransactionType transactionType;
    private String description;
    private LocalDateTime transactionDate;
    private boolean reversed;
    private Long bankAccountId;

    public TransactionResponse() {
    }

    public TransactionResponse(
            Long id,
            BigDecimal amount,
            TransactionType transactionType,
            String description,
            LocalDateTime transactionDate,
            boolean reversed,
            Long bankAccountId) {

        this.id = id;
        this.amount = amount;
        this.transactionType = transactionType;
        this.description = description;
        this.transactionDate = transactionDate;
        this.reversed = reversed;
        this.bankAccountId = bankAccountId;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public boolean isReversed() {
        return reversed;
    }

    public Long getBankAccountId() {
        return bankAccountId;
    }
}