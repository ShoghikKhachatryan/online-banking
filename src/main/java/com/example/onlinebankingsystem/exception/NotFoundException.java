package com.example.onlinebankingsystem.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String accountNumber) {
        super("Could not find account " + accountNumber);
    }
}
