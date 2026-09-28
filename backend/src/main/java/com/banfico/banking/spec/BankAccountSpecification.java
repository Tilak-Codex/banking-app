package com.banfico.banking.spec;

import com.banfico.banking.entity.BankAccount;
import org.springframework.data.jpa.domain.Specification;

public class BankAccountSpecification {

    public static Specification<BankAccount> hasAccount(String accountNumber) {

        return (root, query, criteriaBuilder) ->
                accountNumber == null
                        ? null
                        : criteriaBuilder.equal(
                                root.get("accountNumber"),
                                accountNumber
                        );
    }
}