package com.banfico.banking.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.banfico.banking.entity.BankAccount;
import com.banfico.banking.entity.Customer;
import com.banfico.banking.exception.BankAccountNotFoundException;
import com.banfico.banking.exception.CustomerNotFoundException;
import com.banfico.banking.repository.CustomerRepository;
import com.banfico.banking.entity.BankAccount;
import com.banfico.banking.exception.BankAccountNotFoundException;
import com.banfico.banking.repository.BankAccountRepository;

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

    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer getCostomerById(Long Id) {
        return customerRepository.findById(Id).orElse(null);
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer updateCustomer(Long id, Customer customerDetails) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));

        customer.setName(customerDetails.getName());
        customer.setEmail(customerDetails.getEmail());
        customer.setPhoneNumber(customerDetails.getPhoneNumber());

        return customerRepository.save(customer);
    }

    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));

        customerRepository.delete(customer);
    }

    public Customer addBankAccountToCustomer(
            Long customerId,
            Long accountId) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(
                        "Customer not found"));

        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new BankAccountNotFoundException(
                        "Bank account not found"));

        customer.getBankAccounts().add(bankAccount);

        return customerRepository.save(customer);
    }

    public Customer removeBankAccountFromCustomer(
            Long customerId,
            Long accountId) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(
                        "Customer not found"));

        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new BankAccountNotFoundException(
                        "Bank account not found"));

        customer.getBankAccounts().remove(bankAccount);

        return customerRepository.save(customer);
    }
    public Set<BankAccount> getBankAccountsByCustomerId(Long customerId) {

    Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new CustomerNotFoundException(
                    "Customer not found"));

    return customer.getBankAccounts();
}
}
