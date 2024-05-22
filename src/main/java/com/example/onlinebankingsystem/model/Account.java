package com.example.onlinebankingsystem.model;

import jakarta.persistence.*;
import lombok.*;


import java.math.BigDecimal;

@Entity
@Table(name = "account")
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    // provided by the bank system
    @Column(unique = true, nullable = false)
    private String accountNumber;

    @Column(nullable = false)
    private BigDecimal balance;

    public Account(AccountType accountType, BigDecimal balance) {
        this.accountType = accountType;
        this.balance = balance;
    }
}
