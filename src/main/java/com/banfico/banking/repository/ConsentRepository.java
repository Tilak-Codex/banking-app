package com.banfico.banking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banfico.banking.entity.Consent;

public interface ConsentRepository extends JpaRepository<Consent, Long> {

    List<Consent> findByCustomerId(Long customerId);
}