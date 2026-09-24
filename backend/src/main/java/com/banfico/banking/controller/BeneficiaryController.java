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

import com.banfico.banking.dto.BeneficiaryRequest;
import com.banfico.banking.dto.BeneficiaryResponse;
import com.banfico.banking.service.BeneficiaryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/beneficiaries")
public class BeneficiaryController {

    private final BeneficiaryService beneficiaryService;

    public BeneficiaryController(BeneficiaryService beneficiaryService) {
        this.beneficiaryService = beneficiaryService;
    }

    @PostMapping("/customers/{customerId}")
    @ResponseStatus(HttpStatus.CREATED)
    public BeneficiaryResponse createBeneficiary(
            @PathVariable Long customerId,
            @Valid @RequestBody BeneficiaryRequest request) {

        return beneficiaryService.createBeneficiary(customerId, request);
    }

    @GetMapping("/{id}")
    public BeneficiaryResponse getBeneficiaryById(
            @PathVariable Long id) {

        return beneficiaryService.getBeneficiaryById(id);
    }

    @GetMapping
    public List<BeneficiaryResponse> getAllBeneficiaries() {

        return beneficiaryService.getAllBeneficiaries();
    }

    @GetMapping("/customers/{customerId}")
    public List<BeneficiaryResponse> getBeneficiariesByCustomerId(
            @PathVariable Long customerId) {

        return beneficiaryService.getBeneficiariesByCustomerId(customerId);
    }

    @PutMapping("/{id}")
    public BeneficiaryResponse updateBeneficiary(
            @PathVariable Long id,
            @Valid @RequestBody BeneficiaryRequest request) {

        return beneficiaryService.updateBeneficiary(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBeneficiary(@PathVariable Long id) {

        beneficiaryService.deleteBeneficiary(id);
    }
}