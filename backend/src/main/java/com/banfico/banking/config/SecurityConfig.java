
package com.banfico.banking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())   //application is using a REST API with JWT bearer authentication rather than traditional server-side session/form authentication. For this API architecture, we'll disable CSRF.
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()                
                );

        return http.build();
    }
}

 //  .anyRequest().authenticated()  -> Allow every request (no aunthentication required)