package com.example.onlinebankingsystem.service;

import com.example.onlinebankingsystem.model.Account;
import com.example.onlinebankingsystem.repository.BankSystemRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BankSystemService {

    @Autowired
    private BankSystemRepository bankSystemRepository;

    //save account
    // TODO I think here we don't need transactional
    public Account saveAccount(Account account) {
        return bankSystemRepository.save(account);
    }

    @Transactional
    @Modifying
    public void deposit(Long accountId, BigDecimal amount) {
        Account account = bankSystemRepository.findById(accountId).orElseThrow(() -> new RuntimeException("Account not found"));
        account.setAmount(account.getAmount().add(amount));
        bankSystemRepository.save(account);
    }

    @Transactional
    @Modifying
    public boolean withdraw(Long accountId, BigDecimal amount) {
        Account account = bankSystemRepository.findById(accountId).orElseThrow(() -> new RuntimeException("Account not found"));
        if(account.getAmount().compareTo(amount) < 0) {
            return false;
        }

        account.setAmount(account.getAmount().subtract(amount));
        bankSystemRepository.save(account);

        return true;
    }
}
