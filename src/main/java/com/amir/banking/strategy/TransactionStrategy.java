package com.amir.banking.strategy;

import com.amir.banking.core.AccountNotFoundException;
import com.amir.banking.dto.TransactionInputDto;
import com.amir.banking.model.BankAccount;

public interface TransactionStrategy {
    BankAccount execute(String traceId, TransactionInputDto dto) throws AccountNotFoundException, Exception;
}