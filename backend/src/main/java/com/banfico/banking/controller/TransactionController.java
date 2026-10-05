
package com.banfico.banking.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.banfico.banking.dto.TransactionRequest;
import com.banfico.banking.dto.TransactionResponse;
import com.banfico.banking.dto.TransferRequest;
import com.banfico.banking.service.TransactionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(
            TransactionService transactionService) {

        this.transactionService = transactionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponse createTransaction(
            @Valid @RequestBody TransactionRequest request,
            @AuthenticationPrincipal Jwt jwt) {

        return transactionService.createTransaction(
                request,
                jwt);
    }

    @PostMapping("/transfer")
    public ResponseEntity<List<TransactionResponse>> transfer(
            @Valid @RequestBody TransferRequest request,
            @AuthenticationPrincipal Jwt jwt) {

        return ResponseEntity.ok(
                transactionService.transfer(
                        request,
                        jwt));
    }

    @GetMapping("/{id}")
    public TransactionResponse getTransactionById(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt) {

        return transactionService.getTransactionById(
                id,
                jwt);
    }

    @PostMapping("/{transactionId}/reverse")
    public TransactionResponse reverseTransaction(
            @PathVariable Long transactionId,
            @AuthenticationPrincipal Jwt jwt) {

        return transactionService.reverseTransaction(
                transactionId,
                jwt);
    }

    @GetMapping("/me/{accountId}")
    public ResponseEntity<List<TransactionResponse>> getMyTransactions(
            @PathVariable Long accountId,
            @AuthenticationPrincipal Jwt jwt) {

        return ResponseEntity.ok(
                transactionService.getMyTransactions(
                        accountId,
                        jwt));
    }
}
