package com.amir.banking.core;

public class TransactionStrategyNoFoundException extends IException {

    public TransactionStrategyNoFoundException(String traceId) {
        super(traceId, "TransactionStrategyNoFoundException");
    }
}