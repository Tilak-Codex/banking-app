package com.banfico.banking.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.banfico.banking.dto.TransactionRequest;
import com.banfico.banking.dto.TransactionResponse;
import com.banfico.banking.service.TransactionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponse createTransaction(
            @Valid @RequestBody TransactionRequest request) {

        return transactionService.createTransaction(request);
    }

    @GetMapping("/{id}")
    public TransactionResponse getTransactionById(
            @PathVariable Long id) {

        return transactionService.getTransactionById(id);
    }

    @GetMapping
    public List<TransactionResponse> getAllTransactions() {

        return transactionService.getAllTransactions();
    }

    @PostMapping("/{id}/reverse")
    public TransactionResponse reverseTransaction(
            @PathVariable Long id) {

        return transactionService.reverseTransaction(id);
    }
}