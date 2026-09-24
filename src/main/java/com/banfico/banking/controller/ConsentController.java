package com.banfico.banking.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

import com.banfico.banking.entity.Consent;
import com.banfico.banking.service.ConsentService;

@RestController
@RequestMapping("/api/consents")
public class ConsentController {

    private final ConsentService consentService;

    public ConsentController(ConsentService consentService) {
        this.consentService = consentService;
    }

    @PostMapping("/customers/{customerId}")
public Consent createConsent(
        @PathVariable Long customerId,
        @Valid @RequestBody Consent consent) {

    return consentService.createConsent(customerId, consent);
}

    @GetMapping("/{id}")
    public Consent getConsentById(@PathVariable Long id) {

        return consentService.getConsentById(id);
    }

    @GetMapping
    public List<Consent> getAllConsents() {

        return consentService.getAllConsents();
    }

    @GetMapping("/customers/{customerId}")
    public List<Consent> getConsentsByCustomerId(
            @PathVariable Long customerId) {

        return consentService.getConsentsByCustomerId(customerId);
    }

    @PostMapping("/{id}/approve")
    public Consent approveConsent(@PathVariable Long id) {

        return consentService.approveConsent(id);
    }

    @PostMapping("/{id}/reject")
    public Consent rejectConsent(@PathVariable Long id) {

        return consentService.rejectConsent(id);
    }
}