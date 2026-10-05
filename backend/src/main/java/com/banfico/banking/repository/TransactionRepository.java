package com.banfico.banking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banfico.banking.entity.Transaction;

public interface TransactionRepository
        extends JpaRepository<Transaction, Long> {

    List<Transaction> findByBankAccountIdOrderByTransactionDateDesc(
            Long bankAccountId);
            List<Transaction> findByBankAccount_IdAndBankAccount_Customers_KeycloakUserId(
        Long accountId,
        String keycloakUserId
);
}