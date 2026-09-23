package com.banfico.banking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banfico.banking.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
}
