
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
                .csrf(csrf -> csrf.disable()) // application is using a REST API with JWT bearer authentication rather
                                              // than traditional server-side session/form authentication. For this API
                                              // architecture, we'll disable CSRF.
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt->{}));   // means now our spring appn is a resource server and requires jwt token to access the endpoints

        return http.build();
    }
}

// .anyRequest().authenticated() -> Allow every request (no aunthentication
// required)