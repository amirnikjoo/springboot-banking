package com.amir.banking;

import com.amir.banking.dto.TransactionInputDto;
import com.amir.banking.dto.TransactionOutputDto;
import lombok.SneakyThrows;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.amir.banking.util.AppConstants.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BankThreadSafeTest {
    private static final String pathPrefix = "/api/v1/account";

    final double amount = 1;
    final int numberOfThreads = 10;
    final int txnPerThread = 10;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    @Order(10)
    public void testThreadSafeDepositSuccess() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(new DepositTask(new TransactionInputDto(TEST_ACCOUNT_NO1, amount), txnPerThread));
        }

        executorService.shutdown();

        while (!executorService.isTerminated()) {
            System.out.println("wait to terminate...");
            Thread.sleep(500);
        }

        ResponseEntity<TransactionOutputDto> response = doBalance(TEST_ACCOUNT_NO1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getResponse().getResCode()).isEqualTo(RESPONSE_SUCCESS_VALUE);
        assertThat(response.getBody().getBalance()).isEqualTo(TEST_BALANCE_ACC1 + (numberOfThreads * txnPerThread));
    }

    @Test
    @Order(20)
    public void testThreadSafeWithdrawSuccess() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(new WithdrawTask(new TransactionInputDto(TEST_ACCOUNT_NO2, amount), txnPerThread));
        }

        executorService.shutdown();

        while (!executorService.isTerminated()) {
            System.out.println("wait to terminate...");
            Thread.sleep(500);
        }

        ResponseEntity<TransactionOutputDto> response = doBalance(TEST_ACCOUNT_NO2);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getResponse().getResCode()).isEqualTo(RESPONSE_SUCCESS_VALUE);
        assertThat(response.getBody().getBalance()).isEqualTo(TEST_BALANCE_ACC2 - (numberOfThreads * txnPerThread));
    }

    @Test
    @Order(30)
    public void testThreadSafeTransferSuccess() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(1);

        for (int i = 0; i < txnPerThread; i++) {
            executorService.submit(new TransferTask(new TransactionInputDto(TEST_ACCOUNT_NO3, TEST_ACCOUNT_NO4, amount), numberOfThreads));
        }

        executorService.shutdown();

        while (!executorService.isTerminated()) {
            System.out.println("wait to terminate...");
            Thread.sleep(500);
        }

        ResponseEntity<TransactionOutputDto> from = doBalance(TEST_ACCOUNT_NO3);
        assertThat(from.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(from.getBody()).isNotNull();
        assertThat(from.getBody().getResponse().getResCode()).isEqualTo(RESPONSE_SUCCESS_VALUE);
        assertThat(from.getBody().getBalance()).isEqualTo(TEST_BALANCE_ACC3 - (numberOfThreads * txnPerThread));

        ResponseEntity<TransactionOutputDto> to = doBalance(TEST_ACCOUNT_NO4);
        assertThat(to.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(to.getBody()).isNotNull();
        assertThat(to.getBody().getResponse().getResCode()).isEqualTo(RESPONSE_SUCCESS_VALUE);
        assertThat(to.getBody().getBalance()).isEqualTo(TEST_BALANCE_ACC4 + (numberOfThreads * txnPerThread));
    }

    private ResponseEntity<TransactionOutputDto> doDeposit(TransactionInputDto dto) {
        return testRestTemplate.postForEntity(pathPrefix + "/deposit", dto, TransactionOutputDto.class);
    }

    private ResponseEntity<TransactionOutputDto> doWithdraw(TransactionInputDto dto) {
        return testRestTemplate.postForEntity(pathPrefix + "/withdraw", dto, TransactionOutputDto.class);
    }

    private ResponseEntity<TransactionOutputDto> doTransfer(TransactionInputDto dto) {
        return testRestTemplate.postForEntity(pathPrefix + "/transfer", dto, TransactionOutputDto.class);
    }

    private ResponseEntity<TransactionOutputDto> doBalance(String accNo) {
        return testRestTemplate.getForEntity(pathPrefix + "/balance/" + accNo, TransactionOutputDto.class);
    }

    public class DepositTask implements Runnable {
        int txnPerThread;
        TransactionInputDto dto;

        public DepositTask(TransactionInputDto dto, int txnPerThread) {
            this.dto = dto;
            this.txnPerThread = txnPerThread;
        }

        @SneakyThrows
        @Override
        public void run() {
            for (int i = 0; i < txnPerThread; i++) {
                doDeposit(dto);
            }
        }
    }

    public class WithdrawTask implements Runnable {
        int txnPerThread;
        TransactionInputDto dto;

        public WithdrawTask(TransactionInputDto dto, int txnPerThread) {
            this.dto = dto;
            this.txnPerThread = txnPerThread;
        }

        @SneakyThrows
        @Override
        public void run() {
            for (int i = 0; i < txnPerThread; i++) {
                doWithdraw(dto);
            }
        }
    }

    public class TransferTask implements Runnable {
        int txnPerThread;
        TransactionInputDto dto;

        public TransferTask(TransactionInputDto dto, int txnPerThread) {
            this.dto = dto;
            this.txnPerThread = txnPerThread;
        }

        @SneakyThrows
        @Override
        public void run() {
            for (int i = 0; i < txnPerThread; i++) {
                doTransfer(dto);
            }
        }
    }
}