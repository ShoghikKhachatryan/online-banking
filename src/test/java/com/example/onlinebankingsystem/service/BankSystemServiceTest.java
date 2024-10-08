package com.example.onlinebankingsystem.service;

import com.example.onlinebankingsystem.exception.NotEnoughAmountException;
import com.example.onlinebankingsystem.exception.NotFoundException;
import com.example.onlinebankingsystem.repository.BankSystemRepository;
import com.example.onlinebankingsystem.model.Account;
import com.example.onlinebankingsystem.util.AccountNumberGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BankSystemServiceTest {

    @Mock
    private BankSystemRepository mockedBankSystemRepository;

    @InjectMocks
    private BankSystemService mockedBankSystemService;

    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setId(1L);
        account.setBalance(BigDecimal.valueOf(1000));
        account.setAccountNumber(AccountNumberGenerator.generateAccountNumber());
    }

    @AfterEach
    void tearDown() {
       verifyNoMoreInteractions(mockedBankSystemRepository); // verify not call other methods
    }

    @Test
    void openNewAccount() {
        when(mockedBankSystemRepository.save(any(Account.class))).thenReturn(account);

        mockedBankSystemService.openNewAccount(account);

        verify(mockedBankSystemRepository).save(account);
    }

    @Test
    void onExistingAccountDepositWorksAsExpected() {
        when(mockedBankSystemRepository.findByAccountNumber(account.getAccountNumber())).thenReturn(Optional.of(account));
        when(mockedBankSystemRepository.save(any(Account.class))).thenReturn(account);

        mockedBankSystemService.deposit(account.getAccountNumber(), BigDecimal.valueOf(1000));

        assertEquals(account.getBalance(), BigDecimal.valueOf(2000));
        verify(mockedBankSystemRepository).save(account);
        verify(mockedBankSystemRepository).findByAccountNumber(account.getAccountNumber());
    }

    @Test
    void onNonExistingAccountDepositThrowsException() {
        assertThrows(NotFoundException.class, () -> mockedBankSystemService.deposit(account.getAccountNumber(), BigDecimal.valueOf(1000)));

        verify(mockedBankSystemRepository).findByAccountNumber(account.getAccountNumber());
    }

    @Test
    void onExistingAccountWithdrawWorksAsExpected() {
        when(mockedBankSystemRepository.findByAccountNumber(account.getAccountNumber())).thenReturn(Optional.of(account));
        when(mockedBankSystemRepository.save(any(Account.class))).thenReturn(account);

        mockedBankSystemService.withdraw(account.getAccountNumber(), BigDecimal.valueOf(400));

        assertEquals(account.getBalance(), BigDecimal.valueOf(600));
        verify(mockedBankSystemRepository).save(account);
        verify(mockedBankSystemRepository).findByAccountNumber(account.getAccountNumber());
    }

    @Test
    void onNonExistingAccountWithdrawThrowsException() {
        assertThrows(NotFoundException.class, () -> mockedBankSystemService.withdraw(account.getAccountNumber(), BigDecimal.valueOf(400)));

        verify(mockedBankSystemRepository).findByAccountNumber(account.getAccountNumber());
    }

    @Test
    void onNotEnoughAccountWithdrawThrowsException() {
        when(mockedBankSystemRepository.findByAccountNumber(account.getAccountNumber())).thenReturn(Optional.of(account));

        assertThrows(NotEnoughAmountException.class, () -> mockedBankSystemService.withdraw(account.getAccountNumber(), BigDecimal.valueOf(2000)));

        verify(mockedBankSystemRepository).findByAccountNumber(account.getAccountNumber());
    }

    @Test
    void onExistingAccountTransferWorksAsExpected() {
        Account toAccount = new Account();
        toAccount.setId(2L);
        toAccount.setBalance(BigDecimal.valueOf(400));
        toAccount.setAccountNumber(AccountNumberGenerator.generateAccountNumber());

        when(mockedBankSystemRepository.findByAccountNumber(account.getAccountNumber())).thenReturn(Optional.of(account));
        when(mockedBankSystemRepository.findByAccountNumber(toAccount.getAccountNumber())).thenReturn(Optional.of(toAccount));
        when(mockedBankSystemRepository.save(account)).thenReturn(account);
        when(mockedBankSystemRepository.save(toAccount)).thenReturn(toAccount);

        mockedBankSystemService.transfer(account.getAccountNumber(), toAccount.getAccountNumber(), BigDecimal.valueOf(400));

        assertEquals(account.getBalance(), BigDecimal.valueOf(600));
        assertEquals(toAccount.getBalance(), BigDecimal.valueOf(800));

        verify(mockedBankSystemRepository).save(account);
        verify(mockedBankSystemRepository).findByAccountNumber(account.getAccountNumber());
        verify(mockedBankSystemRepository).save(toAccount);
        verify(mockedBankSystemRepository).findByAccountNumber(toAccount.getAccountNumber());
    }

    @Test
    void onNonExistingToAccountTransferThrowsException() {
        Account toAccount = new Account();
        toAccount.setId(2L);
        toAccount.setBalance(BigDecimal.valueOf(400));
        toAccount.setAccountNumber(AccountNumberGenerator.generateAccountNumber());

        when(mockedBankSystemRepository.findByAccountNumber(account.getAccountNumber())).thenReturn(Optional.of(account));
        when(mockedBankSystemRepository.save(account)).thenReturn(account);


        assertThrows(NotFoundException.class, () -> mockedBankSystemService.transfer(account.getAccountNumber(), toAccount.getAccountNumber(), BigDecimal.valueOf(400)));

        verify(mockedBankSystemRepository).findByAccountNumber(account.getAccountNumber());
        verify(mockedBankSystemRepository).save(account);
        verify(mockedBankSystemRepository).findByAccountNumber(toAccount.getAccountNumber());
    }

    @Test
    void onNonExistingFromAccountTransferThrowsException() {
        Account toAccount = new Account();
        toAccount.setId(2L);
        toAccount.setBalance(BigDecimal.valueOf(400));
        toAccount.setAccountNumber(AccountNumberGenerator.generateAccountNumber());

        assertThrows(NotFoundException.class, () -> mockedBankSystemService.transfer(account.getAccountNumber(), toAccount.getAccountNumber(), BigDecimal.valueOf(400)));

        verify(mockedBankSystemRepository).findByAccountNumber(account.getAccountNumber());
    }

    @Test
    void onNotEnoughAccountTransferThrowsException() {
        Account toAccount = new Account();
        toAccount.setId(2L);
        toAccount.setBalance(BigDecimal.valueOf(400));
        toAccount.setAccountNumber(AccountNumberGenerator.generateAccountNumber());


        when(mockedBankSystemRepository.findByAccountNumber(account.getAccountNumber())).thenReturn(Optional.of(account));

        assertThrows(NotEnoughAmountException.class, () -> mockedBankSystemService.transfer(account.getAccountNumber(), toAccount.getAccountNumber(), BigDecimal.valueOf(10000)));

        verify(mockedBankSystemRepository).findByAccountNumber(account.getAccountNumber());

    }
}