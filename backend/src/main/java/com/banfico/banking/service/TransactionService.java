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
import org.springframework.security.oauth2.jwt.Jwt;
import com.banfico.banking.dto.TransferRequest;

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
                        TransactionRequest request,
                        Jwt jwt) {

                BankAccount bankAccount = bankAccountRepository
                                .findById(request.getBankAccountId())
                                .orElseThrow(() -> new BankAccountNotFoundException("Bank account not found"));
                String keycloakUserId = jwt.getSubject();

                boolean ownsAccount = bankAccount.getCustomers()
                                .stream()
                                .anyMatch(customer -> keycloakUserId.equals(
                                                customer.getKeycloakUserId()));

                if (!ownsAccount) {
                        throw new BankAccountNotFoundException(
                                        "Bank account not found");
                }
                BigDecimal amount = request.getAmount();

                if (bankAccount.getBalance() == null) {
                        throw new IllegalArgumentException(
                                        "Account balance is not initialized");
                }

                if (TransactionType.DEPOSIT == request.getTransactionType()) {

                        bankAccount.setBalance(
                                        bankAccount.getBalance().add(amount));

                } else if (TransactionType.WITHDRAWAL == request.getTransactionType()) {

                        if (bankAccount.getBalance().compareTo(amount) < 0) {
                                throw new IllegalArgumentException(
                                                "Insufficient account balance");
                        }

                        bankAccount.setBalance(
                                        bankAccount.getBalance().subtract(amount));
                }

                // Balance after applying this transaction
                BigDecimal balanceAfter = bankAccount.getBalance();

                Transaction transaction = new Transaction();

                transaction.setAmount(amount);
                transaction.setBalanceAfter(balanceAfter);
                transaction.setTransactionType(request.getTransactionType());
                transaction.setDescription(request.getDescription());
                transaction.setTransactionDate(LocalDateTime.now());
                transaction.setBankAccount(bankAccount);

                bankAccountRepository.save(bankAccount);

                Transaction savedTransaction = transactionRepository.save(transaction);

                return toResponse(savedTransaction);
                
        }
       
@Transactional
public List<TransactionResponse> transfer(
        TransferRequest request,
        Jwt jwt) {

    String keycloakUserId = jwt.getSubject();

    // 1. Find source account
    BankAccount sourceAccount = bankAccountRepository
            .findById(request.getSourceAccountId())
            .orElseThrow(() ->
                    new BankAccountNotFoundException(
                            "Source account not found"));

    // 2. Verify the logged-in user owns the source account
    boolean ownsSourceAccount = sourceAccount.getCustomers()
            .stream()
            .anyMatch(customer ->
                    keycloakUserId.equals(
                            customer.getKeycloakUserId()));

    if (!ownsSourceAccount) {
        throw new BankAccountNotFoundException(
                "Source account not found");
    }

    // 3. Find destination account using account number
    BankAccount destinationAccount = bankAccountRepository
            .findByAccountNumber(
                    request.getDestinationAccountNumber())
            .orElseThrow(() ->
                    new BankAccountNotFoundException(
                            "Destination account not found"));

    // 4. Prevent transferring to the same account
    if (sourceAccount.getId().equals(destinationAccount.getId())) {
        throw new IllegalArgumentException(
                "Source and destination accounts must be different");
    }

    // 5. Check balances are initialized
    if (sourceAccount.getBalance() == null
            || destinationAccount.getBalance() == null) {

        throw new IllegalArgumentException(
                "Account balance is not initialized");
    }

    BigDecimal amount = request.getAmount();

    // 6. Check sufficient balance
    if (sourceAccount.getBalance().compareTo(amount) < 0) {
        throw new IllegalArgumentException(
                "Insufficient account balance");
    }

    // 7. Debit source account
    sourceAccount.setBalance(
            sourceAccount.getBalance().subtract(amount));

    // 8. Credit destination account
    destinationAccount.setBalance(
            destinationAccount.getBalance().add(amount));

    // 9. Create withdrawal transaction for source
    Transaction withdrawal = new Transaction();

    withdrawal.setAmount(amount);
    withdrawal.setBalanceAfter(sourceAccount.getBalance());
    withdrawal.setTransactionType(TransactionType.WITHDRAWAL);
    withdrawal.setDescription(request.getDescription());
    withdrawal.setTransactionDate(LocalDateTime.now());
    withdrawal.setBankAccount(sourceAccount);

    // 10. Create deposit transaction for destination
    Transaction deposit = new Transaction();

    deposit.setAmount(amount);
    deposit.setBalanceAfter(destinationAccount.getBalance());
    deposit.setTransactionType(TransactionType.DEPOSIT);
    deposit.setDescription(request.getDescription());
    deposit.setTransactionDate(LocalDateTime.now());
    deposit.setBankAccount(destinationAccount);

    // 11. Save both account balances
    bankAccountRepository.save(sourceAccount);
    bankAccountRepository.save(destinationAccount);

    // 12. Save both transaction records
    Transaction savedWithdrawal =
            transactionRepository.save(withdrawal);

    Transaction savedDeposit =
            transactionRepository.save(deposit);

    return List.of(
            toResponse(savedWithdrawal),
            toResponse(savedDeposit));
}

        public TransactionResponse getTransactionById(
                        Long id,
                        Jwt jwt) {

                Transaction transaction = transactionRepository.findById(id)
                                .orElseThrow(() -> new TransactionNotFoundException(
                                                "Transaction not found"));

                String keycloakUserId = jwt.getSubject();

                boolean ownsAccount = transaction.getBankAccount()
                                .getCustomers()
                                .stream()
                                .anyMatch(customer -> keycloakUserId.equals(
                                                customer.getKeycloakUserId()));

                if (!ownsAccount) {
                        throw new TransactionNotFoundException(
                                        "Transaction not found");
                }

                return toResponse(transaction);
        }

        public List<TransactionResponse> getTransactionsByBankAccountId(
                        Long bankAccountId) {

                bankAccountRepository.findById(bankAccountId)
                                .orElseThrow(() -> new BankAccountNotFoundException(
                                                "Bank account not found"));

                return transactionRepository
                                .findByBankAccountIdOrderByTransactionDateDesc(
                                                bankAccountId)
                                .stream()
                                .map(this::toResponse)
                                .toList();
        }

        @Transactional
public TransactionResponse reverseTransaction(
        Long transactionId,
        Jwt jwt) {

    Transaction originalTransaction = transactionRepository.findById(transactionId)
            .orElseThrow(() -> new TransactionNotFoundException(
                    "Transaction not found"));

    if (originalTransaction.isReversed()) {
        throw new IllegalArgumentException(
                "Transaction has already been reversed");
    }

    String keycloakUserId = jwt.getSubject();

    boolean ownsAccount = originalTransaction.getBankAccount()
            .getCustomers()
            .stream()
            .anyMatch(customer -> keycloakUserId.equals(
                    customer.getKeycloakUserId()));

    if (!ownsAccount) {
        throw new BankAccountNotFoundException(
                "Transaction not found");
    }

    BankAccount bankAccount = originalTransaction.getBankAccount();
    BigDecimal amount = originalTransaction.getAmount();

    if (originalTransaction.getTransactionType() == TransactionType.DEPOSIT) {
        bankAccount.setBalance(
                bankAccount.getBalance().subtract(amount));

    } else if (originalTransaction.getTransactionType() == TransactionType.WITHDRAWAL) {
        bankAccount.setBalance(
                bankAccount.getBalance().add(amount));
    }

    originalTransaction.setReversed(true);
    originalTransaction.setBalanceAfter(bankAccount.getBalance());

    bankAccountRepository.save(bankAccount);

    Transaction savedTransaction =
            transactionRepository.save(originalTransaction);

    return toResponse(savedTransaction);
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
                                transaction.getBankAccount().getId(),
                                transaction.getBalanceAfter());
        }

        public List<TransactionResponse> getMyTransactions(
                        Long accountId,
                        Jwt jwt) {

                String keycloakUserId = jwt.getSubject();

                List<Transaction> transactions = transactionRepository
                                .findByBankAccount_IdAndBankAccount_Customers_KeycloakUserId(
                                                accountId,
                                                keycloakUserId);

                return transactions.stream()
                                .map(this::toResponse)
                                .toList();
        }
}