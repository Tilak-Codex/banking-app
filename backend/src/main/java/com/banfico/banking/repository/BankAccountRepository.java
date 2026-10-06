
package com.banfico.banking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.banfico.banking.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BankAccountRepository
                extends JpaRepository<BankAccount, Long>,
                JpaSpecificationExecutor<BankAccount> {

        boolean existsByAccountNumber(String accountNumber);

        boolean existsByAccountNumberAndIdNot(
                        String accountNumber,
                        Long id);

        Optional<BankAccount> findByAccountNumber(
                        String accountNumber);

        List<BankAccount> findByCustomers_KeycloakUserId(
                        String keycloakUserId);

        Optional<BankAccount> findByAccountNumberAndCustomers_KeycloakUserId(
                        String accountNumber,
                        String keycloakUserId);

        Optional<BankAccount> findByIdAndCustomers_KeycloakUserId(
                        Long id,
                        String keycloakUserId);

        Page<BankAccount> findByAccountNumberContainingIgnoreCase(
                        String accountNumber,
                        Pageable pageable);
}
