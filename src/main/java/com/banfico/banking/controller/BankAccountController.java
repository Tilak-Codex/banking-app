package com.banfico.banking.controller;

import java.util.List;
import java.util.Set;

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

import com.banfico.banking.dto.BankAccountRequest;
import com.banfico.banking.dto.BankAccountResponse;
import com.banfico.banking.entity.BankAccount;
import com.banfico.banking.entity.Customer;
import com.banfico.banking.entity.Transaction;
import com.banfico.banking.service.BankAccountService;
import com.banfico.banking.service.TransactionService;

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
    public BankAccountResponse createBankAccount(
            @Valid @RequestBody BankAccountRequest request) {

        return bankAccountService.createBankAccount(request);
    }

    @GetMapping("/{id}")
    public BankAccountResponse getBankAccountById(
            @PathVariable Long id) {

        return bankAccountService.getBankAccountById(id);
    }

    @GetMapping
    public List<BankAccountResponse> getAllBankAccounts() {

        return bankAccountService.getAllBankAccounts();
    }

    @PutMapping("/{id}")
public BankAccountResponse updateBankAccount(
        @PathVariable Long id,
        @Valid @RequestBody BankAccountRequest request) {

    return bankAccountService.updateBankAccount(id, request);
}

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBankAccount(@PathVariable Long id) {

        bankAccountService.deleteBankAccount(id);
    }

    @GetMapping("/{accountId}/transactions")
    public List<Transaction> getTransactionsByBankAccountId(
            @PathVariable Long accountId) {
        return transactionService.getTransactionsByBankAccountId(accountId);
    }

    @GetMapping("/{accountId}/customers")
    public Set<Customer> getCustomersByBankAccountId(
            @PathVariable Long accountId) {

        return bankAccountService.getCustomersByBankAccountId(accountId);
    }
}