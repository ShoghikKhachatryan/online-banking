package com.example.onlinebankingsystem.repository;

import com.example.onlinebankingsystem.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankSystemRepository extends JpaRepository<Account, Long> {
    // Find Account giving accountNumber
    @Query("SELECT a FROM Account a WHERE a.accountNumber > :accountNumber")
    default Optional<Account> findByAccountNumber(String accountNumber) {
        return Optional.empty();
    }
}
