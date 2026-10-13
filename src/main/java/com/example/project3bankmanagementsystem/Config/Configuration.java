package com.example.project3bankmanagementsystem.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@org.springframework.context.annotation.Configuration
public class Configuration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .authorizeHttpRequests(auth -> auth
                        // ===== Auth =====
                        .requestMatchers("/api/v1/auth/register").permitAll()
                        .requestMatchers("/api/v1/auth/register-employee",
                                "/api/v1/auth/get",
                                "/api/v1/auth/delete/**").hasAuthority("ADMIN")

                        // ===== Customer =====
                        .requestMatchers("/api/v1/customer/get").hasAnyAuthority("EMPLOYEE", "ADMIN")
                        .requestMatchers("/api/v1/customer/get/my",
                                "/api/v1/customer/update",
                                "/api/v1/customer/delete").hasAuthority("CUSTOMER")

                        // ===== Employee =====
                        .requestMatchers("/api/v1/employee/get/my").hasAuthority("EMPLOYEE")
                        .requestMatchers("/api/v1/employee/get",
                                "/api/v1/employee/update/**",
                                "/api/v1/employee/delete/**").hasAuthority("ADMIN")

                        // ===== Account =====
                        .requestMatchers("/api/v1/account/get",
                                "/api/v1/account/activate/**",
                                "/api/v1/account/block/**").hasAnyAuthority("EMPLOYEE", "ADMIN")
                        .requestMatchers("/api/v1/account/get/**",
                                "/api/v1/account/add",
                                "/api/v1/account/deposit/**",
                                "/api/v1/account/withdraw/**",
                                "/api/v1/account/transfer/**").hasAuthority("CUSTOMER")

                        .anyRequest().authenticated()
                )
                .logout(logout -> logout
                        .logoutUrl("/api/v1/auth/logout")
                        .invalidateHttpSession(true)
                )
                .httpBasic(basic -> {})
                .build();
    }
}