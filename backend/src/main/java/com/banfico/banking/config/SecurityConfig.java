
package com.banfico.banking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())

                .authorizeHttpRequests(auth -> auth

                        // Actuator endpoints are publicly accessible
                        .requestMatchers("/actuator/**")
                        .permitAll()

                        // Only ADMIN can create bank accounts
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/accounts")
                        .hasRole("ADMIN")

                        // Only ADMIN can delete bank accounts
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/accounts/*")
                        .hasRole("ADMIN")

                        // Only ADMIN can delete customers
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/customers/*")
                        .hasRole("ADMIN")

                        // Only ADMIN can unlink a bank account
                        // from a customer
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/customers/*/accounts/*")
                        .hasRole("ADMIN")

                        // Only MAKER can create transactions
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/transactions")
                        .hasRole("MAKER")

                        // Only MAKER can make transfers
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/transactions/transfer")
                        .hasRole("MAKER")

                        // Only ADMIN can create beneficiaries
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/beneficiaries/customers/*")
                        .hasRole("ADMIN")

                        // ADMIN and CHECKER can delete beneficiaries
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/beneficiaries/*")
                        .hasAnyRole("ADMIN", "CHECKER")

                        // Only ADMIN can link a Keycloak user
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/customers/*/keycloak-user")
                        .hasRole("ADMIN")

                        // Every other request requires authentication
                        .anyRequest()
                        .authenticated()
                )

                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(
                                        new KeycloakJwtAuthenticationConverter())));

        return http.build();
    }
}