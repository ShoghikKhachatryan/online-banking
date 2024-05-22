package com.example.onlinebankingsystem.service;

import com.example.onlinebankingsystem.exception.NotEnoughAmountException;
import com.example.onlinebankingsystem.exception.NotFoundException;
import com.example.onlinebankingsystem.model.Account;
import com.example.onlinebankingsystem.model.AccountType;
import com.example.onlinebankingsystem.repository.BankSystemRepository;
import com.example.onlinebankingsystem.util.AccountNumberGenerator;
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
        account.setAccountNumber(AccountNumberGenerator.generateAccountNumber());
        System.out.println("\n\n for save account." + account);
        return bankSystemRepository.save(account);
    }

    @Transactional
    @Modifying
    public void openNewAccount(AccountType accountType, BigDecimal initialDeposit) {
        Account account = new Account();
        account.setAccountNumber(AccountNumberGenerator.generateAccountNumber());
        account.setAccountType(accountType);
        account.setBalance(initialDeposit);
        System.out.println("\n\n for save account." + account);
        bankSystemRepository.save(account);
    }

    @Transactional
    @Modifying
    public void deposit(String accountNumber, BigDecimal amount) {
        Account account = bankSystemRepository.findByAccountNumber(accountNumber).orElseThrow(() -> new NotFoundException(accountNumber));
        account.setBalance(account.getBalance().add(amount));

        bankSystemRepository.save(account);
    }

    @Transactional
    @Modifying
    public void withdraw(String accountNumber, BigDecimal amount) {
        Account account = bankSystemRepository.findByAccountNumber(accountNumber).orElseThrow(() -> new NotFoundException(accountNumber));

        if (account.getBalance().compareTo(amount) < 0) {
            throw new NotEnoughAmountException(amount);
        }

        account.setBalance(account.getBalance().subtract(amount));
        bankSystemRepository.save(account);
    }

    @Transactional
    @Modifying
    public void transfer(String fromAccountNumber, String toAccountNumber, BigDecimal amount) {
        withdraw(fromAccountNumber, amount);
        deposit(toAccountNumber, amount);
    }
}
