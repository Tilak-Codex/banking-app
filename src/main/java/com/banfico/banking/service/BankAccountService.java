package com.banfico.banking.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.banfico.banking.dto.BankAccountRequest;
import com.banfico.banking.dto.BankAccountResponse;
import com.banfico.banking.entity.BankAccount;
import com.banfico.banking.entity.Customer;
import com.banfico.banking.exception.BankAccountNotFoundException;
import com.banfico.banking.repository.BankAccountRepository;
import com.banfico.banking.dto.CustomerResponse;

@Service
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;

    public BankAccountService(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    public BankAccountResponse createBankAccount(BankAccountRequest request) {

        BankAccount bankAccount = new BankAccount();

        bankAccount.setAccountNumber(request.getAccountNumber());
        bankAccount.setBalance(request.getBalance());
        bankAccount.setAccountType(request.getAccountType());

        BankAccount savedAccount = bankAccountRepository.save(bankAccount);

        return toResponse(savedAccount);
    }

    public BankAccountResponse getBankAccountById(Long id) {

        BankAccount bankAccount = bankAccountRepository.findById(id)
                .orElseThrow(() ->
                        new BankAccountNotFoundException("Bank account not found"));

        return toResponse(bankAccount);
    }

    public List<BankAccountResponse> getAllBankAccounts() {

        return bankAccountRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public BankAccountResponse updateBankAccount(
            Long id,
            BankAccountRequest request) {

        BankAccount bankAccount = bankAccountRepository.findById(id)
                .orElseThrow(() ->
                        new BankAccountNotFoundException("Bank account not found"));

        bankAccount.setAccountNumber(request.getAccountNumber());
        bankAccount.setBalance(request.getBalance());
        bankAccount.setAccountType(request.getAccountType());

        BankAccount updatedAccount =
                bankAccountRepository.save(bankAccount);

        return toResponse(updatedAccount);
    }

    public void deleteBankAccount(Long id) {

        BankAccount bankAccount = bankAccountRepository.findById(id)
                .orElseThrow(() ->
                        new BankAccountNotFoundException("Bank account not found"));

        bankAccountRepository.delete(bankAccount);
    }

    public Set<CustomerResponse> getCustomersByBankAccountId(
        Long accountId) {

    BankAccount bankAccount = bankAccountRepository.findById(accountId)
            .orElseThrow(() ->
                    new BankAccountNotFoundException(
                            "Bank account not found"));

    return bankAccount.getCustomers()
            .stream()
            .map(customer -> new CustomerResponse(
                    customer.getId(),
                    customer.getName(),
                    customer.getEmail(),
                    customer.getPhoneNumber()
            ))
            .collect(java.util.stream.Collectors.toSet());
}

    private BankAccountResponse toResponse(BankAccount bankAccount) {

        return new BankAccountResponse(
                bankAccount.getId(),
                bankAccount.getAccountNumber(),
                bankAccount.getBalance(),
                bankAccount.getAccountType()
        );
    }
}