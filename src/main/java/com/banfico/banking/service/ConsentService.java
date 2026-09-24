package com.banfico.banking.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.banfico.banking.dto.ConsentRequest;
import com.banfico.banking.dto.ConsentResponse;
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

    public ConsentResponse createConsent(
            Long customerId,
            ConsentRequest request) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new CustomerNotFoundException(
                                "Customer not found"));

        Consent consent = new Consent();

        consent.setConsentReference(request.getConsentReference());
        consent.setPurpose(request.getPurpose());
        consent.setCustomer(customer);
        consent.setStatus(ConsentStatus.PENDING);
        consent.setCreatedAt(LocalDateTime.now());

        Consent savedConsent = consentRepository.save(consent);

        return toResponse(savedConsent);
    }

    public ConsentResponse getConsentById(Long id) {

        Consent consent = consentRepository.findById(id)
                .orElseThrow(() ->
                        new ConsentNotFoundException(
                                "Consent not found"));

        return toResponse(consent);
    }

    public List<ConsentResponse> getAllConsents() {

        return consentRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<ConsentResponse> getConsentsByCustomerId(
            Long customerId) {

        customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new CustomerNotFoundException(
                                "Customer not found"));

        return consentRepository.findByCustomerId(customerId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ConsentResponse approveConsent(Long id) {

        Consent consent = consentRepository.findById(id)
                .orElseThrow(() ->
                        new ConsentNotFoundException(
                                "Consent not found"));

        if (consent.getStatus() != ConsentStatus.PENDING) {
            throw new IllegalArgumentException(
                    "Only pending consents can be approved");
        }

        consent.setStatus(ConsentStatus.APPROVED);
        consent.setUpdatedAt(LocalDateTime.now());

        Consent updatedConsent = consentRepository.save(consent);

        return toResponse(updatedConsent);
    }

    public ConsentResponse rejectConsent(Long id) {

        Consent consent = consentRepository.findById(id)
                .orElseThrow(() ->
                        new ConsentNotFoundException(
                                "Consent not found"));

        if (consent.getStatus() != ConsentStatus.PENDING) {
            throw new IllegalArgumentException(
                    "Only pending consents can be rejected");
        }

        consent.setStatus(ConsentStatus.REJECTED);
        consent.setUpdatedAt(LocalDateTime.now());

        Consent updatedConsent = consentRepository.save(consent);

        return toResponse(updatedConsent);
    }

    private ConsentResponse toResponse(Consent consent) {

        return new ConsentResponse(
                consent.getId(),
                consent.getConsentReference(),
                consent.getPurpose(),
                consent.getStatus(),
                consent.getCreatedAt(),
                consent.getUpdatedAt(),
                consent.getCustomer().getId()
        );
    }
}