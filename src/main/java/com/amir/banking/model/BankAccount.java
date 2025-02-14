package com.amir.banking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Table(name = "bank_account")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String accountNo;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private double balance;

    public BankAccount(String accountNo, String name, double initialBalance) {
        this.accountNo = accountNo;
        this.name = name;
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