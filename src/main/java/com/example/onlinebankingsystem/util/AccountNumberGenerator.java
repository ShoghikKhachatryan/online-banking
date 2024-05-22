package com.example.onlinebankingsystem.util;

import java.util.Random;

public class AccountNumberGenerator {
    private static final int ACCOUNT_NUMBER_LENGTH = 12;

    public static String generateAccountNumber(){
        Random rand = new Random();
        StringBuilder accountNumber = new StringBuilder(ACCOUNT_NUMBER_LENGTH);

        // Generate the first two characters as random uppercase letters
        for (int i = 0; i < 2; i++) {
            char letter = (char) ('A' + rand.nextInt(26)); // generate a random letter (A-Z)
            accountNumber.append(letter);
        }

        // Generate a random 12-digit account number
        for (int i = 2; i < ACCOUNT_NUMBER_LENGTH; i++) {
            int digit = rand.nextInt(10); // generate a random digit (0-9)
            accountNumber.append(digit);
        }

        return accountNumber.toString();
    }
}
