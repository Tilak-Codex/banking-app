package com.banfico.banking.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banfico.banking.entity.BankAccount;
import com.banfico.banking.entity.Transaction;
import com.banfico.banking.entity.TransactionType;
import com.banfico.banking.exception.BankAccountNotFoundException;
import com.banfico.banking.exception.TransactionNotFoundException;
import com.banfico.banking.repository.BankAccountRepository;
import com.banfico.banking.repository.TransactionRepository;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final BankAccountRepository bankAccountRepository;

    public TransactionService(
            TransactionRepository transactionRepository,
            BankAccountRepository bankAccountRepository) {

        this.transactionRepository = transactionRepository;
        this.bankAccountRepository = bankAccountRepository;
    }

    @Transactional
    public Transaction createTransaction(Transaction transaction) {

        BankAccount bankAccount = bankAccountRepository.findById(
                transaction.getBankAccount().getId()).orElseThrow(
                        () -> new BankAccountNotFoundException(
                                "Bank account not found"));

        BigDecimal amount = transaction.getAmount();

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Transaction amount must be greater than zero");
        }

        if (TransactionType.DEPOSIT == transaction.getTransactionType()) {

            bankAccount.setBalance(
                    bankAccount.getBalance().add(amount));

        } else if (TransactionType.WITHDRAWAL == transaction.getTransactionType()) {

            if (bankAccount.getBalance().compareTo(amount) < 0) {
                throw new IllegalArgumentException(
                        "Insufficient account balance");
            }

            bankAccount.setBalance(
                    bankAccount.getBalance().subtract(amount));

        } else {
            throw new IllegalArgumentException(
                    "Invalid transaction type");
        }

        transaction.setBankAccount(bankAccount);
        transaction.setTransactionDate(LocalDateTime.now());

        bankAccountRepository.save(bankAccount);

        return transactionRepository.save(transaction);
    }

    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(
                        "Transaction not found"));
    }
    public List<Transaction> getAllTransactions() {
    return transactionRepository.findAll();
}
public List<Transaction> getTransactionsByBankAccountId(Long accountId) {
    return transactionRepository.findByBankAccountId(accountId);
}
}