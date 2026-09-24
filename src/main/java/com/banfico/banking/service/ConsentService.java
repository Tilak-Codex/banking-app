package com.banfico.banking.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.banfico.banking.entity.Consent;
import com.banfico.banking.entity.ConsentStatus;
import com.banfico.banking.entity.Customer;
import com.banfico.banking.exception.ConsentNotFoundException;
import com.banfico.banking.exception.CustomerNotFoundException;
import com.banfico.banking.repository.ConsentRepository;
import com.banfico.banking.repository.CustomerRepository;

@Service
public class ConsentService {

    private final ConsentRepository consentRepository;
    private final CustomerRepository customerRepository;

    public ConsentService(
            ConsentRepository consentRepository,
            CustomerRepository customerRepository) {
        this.consentRepository = consentRepository;
        this.customerRepository = customerRepository;
    }

    public Consent createConsent(Long customerId, Consent consent) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new CustomerNotFoundException("Customer not found"));

        consent.setCustomer(customer);
        consent.setStatus(ConsentStatus.PENDING);
        consent.setCreatedAt(LocalDateTime.now());

        return consentRepository.save(consent);
    }

    public Consent getConsentById(Long id) {

        return consentRepository.findById(id)
                .orElseThrow(() ->
                        new ConsentNotFoundException("Consent not found"));
    }

    public List<Consent> getAllConsents() {
        return consentRepository.findAll();
    }

    public List<Consent> getConsentsByCustomerId(Long customerId) {

        customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new CustomerNotFoundException("Customer not found"));

        return consentRepository.findByCustomerId(customerId);
    }

    public Consent approveConsent(Long id) {

        Consent consent = consentRepository.findById(id)
                .orElseThrow(() ->
                        new ConsentNotFoundException("Consent not found"));

        if (consent.getStatus() != ConsentStatus.PENDING) {
            throw new IllegalArgumentException(
                    "Only pending consents can be approved");
        }

        consent.setStatus(ConsentStatus.APPROVED);
        consent.setUpdatedAt(LocalDateTime.now());

        return consentRepository.save(consent);
    }

    public Consent rejectConsent(Long id) {

        Consent consent = consentRepository.findById(id)
                .orElseThrow(() ->
                        new ConsentNotFoundException("Consent not found"));

        if (consent.getStatus() != ConsentStatus.PENDING) {
            throw new IllegalArgumentException(
                    "Only pending consents can be rejected");
        }

        consent.setStatus(ConsentStatus.REJECTED);
        consent.setUpdatedAt(LocalDateTime.now());

        return consentRepository.save(consent);
    }
}