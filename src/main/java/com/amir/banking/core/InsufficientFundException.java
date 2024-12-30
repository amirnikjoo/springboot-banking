package com.amir.banking.core;

public class InsufficientFundException extends IException {

    public InsufficientFundException(String traceId) {
        super(traceId, "InsufficientFundException");

    }
}