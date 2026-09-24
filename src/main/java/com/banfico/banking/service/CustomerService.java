package com.banfico.banking.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.banfico.banking.dto.BankAccountResponse;
import com.banfico.banking.dto.CustomerRequest;
import com.banfico.banking.dto.CustomerResponse;
import com.banfico.banking.entity.BankAccount;
import com.banfico.banking.entity.Customer;
import com.banfico.banking.exception.BankAccountNotFoundException;
import com.banfico.banking.exception.CustomerNotFoundException;
import com.banfico.banking.repository.BankAccountRepository;
import com.banfico.banking.repository.CustomerRepository;
import com.banfico.banking.exception.DuplicateResourceException;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final BankAccountRepository bankAccountRepository;

    public CustomerService(
            CustomerRepository customerRepository,
            BankAccountRepository bankAccountRepository) {

        this.customerRepository = customerRepository;
        this.bankAccountRepository = bankAccountRepository;
    }

    public CustomerResponse createCustomer(CustomerRequest request) {

    if (customerRepository.existsByEmail(request.getEmail())) {
        throw new DuplicateResourceException(
                "Customer with this email already exists");
    }

    if (customerRepository.existsByPhoneNumber(
            request.getPhoneNumber())) {

        throw new DuplicateResourceException(
                "Customer with this phone number already exists");
    }

    Customer customer = new Customer();

    customer.setName(request.getName());
    customer.setEmail(request.getEmail());
    customer.setPhoneNumber(request.getPhoneNumber());

    Customer savedCustomer = customerRepository.save(customer);

    return toResponse(savedCustomer);
}

    public BankAccountResponse addBankAccountToCustomer(
        Long customerId,
        Long accountId) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new CustomerNotFoundException("Customer not found"));

        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(() ->
                        new BankAccountNotFoundException("Bank account not found"));

        customer.getBankAccounts().add(bankAccount);

        customerRepository.save(customer);

return new BankAccountResponse(
        bankAccount.getId(),
        bankAccount.getAccountNumber(),
        bankAccount.getBalance(),
        bankAccount.getAccountType()
);
    }

    public BankAccountResponse removeBankAccountFromCustomer(
        Long customerId,
        Long accountId) {

    Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() ->
                    new CustomerNotFoundException("Customer not found"));

    BankAccount bankAccount = bankAccountRepository.findById(accountId)
            .orElseThrow(() ->
                    new BankAccountNotFoundException("Bank account not found"));

    customer.getBankAccounts().remove(bankAccount);

    customerRepository.save(customer);

    return new BankAccountResponse(
            bankAccount.getId(),
            bankAccount.getAccountNumber(),
            bankAccount.getBalance(),
            bankAccount.getAccountType()
    );
}
    public Set<BankAccountResponse> getBankAccountsByCustomerId(
        Long customerId) {

    Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() ->
                    new CustomerNotFoundException("Customer not found"));

    return customer.getBankAccounts()
            .stream()
            .map(account -> new BankAccountResponse(
                    account.getId(),
                    account.getAccountNumber(),
                    account.getBalance(),
                    account.getAccountType()
            ))
            .collect(java.util.stream.Collectors.toSet());
}

    private CustomerResponse toResponse(Customer customer) {

        return new CustomerResponse(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhoneNumber()
        );
    }
    public CustomerResponse updateCustomer(
        Long id,
        CustomerRequest request) {

    Customer customer = customerRepository.findById(id)
            .orElseThrow(() ->
                    new CustomerNotFoundException("Customer not found"));

    if (customerRepository.existsByEmailAndIdNot(
            request.getEmail(), id)) {

        throw new DuplicateResourceException(
                "Customer with this email already exists");
    }

    if (customerRepository.existsByPhoneNumberAndIdNot(
            request.getPhoneNumber(), id)) {

        throw new DuplicateResourceException(
                "Customer with this phone number already exists");
    }

    customer.setName(request.getName());
    customer.setEmail(request.getEmail());
    customer.setPhoneNumber(request.getPhoneNumber());

    Customer updatedCustomer =
            customerRepository.save(customer);

    return toResponse(updatedCustomer);
}
    public void deleteCustomer(Long id) {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() ->
                        new CustomerNotFoundException("Customer not found"));

        customerRepository.delete(customer);
    }
}