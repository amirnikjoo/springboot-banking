package com.amir.banking.component;

import com.amir.banking.model.BankAccount;
import com.amir.banking.repository.BankAccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import static com.amir.banking.util.AppConstants.*;

@Component
@Order(1)
public class DataInitializer implements CommandLineRunner {

    private final BankAccountRepository bankAccountRepository;

    public DataInitializer(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    @Override
    public void run(String... args) throws InterruptedException {
        if (bankAccountRepository.count() == 0) {
            BankAccount bankAccount = new BankAccount(TEST_ACCOUNT_NO1, "Account1", TEST_BALANCE_ACC1);
            bankAccountRepository.save(bankAccount);
            System.out.println("Inserted bank account: " + bankAccount.getAccountNo());

            BankAccount bankAccount2 = new BankAccount(TEST_ACCOUNT_NO2, "Account2", TEST_BALANCE_ACC2);
            bankAccountRepository.save(bankAccount2);
            System.out.println("Inserted bank account: " + bankAccount2.getAccountNo());

            BankAccount bankAccount3 = new BankAccount(TEST_ACCOUNT_NO3, "Account3", TEST_BALANCE_ACC3);
            bankAccountRepository.save(bankAccount3);
            System.out.println("Inserted bank account: " + bankAccount3.getAccountNo());

            BankAccount bankAccount4 = new BankAccount(TEST_ACCOUNT_NO4, "Account4", TEST_BALANCE_ACC4);
            bankAccountRepository.save(bankAccount4);
            System.out.println("Inserted bank account: " + bankAccount4.getAccountNo());
        }
    }
}