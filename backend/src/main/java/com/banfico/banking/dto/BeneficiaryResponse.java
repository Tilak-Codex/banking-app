package com.banfico.banking.dto;

public class BeneficiaryResponse {

    private Long id;
    private String name;
    private String accountNumber;
    private String bankCode;

    public BeneficiaryResponse() {
    }

    public BeneficiaryResponse(
            Long id,
            String name,
            String accountNumber,
            String bankCode) {

        this.id = id;
        this.name = name;
        this.accountNumber = accountNumber;
        this.bankCode = bankCode;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getBankCode() {
        return bankCode;
    }
}