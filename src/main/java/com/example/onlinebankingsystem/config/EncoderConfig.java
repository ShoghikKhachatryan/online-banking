package com.example.onlinebankingsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


/**
 * This class configures password encoding for the application.
 * It creates a BCryptPasswordEncoder bean, which is used to hash passwords.
 * BCrypt makes passwords harder to crack by making the hashing process slow.
 */
@Configuration
public class EncoderConfig {

    /**
     * Defines a bean for BCryptPasswordEncoder.
     *
     * @return a BCryptPasswordEncoder instance used for password hashing
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
