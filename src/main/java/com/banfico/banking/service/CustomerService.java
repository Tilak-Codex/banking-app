package com.banfico.banking.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.banfico.banking.entity.Customer;
import com.banfico.banking.exception.CustomerNotFound;
import com.banfico.banking.repository.CustomerRepository;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer getCostomerById(Long Id) {
        return customerRepository.findById(Id).orElse(null);
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFound("Customer not found with id: " + id));
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer updateCustomer(Long id, Customer customerDetails) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFound("Customer not found with id: " + id));

        customer.setName(customerDetails.getName());
        customer.setEmail(customerDetails.getEmail());
        customer.setPhoneNumber(customerDetails.getPhoneNumber());

        return customerRepository.save(customer);
    }
    public void deleteCustomer(Long id) {
    Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new CustomerNotFound("Customer not found with id: " + id));

    customerRepository.delete(customer);
}
}
