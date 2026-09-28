package com.banfico.banking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banfico.banking.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BankAccountRepository
        extends JpaRepository<BankAccount, Long>,JpaSpecificationExecutor<BankAccount> {

    boolean existsByAccountNumber(String accountNumber);

    boolean existsByAccountNumberAndIdNot(
            String accountNumber,
            Long id);
}