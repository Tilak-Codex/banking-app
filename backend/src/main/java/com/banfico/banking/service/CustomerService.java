package com.banfico.banking.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.banfico.banking.dto.BankAccountResponse;
import com.banfico.banking.dto.CustomerRequest;
import com.banfico.banking.dto.CustomerResponse;
import com.banfico.banking.dto.KeycloakUserLinkRequest;
import com.banfico.banking.entity.BankAccount;
import com.banfico.banking.entity.Customer;
import com.banfico.banking.exception.BankAccountNotFoundException;
import com.banfico.banking.exception.CustomerNotFoundException;
import com.banfico.banking.exception.DuplicateResourceException;
import com.banfico.banking.repository.BankAccountRepository;
import com.banfico.banking.repository.CustomerRepository;
import java.util.Map;
import jakarta.transaction.Transactional;

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
            Long accountId,
            Jwt jwt) {

        String keycloakUserId = jwt.getSubject();

        Customer customer = customerRepository
                .findByIdAndKeycloakUserId(customerId, keycloakUserId)
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
                bankAccount.getAccountType());
    }

    public BankAccountResponse removeBankAccountFromCustomer(
        Long customerId,
        Long accountId,
        Jwt jwt) {

        String keycloakUserId = jwt.getSubject();

        Customer customer = customerRepository
                .findByIdAndKeycloakUserId(customerId, keycloakUserId)
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
                bankAccount.getAccountType());
    }

    public Set<BankAccountResponse> getBankAccountsByCustomerId(
            Long customerId,
            Jwt jwt) {

        String keycloakUserId = jwt.getSubject();

        Customer customer = customerRepository
                .findByIdAndKeycloakUserId(customerId, keycloakUserId)
                .orElseThrow(() ->
                        new CustomerNotFoundException("Customer not found"));

        return customer.getBankAccounts()
                .stream()
                .map(account -> new BankAccountResponse(
                        account.getId(),
                        account.getAccountNumber(),
                        account.getBalance(),
                        account.getAccountType()))
                .collect(Collectors.toSet());
    }

    public CustomerResponse getCustomerById(
        Long id,
        Jwt jwt) {

    String keycloakUserId = jwt.getSubject();

    boolean isClient = false;

    Map<String, Object> realmAccess =
            jwt.getClaim("realm_access");

    if (realmAccess != null) {
        Object rolesObject = realmAccess.get("roles");

        if (rolesObject instanceof List<?> roles) {
            isClient = roles.contains("CLIENT");
        }
    }

    Customer customer;

    if (isClient) {
        customer = customerRepository
                .findByIdAndKeycloakUserId(
                        id,
                        keycloakUserId)
                .orElseThrow(() ->
                        new CustomerNotFoundException(
                                "Customer not found"));
    } else {
        customer = customerRepository
                .findById(id)
                .orElseThrow(() ->
                        new CustomerNotFoundException(
                                "Customer not found"));
    }

    return toResponse(customer);
}

    public List<CustomerResponse> getAllCustomers(Pageable pageable) {

        return customerRepository.findAll(pageable)
                .getContent()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private CustomerResponse toResponse(Customer customer) {

        return new CustomerResponse(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhoneNumber());
    }

    public CustomerResponse updateCustomer(
            Long id,
            CustomerRequest request,
            Jwt jwt) {

        String keycloakUserId = jwt.getSubject();

        Customer customer = customerRepository
                .findByIdAndKeycloakUserId(id, keycloakUserId)
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

        Customer updatedCustomer = customerRepository.save(customer);

        return toResponse(updatedCustomer);
    }

    @Transactional
    public void deleteCustomer(Long id, Jwt jwt) {

        String keycloakUserId = jwt.getSubject();

        Customer customer = customerRepository
                .findByIdAndKeycloakUserId(id, keycloakUserId)
                .orElseThrow(() ->
                        new CustomerNotFoundException("Customer not found"));

        if (!customer.getBankAccounts().isEmpty()) {
            throw new IllegalArgumentException(
                    "Customer cannot be deleted because bank accounts are linked");
        }

        if (!customer.getBeneficiaries().isEmpty()) {
            throw new IllegalArgumentException(
                    "Customer cannot be deleted because beneficiaries exist");
        }

        if (!customer.getConsents().isEmpty()) {
            throw new IllegalArgumentException(
                    "Customer cannot be deleted because consents exist");
        }

        customerRepository.delete(customer);
    }

    public CustomerResponse linkKeycloakUser(
            Long customerId,
            KeycloakUserLinkRequest request) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new CustomerNotFoundException("Customer not found"));

        if (customerRepository.existsByKeycloakUserId(
                request.getKeycloakUserId())) {

            throw new DuplicateResourceException(
                    "Keycloak user is already linked to a customer");
        }

        customer.setKeycloakUserId(request.getKeycloakUserId());

        Customer savedCustomer = customerRepository.save(customer);

        return toResponse(savedCustomer);
    }

    public CustomerResponse getMyCustomer(Jwt jwt) {

        String keycloakUserId = jwt.getSubject();

        Customer customer = customerRepository
                .findByKeycloakUserId(keycloakUserId)
                .orElseThrow(() ->
                        new CustomerNotFoundException("Customer not found"));

        return toResponse(customer);
    }
}