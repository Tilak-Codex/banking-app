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
import com.banfico.banking.dto.BankAccountUpdateRequest;
import com.banfico.banking.exception.DuplicateResourceException;
import com.banfico.banking.spec.BankAccountSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class BankAccountService {

        private final BankAccountRepository bankAccountRepository;

        public BankAccountService(BankAccountRepository bankAccountRepository) {
                this.bankAccountRepository = bankAccountRepository;
        }

        public BankAccountResponse createBankAccount(
                        BankAccountRequest request) {

                if (bankAccountRepository.existsByAccountNumber(
                                request.getAccountNumber())) {

                        throw new DuplicateResourceException(
                                        "Bank account with this account number already exists");
                }

                BankAccount bankAccount = new BankAccount();

                bankAccount.setAccountNumber(request.getAccountNumber());
                bankAccount.setBalance(request.getBalance());
                bankAccount.setAccountType(request.getAccountType());

                BankAccount savedAccount = bankAccountRepository.save(bankAccount);

                return toResponse(savedAccount);
        }

       public Page<BankAccountResponse> getAllBankAccounts(Pageable pageable) {
    return bankAccountRepository.findAll(pageable)
            .map(this::toResponse);
}
        public BankAccountResponse getBankAccountById(Long id) {
                BankAccount bankAccount = bankAccountRepository.findById(id)
                                .orElseThrow(() -> new BankAccountNotFoundException(
                                                "Bank account not found"));

                return toResponse(bankAccount);
        }

        public BankAccountResponse updateBankAccount(
                        Long id,
                        BankAccountUpdateRequest request) {

                BankAccount bankAccount = bankAccountRepository.findById(id)
                                .orElseThrow(() -> new BankAccountNotFoundException(
                                                "Bank account not found"));

                if (bankAccountRepository.existsByAccountNumberAndIdNot(
                                request.getAccountNumber(), id)) {

                        throw new DuplicateResourceException(
                                        "Bank account with this account number already exists");
                }

                bankAccount.setAccountNumber(request.getAccountNumber());
                bankAccount.setAccountType(request.getAccountType());

                BankAccount updatedAccount = bankAccountRepository.save(bankAccount);

                return toResponse(updatedAccount);
        }

        public void deleteBankAccount(Long id) {

                BankAccount bankAccount = bankAccountRepository.findById(id)
                                .orElseThrow(() -> new BankAccountNotFoundException("Bank account not found"));

                bankAccountRepository.delete(bankAccount);
        }

        public Set<CustomerResponse> getCustomersByBankAccountId(
                        Long accountId) {

                BankAccount bankAccount = bankAccountRepository.findById(accountId)
                                .orElseThrow(() -> new BankAccountNotFoundException(
                                                "Bank account not found"));

                return bankAccount.getCustomers()
                                .stream()
                                .map(customer -> new CustomerResponse(
                                                customer.getId(),
                                                customer.getName(),
                                                customer.getEmail(),
                                                customer.getPhoneNumber()))
                                .collect(java.util.stream.Collectors.toSet());
        }

        private BankAccountResponse toResponse(BankAccount bankAccount) {

                return new BankAccountResponse(
                                bankAccount.getId(),
                                bankAccount.getAccountNumber(),
                                bankAccount.getBalance(),
                                bankAccount.getAccountType());
        }
        public List<BankAccountResponse> searchAccount(String accountNumber) {
    Specification<BankAccount> spec =
            BankAccountSpecification.hasAccount(accountNumber);

    return bankAccountRepository.findAll(spec)
            .stream()
            .map(this::toResponse)
            .toList();
}
public List<BankAccountResponse> getMyAccounts(Jwt jwt) {

    String keycloakUserId = jwt.getSubject();

    List<BankAccount> accounts =
            bankAccountRepository.findByCustomers_KeycloakUserId(
                    keycloakUserId
            );

    return accounts.stream()
            .map(this::toResponse)
            .toList();
}
public BankAccountResponse getMyBankAccountById(
        Long id,
        Jwt jwt) {

    String keycloakUserId = jwt.getSubject();
System.out.println("JWT subject: " + jwt.getSubject());
    BankAccount bankAccount =
            bankAccountRepository
                    .findByIdAndCustomers_KeycloakUserId(
                            id,
                            keycloakUserId
                    )
                    .orElseThrow(() ->
                            new BankAccountNotFoundException(
                                    "Bank account not found"
                            ));

    return toResponse(bankAccount);
}
}