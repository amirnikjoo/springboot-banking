package com.amir.banking.component;

import com.amir.banking.dto.TransactionDto;
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

import static com.amir.banking.util.BankingConstants.*;

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
            BankAccount bankAccount = new BankAccount(TEST_ACCOUNT_NO1, "Amir", TEST_INIT_BALANCE_ACC1);
            bankAccountRepository.save(bankAccount);
            System.out.println("Inserted bank account: " + bankAccount.getAccountHolder());

            BankAccount bankAccount2 = new BankAccount(TEST_ACCOUNT_NO2, "Sepehr", TEST_INIT_BALANCE_ACC2);
            bankAccountRepository.save(bankAccount2);
            System.out.println("Inserted bank account: " + bankAccount2.getAccountHolder());
        }

        for (int i = 0; i < 5; i++) {
            System.out.println("i = " + i);
//            doConcurrentTest(TEST_ACCOUNT_NO1, TEST_ACCOUNT_NO2);
        }

    }

    @SneakyThrows
    private void doConcurrentTest(String accNo1, String accNo2) throws InterruptedException {
        Thread.sleep(1000);
        System.out.println("start task with threads");

        int numberOfThreads = 10;
        int txnsPerThread = 10;
        double depositAmount = 1;

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(new DepositTask(new TransactionDto(accNo1, depositAmount), txnsPerThread));
//            executorService.submit(new WithdrawTask(new TransactionDto(accNo1, depositAmount), txnsPerThread));
//            executorService.submit(new TransferTask(new TransactionDto(accNo1, accNo2, depositAmount), txnsPerThread));
        }

        executorService.shutdown();

        while (!executorService.isTerminated()) {
            System.out.println("wait to terminate...");
            Thread.sleep(1000);
        }
        System.out.println("finished task with threads");
        System.out.println("final balance accNo1 is: " + bankService.getBalance(StringUtil.generateRandomTraceId(TEST_TRACE_ID_LEN), new TransactionDto(accNo1)));
        System.out.println("final balance accNo2 is: " + bankService.getBalance(StringUtil.generateRandomTraceId(TEST_TRACE_ID_LEN), new TransactionDto(accNo2)));
    }

    public class DepositTask implements Runnable {
        int txnsPerThread;
        TransactionDto dto;

        public DepositTask(TransactionDto dto, int txnsPerThread) {
            this.dto = dto;
            this.txnsPerThread = txnsPerThread;
        }

        @SneakyThrows
        @Override
        public void run() {
            for (int i = 0; i < txnsPerThread; i++) {
                bankService.doDeposit(StringUtil.generateRandomTraceId(TEST_TRACE_ID_LEN), dto);
            }
        }
    }

    public class WithdrawTask implements Runnable {
        int txnsPerThread;
        TransactionDto dto;

        public WithdrawTask(TransactionDto dto, int txnsPerThread) {
            this.dto = dto;
            this.txnsPerThread = txnsPerThread;
        }

        @SneakyThrows
        @Override
        public void run() {
            for (int i = 0; i < txnsPerThread; i++) {
                bankService.doWithdraw(StringUtil.generateRandomTraceId(TEST_TRACE_ID_LEN), dto);
            }
        }
    }

    public class TransferTask implements Runnable {
        int txnsPerThread;
        TransactionDto dto;

        public TransferTask(TransactionDto dto, int txnsPerThread) {
            this.dto = dto;
            this.txnsPerThread = txnsPerThread;
        }

        @SneakyThrows
        @Override
        public void run() {
            for (int i = 0; i < txnsPerThread; i++) {
                bankService.doTransfer(StringUtil.generateRandomTraceId(TEST_TRACE_ID_LEN), dto);
            }
        }
    }
}