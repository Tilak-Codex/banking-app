package com.banfico.banking.dto;

import java.time.LocalDateTime;

import com.banfico.banking.entity.ConsentStatus;

public class ConsentResponse {

    private Long id;
    private String consentReference;
    private String purpose;
    private ConsentStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long customerId;

    public ConsentResponse() {
    }

    public ConsentResponse(
            Long id,
            String consentReference,
            String purpose,
            ConsentStatus status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            Long customerId) {

        this.id = id;
        this.consentReference = consentReference;
        this.purpose = purpose;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.customerId = customerId;
    }

    public Long getId() {
        return id;
    }

    public String getConsentReference() {
        return consentReference;
    }

    public String getPurpose() {
        return purpose;
    }

    public ConsentStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Long getCustomerId() {
        return customerId;
    }
}