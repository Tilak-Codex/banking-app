package com.banfico.banking.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.banfico.banking.dto.ConsentRequest;
import com.banfico.banking.dto.ConsentResponse;
import com.banfico.banking.service.ConsentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/consents")
public class ConsentController {

    private final ConsentService consentService;

    public ConsentController(ConsentService consentService) {
        this.consentService = consentService;
    }

    @PostMapping("/customers/{customerId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ConsentResponse createConsent(
            @PathVariable Long customerId,
            @Valid @RequestBody ConsentRequest request) {

        return consentService.createConsent(customerId, request);
    }

    @GetMapping("/{id}")
    public ConsentResponse getConsentById(
            @PathVariable Long id) {

        return consentService.getConsentById(id);
    }

    @GetMapping
    public List<ConsentResponse> getAllConsents() {

        return consentService.getAllConsents();
    }

    @GetMapping("/customers/{customerId}")
    public List<ConsentResponse> getConsentsByCustomerId(
            @PathVariable Long customerId) {

        return consentService.getConsentsByCustomerId(customerId);
    }

    @PostMapping("/{id}/approve")
    public ConsentResponse approveConsent(
            @PathVariable Long id) {

        return consentService.approveConsent(id);
    }

    @PostMapping("/{id}/reject")
    public ConsentResponse rejectConsent(
            @PathVariable Long id) {

        return consentService.rejectConsent(id);
    }
    @PutMapping("/{id}")
public ConsentResponse updateConsent(
        @PathVariable Long id,
        @Valid @RequestBody ConsentRequest request) {

    return consentService.updateConsent(id, request);
}
}