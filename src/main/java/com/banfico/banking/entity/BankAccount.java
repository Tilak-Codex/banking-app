package com.banfico.banking.entity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "accounts")
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String accountNumber;

    @Column(nullable = false)
    private BigDecimal balance;

    @Column(nullable = false)
    private String accountType;

    @ManyToMany(mappedBy = "bankAccounts") // Bidirectional Relationship with Customer and mapped using the
                                           // "bankAccounts" field in Customer
    private Set<Customer> customers = new HashSet<>();

    @OneToMany(mappedBy = "bankAccount") // MappedBy will come in non-owning side of the relationship, which is the
                                         // Transaction entity in this case
    private Set<Transaction> transactions = new HashSet<>();
}
