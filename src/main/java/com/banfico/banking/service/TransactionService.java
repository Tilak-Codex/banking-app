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

        if (transaction.getBankAccount() == null
                || transaction.getBankAccount().getId() == null) {

            throw new IllegalArgumentException(
                    "Bank account is required");
        }

        BankAccount bankAccount = bankAccountRepository.findById(
                transaction.getBankAccount().getId())
                .orElseThrow(() -> new BankAccountNotFoundException(
                        "Bank account not found"));

        BigDecimal amount = transaction.getAmount();

        if (amount == null
                || amount.compareTo(BigDecimal.ZERO) <= 0) {

            throw new IllegalArgumentException(
                    "Transaction amount must be greater than zero");
        }

        if (transaction.getTransactionType() == null) {

            throw new IllegalArgumentException(
                    "Transaction type is required");
        }

        if (bankAccount.getBalance() == null) {

            throw new IllegalArgumentException(
                    "Account balance is not initialized");
        }

        if (TransactionType.DEPOSIT
                == transaction.getTransactionType()) {

            bankAccount.setBalance(
                    bankAccount.getBalance().add(amount));

        } else if (TransactionType.WITHDRAWAL
                == transaction.getTransactionType()) {

            if (bankAccount.getBalance().compareTo(amount) < 0) {

                throw new IllegalArgumentException(
                        "Insufficient account balance");
            }

            bankAccount.setBalance(
                    bankAccount.getBalance().subtract(amount));
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

    public List<Transaction> getTransactionsByBankAccountId(
            Long bankAccountId) {

        bankAccountRepository.findById(bankAccountId)
                .orElseThrow(() -> new BankAccountNotFoundException(
                        "Bank account not found"));

        return transactionRepository
                .findByBankAccountIdOrderByTransactionDateDesc(
                        bankAccountId);
    }

    @Transactional
    public Transaction reverseTransaction(Long transactionId) {

        Transaction originalTransaction =
                transactionRepository.findById(transactionId)
                        .orElseThrow(() -> new TransactionNotFoundException(
                                "Transaction not found"));

        // Prevent the same transaction from being reversed twice
        if (originalTransaction.isReversed()) {

            throw new IllegalArgumentException(
                    "Transaction has already been reversed");
        }

        BankAccount bankAccount =
                originalTransaction.getBankAccount();

        BigDecimal amount =
                originalTransaction.getAmount();

        if (bankAccount == null) {

            throw new IllegalArgumentException(
                    "Transaction is not associated with a bank account");
        }

        if (bankAccount.getBalance() == null) {

            throw new IllegalArgumentException(
                    "Account balance is not initialized");
        }

        if (TransactionType.DEPOSIT
                == originalTransaction.getTransactionType()) {

            if (bankAccount.getBalance().compareTo(amount) < 0) {

                throw new IllegalArgumentException(
                        "Insufficient balance to reverse transaction");
            }

            bankAccount.setBalance(
                    bankAccount.getBalance().subtract(amount));

        } else if (TransactionType.WITHDRAWAL
                == originalTransaction.getTransactionType()) {

            bankAccount.setBalance(
                    bankAccount.getBalance().add(amount));
        }

        // Create a new transaction representing the reversal
        Transaction reversalTransaction = new Transaction();

        reversalTransaction.setBankAccount(bankAccount);
        reversalTransaction.setAmount(amount);

        reversalTransaction.setTransactionType(
                originalTransaction.getTransactionType()
                        == TransactionType.DEPOSIT
                        ? TransactionType.WITHDRAWAL
                        : TransactionType.DEPOSIT);

        reversalTransaction.setDescription(
                "Reversal of transaction "
                        + originalTransaction.getId());

        reversalTransaction.setTransactionDate(
                LocalDateTime.now());

        // Mark the original transaction as reversed
        originalTransaction.setReversed(true);

        bankAccountRepository.save(bankAccount);

        transactionRepository.save(originalTransaction);

        return transactionRepository.save(reversalTransaction);
    }
}