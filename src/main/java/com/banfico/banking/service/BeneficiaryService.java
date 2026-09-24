package com.banfico.banking.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.banfico.banking.dto.BeneficiaryRequest;
import com.banfico.banking.dto.BeneficiaryResponse;
import com.banfico.banking.entity.Beneficiary;
import com.banfico.banking.entity.Customer;
import com.banfico.banking.exception.BeneficiaryNotFoundException;
import com.banfico.banking.exception.CustomerNotFoundException;
import com.banfico.banking.repository.BeneficiaryRepository;
import com.banfico.banking.repository.CustomerRepository;

@Service
public class BeneficiaryService {

    private final BeneficiaryRepository beneficiaryRepository;
    private final CustomerRepository customerRepository;

    public BeneficiaryService(
            BeneficiaryRepository beneficiaryRepository,
            CustomerRepository customerRepository) {

        this.beneficiaryRepository = beneficiaryRepository;
        this.customerRepository = customerRepository;
    }

    public BeneficiaryResponse createBeneficiary(
            Long customerId,
            BeneficiaryRequest request) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new CustomerNotFoundException("Customer not found"));

        Beneficiary beneficiary = new Beneficiary();

        beneficiary.setName(request.getName());
        beneficiary.setAccountNumber(request.getAccountNumber());
        beneficiary.setBankCode(request.getBankCode());
        beneficiary.setCustomer(customer);

        Beneficiary savedBeneficiary =
                beneficiaryRepository.save(beneficiary);

        return toResponse(savedBeneficiary);
    }

    public BeneficiaryResponse getBeneficiaryById(Long id) {

        Beneficiary beneficiary = beneficiaryRepository.findById(id)
                .orElseThrow(() ->
                        new BeneficiaryNotFoundException("Beneficiary not found"));

        return toResponse(beneficiary);
    }

    public List<BeneficiaryResponse> getAllBeneficiaries() {

        return beneficiaryRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public BeneficiaryResponse updateBeneficiary(
            Long id,
            BeneficiaryRequest request) {

        Beneficiary beneficiary = beneficiaryRepository.findById(id)
                .orElseThrow(() ->
                        new BeneficiaryNotFoundException("Beneficiary not found"));

        beneficiary.setName(request.getName());
        beneficiary.setAccountNumber(request.getAccountNumber());
        beneficiary.setBankCode(request.getBankCode());

        Beneficiary updatedBeneficiary =
                beneficiaryRepository.save(beneficiary);

        return toResponse(updatedBeneficiary);
    }

    public void deleteBeneficiary(Long id) {

        Beneficiary beneficiary = beneficiaryRepository.findById(id)
                .orElseThrow(() ->
                        new BeneficiaryNotFoundException("Beneficiary not found"));

        beneficiaryRepository.delete(beneficiary);
    }

    public List<BeneficiaryResponse> getBeneficiariesByCustomerId(
            Long customerId) {

        customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new CustomerNotFoundException("Customer not found"));

        return beneficiaryRepository.findByCustomerId(customerId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private BeneficiaryResponse toResponse(Beneficiary beneficiary) {

        return new BeneficiaryResponse(
                beneficiary.getId(),
                beneficiary.getName(),
                beneficiary.getAccountNumber(),
                beneficiary.getBankCode()
        );
    }
}