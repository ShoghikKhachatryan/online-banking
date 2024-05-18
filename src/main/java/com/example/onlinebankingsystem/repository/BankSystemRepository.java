package com.example.onlinebankingsystem.repository;

import com.example.onlinebankingsystem.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankSystemRepository extends JpaRepository<Account, Long> {
}
