package com.banfico.banking.dto;

import jakarta.validation.constraints.NotBlank;

public class ConsentRequest {

    @NotBlank(message = "Consent reference is required")
    private String consentReference;

    @NotBlank(message = "Consent purpose is required")
    private String purpose;

    public String getConsentReference() {
        return consentReference;
    }

    public void setConsentReference(String consentReference) {
        this.consentReference = consentReference;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
}