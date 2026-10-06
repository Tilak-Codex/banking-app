package com.banfico.banking.controller;

import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

import com.banfico.banking.dto.BankAccountRequest;
import com.banfico.banking.dto.BankAccountResponse;
import com.banfico.banking.dto.BankAccountUpdateRequest;
import com.banfico.banking.dto.CustomerResponse;
import com.banfico.banking.dto.TransactionResponse;
import com.banfico.banking.service.BankAccountService;
import com.banfico.banking.service.TransactionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/accounts")
public class BankAccountController {

    private final BankAccountService bankAccountService;
    private final TransactionService transactionService;

    public BankAccountController(
            BankAccountService bankAccountService,
            TransactionService transactionService) {

        this.bankAccountService = bankAccountService;
        this.transactionService = transactionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BankAccountResponse createBankAccount(
            @Valid @RequestBody BankAccountRequest request) {

        return bankAccountService.createBankAccount(request);
    }

    @GetMapping
    public Page<BankAccountResponse> getAllBankAccounts(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "5") int pageSize,
            @RequestParam(defaultValue = "id,asc") String sort,
            @RequestParam(required = false) String accountNumber) {

        String[] sortParams = sort.split(",");

        Sort.Direction direction = Sort.Direction.fromString(sortParams[1]);

        Pageable pageable = PageRequest.of(
                pageNo,
                pageSize,
                Sort.by(direction, sortParams[0]));

        return bankAccountService.getAllBankAccounts(
                pageable,
                accountNumber);
    }

    @PutMapping("/{id}")
    public BankAccountResponse updateBankAccount(
            @PathVariable Long id,
            @Valid @RequestBody BankAccountUpdateRequest request) {

        return bankAccountService.updateBankAccount(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBankAccount(@PathVariable Long id) {

        bankAccountService.deleteBankAccount(id);
    }

    @GetMapping("/{accountId}/transactions")
    public List<TransactionResponse> getTransactionsByBankAccountId(
            @PathVariable Long accountId) {

        return transactionService.getTransactionsByBankAccountId(accountId);
    }

    @GetMapping("/{accountId}/customers")
    public Set<CustomerResponse> getCustomersByBankAccountId(
            @PathVariable Long accountId) {

        return bankAccountService.getCustomersByBankAccountId(accountId);
    }

    @GetMapping("/{id}")
    public BankAccountResponse getBankAccountById(
            @PathVariable Long id) {

        return bankAccountService.getBankAccountById(id);
    }


    @GetMapping("/me")
    public ResponseEntity<List<BankAccountResponse>> getMyAccounts(
            @AuthenticationPrincipal Jwt jwt) {

        return ResponseEntity.ok(
                bankAccountService.getMyAccounts(jwt));
    }

    @GetMapping("/me/{id}")
    public ResponseEntity<BankAccountResponse> getMyBankAccountById(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt) {

        return ResponseEntity.ok(
                bankAccountService.getMyBankAccountById(id, jwt));
    }
}