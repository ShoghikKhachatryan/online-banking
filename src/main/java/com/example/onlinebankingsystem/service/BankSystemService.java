package com.example.onlinebankingsystem.service;

import com.example.onlinebankingsystem.model.Account;
import com.example.onlinebankingsystem.repository.BankSystemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankSystemService {

    @Autowired
    private BankSystemRepository bankSystemRepository;

    //save account
    public Account saveAccount(Account account) {
        return bankSystemRepository.save(account);
    }
}
