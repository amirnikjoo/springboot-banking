package com.amir.banking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Table(name = "bank_account")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String accountNumber;

    @Column(nullable = false)
    private String accountHolder;

    @Column(nullable = false)
    private double balance;

    @Version
    @JsonIgnore
    private int version;

    public BankAccount(String accountNumber, String accountHolder, double initialBalance) {
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.balance = initialBalance;
    }


    public double deposit(double amount) {
        this.balance += amount;
        return this.balance;
    }

    public double withdraw(double amount) {
        this.balance -= amount;
        return this.balance;
    }
}