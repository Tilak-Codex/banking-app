
package com.banfico.banking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.http.HttpMethod;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable()) // application is using a REST API with JWT bearer authentication rather
                                              // than traditional server-side session/form authentication. For this API
                                              // architecture, we'll disable CSRF.
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/accounts")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/transactions")
                        .hasRole("MAKER")
                        .requestMatchers(HttpMethod.DELETE, "/api/beneficiaries/{id}")
                        .hasAnyRole("ADMIN", "CHECKER")
                        .anyRequest().authenticated())

                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(
                        new KeycloakJwtAuthenticationConverter()))); // means now our spring appn is a resource

        return http.build();
    }
}

// .anyRequest().authenticated() -> DisAllow every request (need aunthentication
// required)