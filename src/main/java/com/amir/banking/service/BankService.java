package com.amir.banking.service;

import com.amir.banking.dto.TransactionInputDto;
import com.amir.banking.model.BankAccount;

public interface BankService {
    BankAccount createAccount(String traceId, TransactionInputDto dto) throws Exception;

    BankAccount doDeposit(String traceId, TransactionInputDto dto) throws Exception;

    BankAccount doWithdraw(String traceId, TransactionInputDto dto) throws Exception;

    BankAccount getBalance(String traceId, TransactionInputDto dto) throws Exception;

    BankAccount doTransfer(String traceId, TransactionInputDto dto) throws Exception;
}
