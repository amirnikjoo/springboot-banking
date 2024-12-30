package com.amir.banking.strategy;

import com.amir.banking.core.AccountNotFoundException;
import com.amir.banking.dto.TransactionDto;
import com.amir.banking.model.BankAccount;

public interface TransactionStrategy {
    BankAccount execute(String traceId, TransactionDto dto) throws AccountNotFoundException, Exception;
}