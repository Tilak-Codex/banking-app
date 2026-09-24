package com.banfico.banking.service;

import java.util.List;

import org.springframework.stereotype.Service;

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

   

    public Beneficiary createBeneficiary(
        Long customerId,
        Beneficiary beneficiary) {

    Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new CustomerNotFoundException(
                    "Customer not found"));

    beneficiary.setCustomer(customer);

    return beneficiaryRepository.save(beneficiary);
}

    public Beneficiary getBeneficiaryById(Long id) {
        return beneficiaryRepository.findById(id)
                .orElseThrow(() -> new BeneficiaryNotFoundException(
                        "Beneficiary not found"));
    }

    public List<Beneficiary> getAllBeneficiaries() {
        return beneficiaryRepository.findAll();
    }

    public Beneficiary updateBeneficiary(
            Long id,
            Beneficiary beneficiaryDetails) {

        Beneficiary beneficiary = beneficiaryRepository.findById(id)
                .orElseThrow(() -> new BeneficiaryNotFoundException(
                        "Beneficiary not found"));

        beneficiary.setName(beneficiaryDetails.getName());
        beneficiary.setAccountNumber(beneficiaryDetails.getAccountNumber());
        beneficiary.setBankCode(beneficiaryDetails.getBankCode());

        return beneficiaryRepository.save(beneficiary);
    }

    public void deleteBeneficiary(Long id) {

        Beneficiary beneficiary = beneficiaryRepository.findById(id)
                .orElseThrow(() -> new BeneficiaryNotFoundException(
                        "Beneficiary not found"));

        beneficiaryRepository.delete(beneficiary);
    }
}