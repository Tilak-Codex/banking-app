package com.banfico.banking.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banfico.banking.dto.TransactionRequest;
import com.banfico.banking.dto.TransactionResponse;
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
    public TransactionResponse createTransaction(
            TransactionRequest request) {

        BankAccount bankAccount = bankAccountRepository
                .findById(request.getBankAccountId())
                .orElseThrow(() ->
                        new BankAccountNotFoundException(
                                "Bank account not found"));

        BigDecimal amount = request.getAmount();

        if (bankAccount.getBalance() == null) {
            throw new IllegalArgumentException(
                    "Account balance is not initialized");
        }

        if (TransactionType.DEPOSIT == request.getTransactionType()) {

            bankAccount.setBalance(
                    bankAccount.getBalance().add(amount)
            );

        } else if (TransactionType.WITHDRAWAL == request.getTransactionType()) {

            if (bankAccount.getBalance().compareTo(amount) < 0) {
                throw new IllegalArgumentException(
                        "Insufficient account balance");
            }

            bankAccount.setBalance(
                    bankAccount.getBalance().subtract(amount)
            );
        }

        Transaction transaction = new Transaction();

        transaction.setAmount(amount);
        transaction.setTransactionType(request.getTransactionType());
        transaction.setDescription(request.getDescription());
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setBankAccount(bankAccount);

        bankAccountRepository.save(bankAccount);

        Transaction savedTransaction =
                transactionRepository.save(transaction);

        return toResponse(savedTransaction);
    }

    public TransactionResponse getTransactionById(Long id) {

        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() ->
                        new TransactionNotFoundException(
                                "Transaction not found"));

        return toResponse(transaction);
    }

    public List<TransactionResponse> getAllTransactions() {

        return transactionRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<TransactionResponse> getTransactionsByBankAccountId(
            Long bankAccountId) {

        bankAccountRepository.findById(bankAccountId)
                .orElseThrow(() ->
                        new BankAccountNotFoundException(
                                "Bank account not found"));

        return transactionRepository
                .findByBankAccountIdOrderByTransactionDateDesc(
                        bankAccountId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public TransactionResponse reverseTransaction(Long transactionId) {

        Transaction originalTransaction =
                transactionRepository.findById(transactionId)
                        .orElseThrow(() ->
                                new TransactionNotFoundException(
                                        "Transaction not found"));

        if (originalTransaction.isReversed()) {
            throw new IllegalArgumentException(
                    "Transaction has already been reversed");
        }

        BankAccount bankAccount =
                originalTransaction.getBankAccount();

        BigDecimal amount =
                originalTransaction.getAmount();

        if (TransactionType.DEPOSIT ==
                originalTransaction.getTransactionType()) {

            if (bankAccount.getBalance().compareTo(amount) < 0) {
                throw new IllegalArgumentException(
                        "Insufficient balance to reverse transaction");
            }

            bankAccount.setBalance(
                    bankAccount.getBalance().subtract(amount)
            );

        } else if (TransactionType.WITHDRAWAL ==
                originalTransaction.getTransactionType()) {

            bankAccount.setBalance(
                    bankAccount.getBalance().add(amount)
            );
        }

        Transaction reversalTransaction = new Transaction();

        reversalTransaction.setBankAccount(bankAccount);
        reversalTransaction.setAmount(amount);

        reversalTransaction.setTransactionType(
                originalTransaction.getTransactionType()
                        == TransactionType.DEPOSIT
                        ? TransactionType.WITHDRAWAL
                        : TransactionType.DEPOSIT
        );

        reversalTransaction.setDescription(
                "Reversal of transaction "
                        + originalTransaction.getId()
        );

        reversalTransaction.setTransactionDate(
                LocalDateTime.now()
        );

        originalTransaction.setReversed(true);

        bankAccountRepository.save(bankAccount);
        transactionRepository.save(originalTransaction);

        Transaction savedReversal =
                transactionRepository.save(reversalTransaction);

        return toResponse(savedReversal);
    }

    private TransactionResponse toResponse(
            Transaction transaction) {

        return new TransactionResponse(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getTransactionType(),
                transaction.getDescription(),
                transaction.getTransactionDate(),
                transaction.isReversed(),
                transaction.getBankAccount().getId()
        );
    }
}