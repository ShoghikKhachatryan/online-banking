package com.example.onlinebankingsystem.model;

import lombok.Getter;

/**
 * Account type can be Checking or Saving.
 * */
@Getter
public enum AccountType {
    CHECKING("Checking"),
    SAVING("Saving");

    private final String accountType;

    AccountType(String accountType) {
        this.accountType = accountType;
    }
}
