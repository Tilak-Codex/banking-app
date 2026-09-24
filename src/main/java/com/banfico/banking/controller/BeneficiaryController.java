package com.banfico.banking.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.banfico.banking.entity.Beneficiary;
import com.banfico.banking.service.BeneficiaryService;

@RestController
@RequestMapping("/api/beneficiaries")
public class BeneficiaryController {

    private final BeneficiaryService beneficiaryService;

    public BeneficiaryController(BeneficiaryService beneficiaryService) {
        this.beneficiaryService = beneficiaryService;
    }

    @PostMapping("/customers/{customerId}")
    public Beneficiary createBeneficiary(
            @PathVariable Long customerId,
            @RequestBody Beneficiary beneficiary) {

        return beneficiaryService.createBeneficiary(
                customerId, beneficiary);
    }

    @GetMapping("/{id}")
    public Beneficiary getBeneficiaryById(
            @PathVariable Long id) {

        return beneficiaryService.getBeneficiaryById(id);
    }

    @GetMapping
    public List<Beneficiary> getAllBeneficiaries() {
        return beneficiaryService.getAllBeneficiaries();
    }

    @PutMapping("/{id}")
    public Beneficiary updateBeneficiary(
            @PathVariable Long id,
            @RequestBody Beneficiary beneficiaryDetails) {

        return beneficiaryService.updateBeneficiary(
                id, beneficiaryDetails);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBeneficiary(@PathVariable Long id) {
        beneficiaryService.deleteBeneficiary(id);
    }
    @GetMapping("/customers/{customerId}")
public List<Beneficiary> getBeneficiariesByCustomerId(
        @PathVariable Long customerId) {

    return beneficiaryService.getBeneficiariesByCustomerId(customerId);
}
}