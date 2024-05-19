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
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private AccountType accountType;

    @Column(nullable = false)
    private BigDecimal amount;

    public Account(AccountType accountType, BigDecimal amount) {
        this.accountType = accountType;
        this.amount = amount;
    }
}
