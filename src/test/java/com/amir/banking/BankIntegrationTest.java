package com.amir.banking;

import com.amir.banking.dto.ResponseDto;
import com.amir.banking.dto.TransactionInputDto;
import com.amir.banking.model.BankAccount;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.amir.banking.util.BankingConstants.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BankIntegrationTest {
    private static final String pathPrefix = "/api/v1/account";
    private static final String createAccountNo1 = "111";
    private static final Double initBalanceAcc1 = 100.0;

    private static final String createAccountNo2 = "222";
    private static final Double initBalanceAcc2 = 100.0;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    @Order(10)
    public void testCreateUserSuccess() {
        TransactionInputDto dto1 = new TransactionInputDto(createAccountNo1, "", "Omid", initBalanceAcc1);
        ResponseEntity<BankAccount> response1 = testRestTemplate.postForEntity(pathPrefix + "/create", dto1, BankAccount.class);
        assertThat(response1.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response1.getBody()).isNotNull();
        assertThat(response1.getBody().getName()).isEqualTo("Omid");
        assertThat(response1.getBody().getBalance()).isEqualTo(initBalanceAcc1);

        TransactionInputDto dto2 = new TransactionInputDto(createAccountNo2, "", "Saeed", initBalanceAcc2);
        ResponseEntity<BankAccount> response2 = testRestTemplate.postForEntity(pathPrefix + "/create", dto2, BankAccount.class);
        assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response2.getBody()).isNotNull();
        assertThat(response2.getBody().getName()).isEqualTo("Saeed");
        assertThat(response2.getBody().getBalance()).isEqualTo(initBalanceAcc2);
    }

    @Test
    @Order(15)
    public void testCreateUserFailDuplicateAccountNo() {
        TransactionInputDto dto = new TransactionInputDto(createAccountNo1, "", "Omid", initBalanceAcc1);
        ResponseEntity<ResponseDto> response = testRestTemplate.postForEntity(pathPrefix + "/create", dto, ResponseDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getResCode()).isEqualTo("002");

    }

    @Test
    @Order(20)
    public void testBalanceSuccess() {
        ResponseEntity<Double> response = doBalance(createAccountNo1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(initBalanceAcc1);
    }

    private ResponseEntity<Double> doBalance(String accNo) {
        ResponseEntity<Double> response = testRestTemplate.getForEntity(pathPrefix + "/balance/" + accNo, Double.class, Double.class);
        return response;
    }

    @Test
    @Order(30)
    public void testDepositSuccess() {
        double amount = 1;

        ResponseEntity<Double> response = testRestTemplate.postForEntity(
                pathPrefix + "/deposit", new TransactionInputDto(createAccountNo1, amount), Double.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(initBalanceAcc1 + amount);
    }

    @Test
    @Order(40)
    public void testWithdrawSuccess() {
        double amount = 1;

        ResponseEntity<Double> response = testRestTemplate.postForEntity(
                pathPrefix + "/withdraw", new TransactionInputDto(createAccountNo1, amount), Double.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(initBalanceAcc1);
    }

    @Test
    @Order(50)
    public void testTransferSuccess1() {
        double amount = 1;

        TransactionInputDto dto = new TransactionInputDto(createAccountNo1, createAccountNo2, null, amount);

        ResponseEntity<Double> response = testRestTemplate.postForEntity(pathPrefix + "/transfer", dto, Double.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo(initBalanceAcc1 - amount);

        ResponseEntity<Double> acc2Balance = doBalance(createAccountNo2);
        assertThat(acc2Balance.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(acc2Balance.getBody()).isNotNull();
        assertThat(acc2Balance.getBody()).isEqualTo(initBalanceAcc2 + amount);
    }

    @Test
    @Order(60)
    public void testTransferSuccess2() {
        double amount = 1;

        TransactionInputDto dto = new TransactionInputDto(TEST_ACCOUNT_NO1, TEST_ACCOUNT_NO2, null, amount);
        ResponseEntity<Double> response = testRestTemplate.postForEntity(pathPrefix + "/transfer", dto, Double.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo(TEST_INIT_BALANCE_ACC1 - amount);

        ResponseEntity<Double> acc2Balance = doBalance(TEST_ACCOUNT_NO2);
        assertThat(acc2Balance.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(acc2Balance.getBody()).isNotNull();
        assertThat(acc2Balance.getBody()).isEqualTo(TEST_INIT_BALANCE_ACC2 + amount);
    }

    @Test
    @Order(60)
    public void testTransferFailInsufficientFund() {
        double amount = 10000;

        TransactionInputDto dto = new TransactionInputDto(createAccountNo2, createAccountNo1, null, amount);

        ResponseEntity<ResponseDto> response = testRestTemplate.postForEntity(pathPrefix + "/transfer", dto, ResponseDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getResCode()).isEqualTo("201");
    }
}