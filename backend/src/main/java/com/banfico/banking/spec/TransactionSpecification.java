
package com.banfico.banking.specification;

import com.banfico.banking.entity.Transaction;
import com.banfico.banking.entity.TransactionType;

import jakarta.persistence.criteria.JoinType;

import org.springframework.data.jpa.domain.Specification;

public class TransactionSpecification {

    private TransactionSpecification() {
    }

    public static Specification<Transaction> hasBankAccountId(
            Long accountId) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("bankAccount").get("id"),
                        accountId);
    }

    public static Specification<Transaction> belongsToUser(
            String keycloakUserId) {

        return (root, query, criteriaBuilder) -> {

            var bankAccount =
                    root.join("bankAccount", JoinType.INNER);

            var customers =
                    bankAccount.join("customers", JoinType.INNER);

            return criteriaBuilder.equal(
                    customers.get("keycloakUserId"),
                    keycloakUserId);
        };
    }

    public static Specification<Transaction> hasTransactionType(
            TransactionType transactionType) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("transactionType"),
                        transactionType);
    }

    public static Specification<Transaction> descriptionContains(
            String search) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(
                                root.get("description")),
                        "%" + search.toLowerCase() + "%");
    }
}
