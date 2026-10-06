
package com.banfico.banking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.banfico.banking.entity.Transaction;

public interface TransactionRepository
        extends JpaRepository<Transaction, Long>,
                JpaSpecificationExecutor<Transaction> {
}
