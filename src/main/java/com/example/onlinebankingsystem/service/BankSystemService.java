package com.example.onlinebankingsystem.service;

import com.example.onlinebankingsystem.exception.NotEnoughAmountException;
import com.example.onlinebankingsystem.exception.NotFoundException;
import com.example.onlinebankingsystem.model.Account;
import com.example.onlinebankingsystem.model.AccountType;
import com.example.onlinebankingsystem.model.User;
import com.example.onlinebankingsystem.repository.AccountRepository;
import com.example.onlinebankingsystem.repository.UserRepository;
import com.example.onlinebankingsystem.util.AccountNumberGenerator;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BankSystemService {

    private final AccountRepository accountRepository;

    private final UserRepository userRepository;

    public BankSystemService(UserRepository userRepository, AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @Modifying
    public void openNewAccount(Account account, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User Not Found"));
        account.setAccountNumber(AccountNumberGenerator.generateAccountNumber());
        account.setUser(user);
        accountRepository.save(account);
    }


    @Transactional
    @Modifying
    public void deposit(String accountNumber, BigDecimal amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(() -> new NotFoundException(accountNumber));
        account.setBalance(account.getBalance().add(amount));

        accountRepository.save(account);
    }

    @Transactional
    @Modifying
    public void withdraw(String accountNumber, BigDecimal amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(() -> new NotFoundException(accountNumber));

        if (account.getBalance().compareTo(amount) < 0) {
            throw new NotEnoughAmountException(amount);
        }

        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);
    }

    @Transactional
    @Modifying
    public void transfer(String fromAccountNumber, String toAccountNumber, BigDecimal amount) {
        withdraw(fromAccountNumber, amount);
        deposit(toAccountNumber, amount);
    }
}
