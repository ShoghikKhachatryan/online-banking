package com.example.onlinebankingsystem.exception;

public class AlreadyUserExistException extends RuntimeException {
    public AlreadyUserExistException(String username) {
        super("The username " + username + "already exist.");
    }
}
