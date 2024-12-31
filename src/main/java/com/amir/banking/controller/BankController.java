package com.amir.banking.controller;

import com.amir.banking.component.TransactionLogger;
import com.amir.banking.dto.ResponseDto;
import com.amir.banking.dto.TransactionInputDto;
import com.amir.banking.dto.TransactionOutputDto;
import com.amir.banking.model.BankAccount;
import com.amir.banking.service.BankService;
import com.amir.banking.util.BankingConstants;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.amir.banking.util.BankingConstants.RESPONSE_SUCCESS;
import static com.amir.banking.util.BankingConstants.responseMap;

@RestController
@RequestMapping("/api/v1/account")
public class BankController {
    private final BankService bankService;
    private final Tracer tracer;
    private final TransactionLogger logger;
    private final ModelMapper modelMapper;

    @Autowired
    public BankController(BankService bankService, Tracer tracer, TransactionLogger transactionLogger, ModelMapper modelMapper) {
        this.bankService = bankService;
        this.tracer = tracer;
        this.logger = transactionLogger;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/create")
    public ResponseEntity<TransactionOutputDto> createAccount(@RequestBody TransactionInputDto dto) throws Exception {
        String traceId = Objects.requireNonNull(tracer.currentSpan()).context().traceId();
        logger.onTransaction(traceId, "createAccount called with transaction: " + dto);
        return ResponseEntity.ok(prepareOutput(traceId, bankService.createAccount(traceId, dto)));
    }

    private TransactionOutputDto prepareOutput(String traceId, BankAccount account) {
        TransactionOutputDto outputDto = modelMapper.map(account, TransactionOutputDto.class);
        outputDto.setTraceId(traceId);
        ResponseDto response = responseMap.get(RESPONSE_SUCCESS);
        response.setReqDate(LocalDateTime.now().format(BankingConstants.formatter));
        outputDto.setResponse(response);
        return outputDto;
    }

    @PostMapping("/deposit")
    public ResponseEntity<TransactionOutputDto> deposit(@RequestBody TransactionInputDto dto) throws Exception {
        String traceId = Objects.requireNonNull(tracer.currentSpan()).context().traceId();
        logger.onTransaction(traceId, "deposit called with transaction: " + dto);
        BankAccount bankAccount = bankService.doDeposit(traceId, dto);
        return ResponseEntity.ok(prepareOutput(traceId, bankAccount));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<TransactionOutputDto> withdraw(@RequestBody TransactionInputDto dto) throws Exception {
        String traceId = Objects.requireNonNull(tracer.currentSpan()).context().traceId();
        logger.onTransaction(traceId, "withdraw called with transaction: " + dto);
        BankAccount bankAccount = bankService.doWithdraw(traceId, dto);
        return ResponseEntity.ok(prepareOutput(traceId, bankAccount));
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransactionOutputDto> transfer(@RequestBody TransactionInputDto dto) throws Exception {
        String traceId = Objects.requireNonNull(tracer.currentSpan()).context().traceId();
        logger.onTransaction(traceId, "transfer called with transaction: " + dto);
        BankAccount bankAccount = bankService.doTransfer(traceId, dto);
        return ResponseEntity.ok(prepareOutput(traceId, bankAccount));
    }

    @GetMapping("/balance/{accountNumber}")
    public ResponseEntity<TransactionOutputDto> getBalance(@PathVariable String accountNumber) throws Exception {
        String traceId = Objects.requireNonNull(tracer.currentSpan()).context().traceId();
        TransactionInputDto dto = new TransactionInputDto(accountNumber);
        logger.onTransaction(traceId, "getBalance called with transaction: " + dto);
        BankAccount bankAccount = bankService.getBalance(traceId, dto);
        return ResponseEntity.ok(prepareOutput(traceId, bankAccount));
    }
}