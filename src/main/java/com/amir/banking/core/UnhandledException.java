package com.amir.banking.core;

public class UnhandledException extends IException {

    public UnhandledException(String traceId) {
        super(traceId, "TransactionStrategyNoFoundException");
    }
}