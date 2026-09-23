package com.banfico.banking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banfico.banking.entity.BankAccount;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

}