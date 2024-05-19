package com.example.onlinebankingsystem.service;

import com.example.onlinebankingsystem.exception.NotEnoughAmountException;
import com.example.onlinebankingsystem.exception.NotFoundException;
import com.example.onlinebankingsystem.model.Account;
import com.example.onlinebankingsystem.repository.BankSystemRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BankSystemService {

    private final BankSystemRepository bankSystemRepository;

    public BankSystemService(BankSystemRepository bankSystemRepository) {
        this.bankSystemRepository = bankSystemRepository;
    }

    @Transactional
    public Account createAccount(Account account) {
        return bankSystemRepository.save(account);
    }

    @Transactional
    @Modifying
    public void deposit(Long accountId, BigDecimal amount) {
        Account account = bankSystemRepository.findById(accountId).orElseThrow(() -> new NotFoundException(accountId));
        account.setAmount(account.getAmount().add(amount));
        bankSystemRepository.save(account);
    }

    @Transactional
    @Modifying
    public void withdraw(Long accountId, BigDecimal amount) {
        Account account = bankSystemRepository.findById(accountId).orElseThrow(() -> new NotFoundException(accountId));
        if (account.getAmount().compareTo(amount) < 0) {
            throw new NotEnoughAmountException(amount);
        }

        account.setAmount(account.getAmount().subtract(amount));
        bankSystemRepository.save(account);
    }

    @Transactional
    @Modifying
    public void transfer(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        Account fromAccount = bankSystemRepository.findById(fromAccountId).orElseThrow(() -> new NotFoundException(fromAccountId));
        Account toAccount = bankSystemRepository.findById(toAccountId).orElseThrow(() -> new NotFoundException(toAccountId));

        if (fromAccount.getAmount().compareTo(amount) < 0) {
            throw new NotEnoughAmountException(amount);
        }

        toAccount.setAmount(toAccount.getAmount().add(amount));
        fromAccount.setAmount(fromAccount.getAmount().subtract(amount));
        bankSystemRepository.save(toAccount);
        bankSystemRepository.save(fromAccount);
    }
}
