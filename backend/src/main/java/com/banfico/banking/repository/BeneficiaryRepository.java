package com.banfico.banking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banfico.banking.entity.Beneficiary;

public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long> {

    List<Beneficiary> findByCustomerId(Long customerId);
}