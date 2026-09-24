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

import com.banfico.banking.entity.BankAccount;
import com.banfico.banking.entity.Customer;
import com.banfico.banking.entity.Transaction;
import com.banfico.banking.service.BankAccountService;
import com.banfico.banking.service.TransactionService;

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
    public BankAccount createBankAccount(@RequestBody BankAccount bankAccount) {
        return bankAccountService.createBankAccount(bankAccount);
    }

    @GetMapping("/{id}")
    public BankAccount getBankAccountById(@PathVariable Long id) {
        return bankAccountService.getBankAccountById(id);
    }

    @GetMapping
    public List<BankAccount> getAllBankAccounts() {
        return bankAccountService.getAllBankAccounts();
    }

    @PutMapping("/{id}")
    public BankAccount updateBankAccount(
            @PathVariable Long id,
            @RequestBody BankAccount accountDetails) {
        return bankAccountService.updateBankAccount(id, accountDetails);
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