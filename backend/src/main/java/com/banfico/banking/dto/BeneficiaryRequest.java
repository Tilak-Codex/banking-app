package com.banfico.banking.dto;

import jakarta.validation.constraints.NotBlank;

public class BeneficiaryRequest {

    @NotBlank(message = "Beneficiary name is required")
    private String name;

    @NotBlank(message = "Beneficiary account number is required")
    private String accountNumber;

    @NotBlank(message = "Bank code is required")
    private String bankCode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
}