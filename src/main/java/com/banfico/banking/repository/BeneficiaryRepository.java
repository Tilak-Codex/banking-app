package com.banfico.banking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banfico.banking.entity.Beneficiary;

public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long> {

}