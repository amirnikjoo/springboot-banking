package com.amir.banking.strategy;

import com.amir.banking.core.TransactionStrategyNoFoundException;
import com.amir.banking.dto.TransactionInputDto;
import com.amir.banking.model.BankAccount;
import org.springframework.stereotype.Component;

@Component
public class TransactionContext {
    private TransactionStrategy strategy;

    public void setStrategy(TransactionStrategy strategy) {
        this.strategy = strategy;
    }

    public BankAccount executeTransaction(String traceId, TransactionInputDto dto) throws Exception {
        if (strategy == null) {
            throw new TransactionStrategyNoFoundException(traceId);
        }
        return strategy.execute(traceId,dto);
    }
}
