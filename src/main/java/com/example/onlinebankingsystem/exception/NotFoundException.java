package com.example.onlinebankingsystem.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(Long accountNumber) {
        super("Could not find account " + accountNumber);
    }
}
