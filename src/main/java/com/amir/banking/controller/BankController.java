package com.amir.banking.controller;

import com.amir.banking.component.TransactionLogger;
import com.amir.banking.dto.TransactionDto;
import com.amir.banking.model.BankAccount;
import com.amir.banking.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.cloud.sleuth.Tracer;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/account")
public class BankController {
    private final BankService bankService;
    private final Tracer tracer;
    private final TransactionLogger logger;

    @Autowired
    public BankController(BankService bankService, Tracer tracer, TransactionLogger transactionLogger) {
        this.bankService = bankService;
        this.tracer = tracer;
        this.logger = transactionLogger;
    }

    @PostMapping("/create")
    public ResponseEntity<BankAccount> createAccount(@RequestBody TransactionDto dto) throws Exception {
        String traceId = Objects.requireNonNull(tracer.currentSpan()).context().traceId();
        logger.onTransaction(traceId, "createAccount called with transaction: " + dto);
        return ResponseEntity.ok(bankService.createAccount(traceId, dto));
    }

    @PostMapping("/deposit")
    public ResponseEntity<Double> deposit(@RequestBody TransactionDto dto) throws Exception {
        String traceId = Objects.requireNonNull(tracer.currentSpan()).context().traceId();
        logger.onTransaction(traceId, "deposit called with transaction: " + dto);
        return ResponseEntity.ok(bankService.doDeposit(traceId, dto));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Double> withdraw(@RequestBody TransactionDto dto) throws Exception {
        String traceId = Objects.requireNonNull(tracer.currentSpan()).context().traceId();
        logger.onTransaction(traceId, "withdraw called with transaction: " + dto);
        return ResponseEntity.ok(bankService.doWithdraw(traceId, dto));
    }

    @PostMapping("/transfer")
    public ResponseEntity<Double> transfer(@RequestBody TransactionDto dto) throws Exception {
        String traceId = Objects.requireNonNull(tracer.currentSpan()).context().traceId();
        logger.onTransaction(traceId, "transfer called with transaction: " + dto);
        return ResponseEntity.ok(bankService.doTransfer(traceId, dto));
    }

    @GetMapping("/balance/{accountNumber}")
    public ResponseEntity<Double> getBalance(@PathVariable String accountNumber) throws Exception {
        String traceId = Objects.requireNonNull(tracer.currentSpan()).context().traceId();
        TransactionDto dto = new TransactionDto(accountNumber);
        logger.onTransaction(traceId, "getBalance called with transaction: " + dto);
        double balance = bankService.getBalance(traceId, dto);
        return ResponseEntity.ok(balance);
    }
}