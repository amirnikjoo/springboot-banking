package com.amir.banking.service;

import com.amir.banking.dto.TransactionDto;
import com.amir.banking.model.BankAccount;

public interface BankService {
    BankAccount createAccount(String traceId, TransactionDto dto) throws Exception;

    double doDeposit(String traceId, TransactionDto dto) throws Exception;

    double doWithdraw(String traceId, TransactionDto dto) throws Exception;

    double getBalance(String traceId, TransactionDto dto) throws Exception;

    double doTransfer(String traceId,TransactionDto dto) throws Exception;
}
