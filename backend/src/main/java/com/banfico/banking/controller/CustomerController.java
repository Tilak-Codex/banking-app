package com.banfico.banking.controller;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.banfico.banking.dto.BankAccountResponse;
import com.banfico.banking.dto.CustomerRequest;
import com.banfico.banking.dto.CustomerResponse;
import com.banfico.banking.dto.KeycloakUserLinkRequest;
import com.banfico.banking.service.CustomerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/{customerId}/accounts/{accountId}")
    @ResponseStatus(HttpStatus.CREATED)
    public BankAccountResponse addBankAccountToCustomer(
            @PathVariable Long customerId,
            @PathVariable Long accountId,
            @AuthenticationPrincipal Jwt jwt) {

        return customerService.addBankAccountToCustomer(
                customerId,
                accountId,
                jwt);
    }

  

    @GetMapping("/{customerId}/accounts")
    public Set<BankAccountResponse> getBankAccountsByCustomerId(
            @PathVariable Long customerId,
            @AuthenticationPrincipal Jwt jwt) {

        return customerService.getBankAccountsByCustomerId(
                customerId,
                jwt);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerResponse createCustomer(
            @Valid @RequestBody CustomerRequest request) {

        return customerService.createCustomer(request);
    }

    @GetMapping
    public List<CustomerResponse> getAllCustomers(
            @RequestParam(required = false, defaultValue = "0") int pageNo,
            @RequestParam(required = false, defaultValue = "5") int pageSize) {

        return customerService.getAllCustomers(
                PageRequest.of(pageNo, pageSize));
    }

    @GetMapping("/{id}")
    public CustomerResponse getCustomerById(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt) {

        return customerService.getCustomerById(id, jwt);
    }

    @PutMapping("/{id}")
    public CustomerResponse updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CustomerRequest request,
            @AuthenticationPrincipal Jwt jwt) {

        return customerService.updateCustomer(
                id,
                request,
                jwt);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomer(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt) {

        customerService.deleteCustomer(id, jwt);
    }

    @PutMapping("/{customerId}/keycloak-user")
    public ResponseEntity<CustomerResponse> linkKeycloakUser(
            @PathVariable Long customerId,
            @Valid @RequestBody KeycloakUserLinkRequest request) {

        return ResponseEntity.ok(
                customerService.linkKeycloakUser(
                        customerId,
                        request));
    }

    @GetMapping("/me")
    public ResponseEntity<CustomerResponse> getMyCustomer(
            @AuthenticationPrincipal Jwt jwt) {

        return ResponseEntity.ok(
                customerService.getMyCustomer(jwt));
    }
    @DeleteMapping("/{customerId}/accounts/{accountId}")
public BankAccountResponse removeBankAccountFromCustomer(
        @PathVariable Long customerId,
        @PathVariable Long accountId,
        @AuthenticationPrincipal Jwt jwt) {

    return customerService.removeBankAccountFromCustomer(
            customerId,
            accountId,
            jwt);
}   
}