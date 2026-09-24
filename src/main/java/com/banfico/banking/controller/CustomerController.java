package com.banfico.banking.controller;

import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.banfico.banking.dto.CustomerRequest;
import com.banfico.banking.dto.CustomerResponse;
import com.banfico.banking.entity.BankAccount;
import com.banfico.banking.entity.Customer;
import com.banfico.banking.service.CustomerService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
public CustomerResponse createCustomer(
        @Valid @RequestBody CustomerRequest request) {

    return customerService.createCustomer(request);
}

    @GetMapping("/{id}")
public CustomerResponse getCustomerById(@PathVariable Long id) {

    return customerService.getCustomerById(id);
}

    @GetMapping
public List<CustomerResponse> getAllCustomers() {

    return customerService.getAllCustomers();
}

   @PutMapping("/{id}")
public CustomerResponse updateCustomer(
        @PathVariable Long id,
        @Valid @RequestBody CustomerRequest request) {

    return customerService.updateCustomer(id, request);
}

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
    }

    @PostMapping("/{customerId}/accounts/{accountId}")
    public Customer addBankAccountToCustomer(
            @PathVariable Long customerId,
            @PathVariable Long accountId) {

        return customerService.addBankAccountToCustomer(
                customerId, accountId);
    }

    @DeleteMapping("/{customerId}/accounts/{accountId}")
    public Customer removeBankAccountFromCustomer(
            @PathVariable Long customerId,
            @PathVariable Long accountId) {

        return customerService.removeBankAccountFromCustomer(
                customerId, accountId);
    }

    @GetMapping("/{customerId}/accounts")
    public Set<BankAccount> getBankAccountsByCustomerId(
            @PathVariable Long customerId) {

        return customerService.getBankAccountsByCustomerId(customerId);
    }
}