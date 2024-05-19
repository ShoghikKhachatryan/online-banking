package com.example.onlinebankingsystem.service;

import com.example.onlinebankingsystem.exception.NotEnoughAmountException;
import com.example.onlinebankingsystem.exception.NotFoundException;
import com.example.onlinebankingsystem.repository.BankSystemRepository;
import com.example.onlinebankingsystem.model.Account;
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
        account.setAmount(BigDecimal.valueOf(1000));
    }

    @AfterEach
    void tearDown() {
       verifyNoMoreInteractions(mockedBankSystemRepository); // verify not call other methods
    }

    @Test
    void createAccount() {
        when(mockedBankSystemRepository.save(any(Account.class))).thenReturn(account);

        Account createdAccount = mockedBankSystemService.createAccount(account);

        assertEquals(account, createdAccount);
        verify(mockedBankSystemRepository).save(account);
    }

    @Test
    void onExistingAccountDepositWorksAsExpected() {
        when(mockedBankSystemRepository.findById(account.getId())).thenReturn(Optional.of(account));
        when(mockedBankSystemRepository.save(any(Account.class))).thenReturn(account);

        mockedBankSystemService.deposit(account.getId(), BigDecimal.valueOf(1000));

        assertEquals(account.getAmount(), BigDecimal.valueOf(2000));
        verify(mockedBankSystemRepository).save(account);
        verify(mockedBankSystemRepository).findById(account.getId());
    }

    @Test
    void onNonExistingAccountDepositThrowsException() {
        assertThrows(NotFoundException.class, () -> mockedBankSystemService.deposit(account.getId(), BigDecimal.valueOf(1000)));

        verify(mockedBankSystemRepository).findById(account.getId());
    }

    @Test
    void onExistingAccountWithdrawWorksAsExpected() {
        when(mockedBankSystemRepository.findById(account.getId())).thenReturn(Optional.of(account));
        when(mockedBankSystemRepository.save(any(Account.class))).thenReturn(account);

        mockedBankSystemService.withdraw(account.getId(), BigDecimal.valueOf(400));

        assertEquals(account.getAmount(), BigDecimal.valueOf(600));
        verify(mockedBankSystemRepository).save(account);
        verify(mockedBankSystemRepository).findById(account.getId());
    }

    @Test
    void onNonExistingAccountWithdrawThrowsException() {
        assertThrows(NotFoundException.class, () -> mockedBankSystemService.withdraw(account.getId(), BigDecimal.valueOf(400)));

        verify(mockedBankSystemRepository).findById(account.getId());
    }

    @Test
    void onNotEnoughAccountWithdrawThrowsException() {
        when(mockedBankSystemRepository.findById(account.getId())).thenReturn(Optional.of(account));

        assertThrows(NotEnoughAmountException.class, () -> mockedBankSystemService.withdraw(account.getId(), BigDecimal.valueOf(2000)));

        verify(mockedBankSystemRepository).findById(account.getId());
    }

    @Test
    void onExistingAccountTransferWorksAsExpected() {
        Account toAccount = new Account();
        toAccount.setId(2L);
        toAccount.setAmount(BigDecimal.valueOf(400));

        when(mockedBankSystemRepository.findById(account.getId())).thenReturn(Optional.of(account));
        when(mockedBankSystemRepository.findById(toAccount.getId())).thenReturn(Optional.of(toAccount));
        when(mockedBankSystemRepository.save(account)).thenReturn(account);
        when(mockedBankSystemRepository.save(toAccount)).thenReturn(toAccount);

        mockedBankSystemService.transfer(account.getId(), toAccount.getId(), BigDecimal.valueOf(400));

        assertEquals(account.getAmount(), BigDecimal.valueOf(600));
        assertEquals(toAccount.getAmount(), BigDecimal.valueOf(800));

        verify(mockedBankSystemRepository).save(account);
        verify(mockedBankSystemRepository).findById(account.getId());
        verify(mockedBankSystemRepository).save(toAccount);
        verify(mockedBankSystemRepository).findById(toAccount.getId());
    }

    @Test
    void onNonExistingToAccountTransferThrowsException() {
        Account toAccount = new Account();
        toAccount.setId(2L);
        toAccount.setAmount(BigDecimal.valueOf(400));

        when(mockedBankSystemRepository.findById(account.getId())).thenReturn(Optional.of(account));

        assertThrows(NotFoundException.class, () -> mockedBankSystemService.transfer(account.getId(), toAccount.getId(), BigDecimal.valueOf(400)));

        verify(mockedBankSystemRepository).findById(account.getId());
        verify(mockedBankSystemRepository).findById(toAccount.getId());
    }

    @Test
    void onNonExistingFromAccountTransferThrowsException() {
        Account toAccount = new Account();
        toAccount.setId(2L);
        toAccount.setAmount(BigDecimal.valueOf(400));

        Account fromAccount = new Account();
        fromAccount.setId(3L);
        fromAccount.setAmount(BigDecimal.valueOf(400));

        assertThrows(NotFoundException.class, () -> mockedBankSystemService.transfer(fromAccount.getId(), toAccount.getId(), BigDecimal.valueOf(400)));

        verify(mockedBankSystemRepository).findById(fromAccount.getId());
    }

    @Test
    void onNotEnoughAccountTransferThrowsException() {
        Account toAccount = new Account();
        toAccount.setId(2L);
        toAccount.setAmount(BigDecimal.valueOf(400));

        when(mockedBankSystemRepository.findById(toAccount.getId())).thenReturn(Optional.of(toAccount));
        when(mockedBankSystemRepository.findById(account.getId())).thenReturn(Optional.of(account));

        assertThrows(NotEnoughAmountException.class, () -> mockedBankSystemService.transfer(account.getId(), toAccount.getId(), BigDecimal.valueOf(10000)));

        verify(mockedBankSystemRepository).findById(account.getId());
        verify(mockedBankSystemRepository).findById(toAccount.getId());
    }
}