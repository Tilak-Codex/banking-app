package com.banfico.banking.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.banfico.banking.entity.BankAccount;
import com.banfico.banking.exception.BankAccountNotFoundException;
import com.banfico.banking.repository.BankAccountRepository;

@Service
public class BankAccountService {
    private final BankAccountRepository bankAccountRepository;

    public BankAccountService(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    public BankAccount createBankAccount(BankAccount bankAccount) {
        return bankAccountRepository.save(bankAccount);
    }

    public BankAccount getBankAccountById(Long id) {
        return bankAccountRepository.findById(id)
                .orElseThrow(() -> new BankAccountNotFoundException(
                        "Bank account not found"));
    }

    public List<BankAccount> getAllBankAccounts() {
        return bankAccountRepository.findAll();
    }

    public BankAccount updateBankAccount(Long id, BankAccount accountDetails) {
        BankAccount bankAccount = bankAccountRepository.findById(id)
                .orElseThrow(() -> new BankAccountNotFoundException(
                        "Bank account not found"));

        bankAccount.setAccountNumber(accountDetails.getAccountNumber());
        bankAccount.setBalance(accountDetails.getBalance());
        bankAccount.setAccountType(accountDetails.getAccountType());

        return bankAccountRepository.save(bankAccount);
    }
    public void deleteBankAccount(Long id) {
    BankAccount bankAccount = bankAccountRepository.findById(id)
            .orElseThrow(() -> new BankAccountNotFoundException(
                    "Bank account not found"));

    bankAccountRepository.delete(bankAccount);
}

}