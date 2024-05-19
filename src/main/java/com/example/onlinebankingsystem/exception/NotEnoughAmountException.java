package com.example.onlinebankingsystem.exception;

import java.math.BigDecimal;

public class NotEnoughAmountException extends RuntimeException {
    public NotEnoughAmountException(BigDecimal amount) {
        super("There is amount is less then " + amount);
    }
}
