package com.banfico.banking.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.banfico.banking.dto.CustomerRequest;
import com.banfico.banking.dto.CustomerResponse;
import com.banfico.banking.entity.BankAccount;
import com.banfico.banking.entity.Customer;
import com.banfico.banking.exception.BankAccountNotFoundException;
import com.banfico.banking.exception.CustomerNotFoundException;
import com.banfico.banking.repository.BankAccountRepository;
import com.banfico.banking.repository.CustomerRepository;

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

        Customer customer = new Customer();

        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhoneNumber(request.getPhoneNumber());

        Customer savedCustomer = customerRepository.save(customer);

        return toResponse(savedCustomer);
    }

    public CustomerResponse getCustomerById(Long id) {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() ->
                        new CustomerNotFoundException("Customer not found"));

        return toResponse(customer);
    }

    public List<CustomerResponse> getAllCustomers() {

        return customerRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public CustomerResponse updateCustomer(
            Long id,
            CustomerRequest request) {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() ->
                        new CustomerNotFoundException("Customer not found"));

        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhoneNumber(request.getPhoneNumber());

        Customer updatedCustomer = customerRepository.save(customer);

        return toResponse(updatedCustomer);
    }

    public void deleteCustomer(Long id) {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() ->
                        new CustomerNotFoundException("Customer not found"));

        customerRepository.delete(customer);
    }

    public Customer addBankAccountToCustomer(
            Long customerId,
            Long accountId) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new CustomerNotFoundException("Customer not found"));

        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(() ->
                        new BankAccountNotFoundException("Bank account not found"));

        customer.getBankAccounts().add(bankAccount);

        return customerRepository.save(customer);
    }

    public Customer removeBankAccountFromCustomer(
            Long customerId,
            Long accountId) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new CustomerNotFoundException("Customer not found"));

        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(() ->
                        new BankAccountNotFoundException("Bank account not found"));

        customer.getBankAccounts().remove(bankAccount);

        return customerRepository.save(customer);
    }

    public Set<BankAccount> getBankAccountsByCustomerId(Long customerId) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new CustomerNotFoundException("Customer not found"));

        return customer.getBankAccounts();
    }

    private CustomerResponse toResponse(Customer customer) {

        return new CustomerResponse(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhoneNumber()
        );
    }
}