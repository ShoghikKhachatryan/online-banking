package com.example.onlinebankingsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * This class we use for configure rule of authorizeHttpRequests, login and logout sections
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, UserIdSessionAttributeHandler userIdSessionAttributeHandler) throws Exception {

        http
                // Configuring URL access rules
                .authorizeHttpRequests(
                        authorizeRequests -> authorizeRequests
                                .requestMatchers("/signup", "/login").permitAll() // Allow access to signup and login pages without authentication
                                .anyRequest().authenticated() // All other requests require authentication
                )

                // Configuring form login
                .formLogin(
                        formLogin -> formLogin
                                .loginPage("/login") // Custom login page URL
                                .permitAll() // Allow everyone to access the login page
                                .successHandler(userIdSessionAttributeHandler) // Custom success handler
                )

                // Configuring logout
                .logout(
                        logout -> logout
                                .logoutUrl("/logout") // Custom logout URL
                                .logoutSuccessUrl("/login?logout") // Redirect to login page with logout parameter on successful logout
                                .deleteCookies("JSESSIONID") // Delete session cookie on logout
                                .invalidateHttpSession(true) // Invalidate the HTTP session on logout
                                .permitAll() // Allow everyone to access the logout URL
                );

        return http.build();
    }
}
