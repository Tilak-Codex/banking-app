package com.banfico.banking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banfico.banking.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}