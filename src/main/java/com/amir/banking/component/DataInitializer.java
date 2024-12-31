package com.amir.banking.component;

import com.amir.banking.dto.TransactionInputDto;
import com.amir.banking.model.BankAccount;
import com.amir.banking.repository.BankAccountRepository;
import com.amir.banking.service.BankService;
import com.amir.banking.util.StringUtil;
import lombok.SneakyThrows;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.amir.banking.util.AppConstants.*;

@Component
@Order(1)
public class DataInitializer implements CommandLineRunner {

    private final BankAccountRepository bankAccountRepository;
    private final BankService bankService;

    public DataInitializer(BankAccountRepository bankAccountRepository, BankService bankService) {
        this.bankAccountRepository = bankAccountRepository;
        this.bankService = bankService;
    }

    @Override
    public void run(String... args) throws InterruptedException {
        if (bankAccountRepository.count() == 0) {
            BankAccount bankAccount = new BankAccount(TEST_ACCOUNT_NO1, "Account1", TEST_BALANCE_ACC1);
            bankAccountRepository.save(bankAccount);
            System.out.println("Inserted bank account: " + bankAccount.getName());

            BankAccount bankAccount2 = new BankAccount(TEST_ACCOUNT_NO2, "Account2", TEST_BALANCE_ACC2);
            bankAccountRepository.save(bankAccount2);
            System.out.println("Inserted bank account: " + bankAccount2.getName());

            BankAccount bankAccount3 = new BankAccount(TEST_ACCOUNT_NO3, "Account3", TEST_BALANCE_ACC3);
            bankAccountRepository.save(bankAccount3);
            System.out.println("Inserted bank account: " + bankAccount3.getName());

            BankAccount bankAccount4 = new BankAccount(TEST_ACCOUNT_NO4, "Account4", TEST_BALANCE_ACC4);
            bankAccountRepository.save(bankAccount4);
            System.out.println("Inserted bank account: " + bankAccount4.getName());
        }
    }
}