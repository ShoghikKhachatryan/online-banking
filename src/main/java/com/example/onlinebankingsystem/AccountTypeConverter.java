package com.example.onlinebankingsystem;

import com.example.onlinebankingsystem.model.AccountType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;
//This cass using cor cover enu
@Converter(autoApply = true)
public class AccountTypeConverter implements AttributeConverter<AccountType, String> {
    @Override
    public String convertToDatabaseColumn(AccountType accountType) {
        if (accountType == null) {
            return null;
        }
        return accountType.getAccountType();
    }

    @Override
    public AccountType convertToEntityAttribute(String accountType) {
        if (accountType == null) {
            return null;
        }

        return Stream.of(AccountType.values())
                .filter(c -> c.getAccountType().equals(accountType))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
