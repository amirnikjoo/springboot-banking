package com.amir.banking.core;

public class AccountNotFoundException extends IException {

    public AccountNotFoundException(String traceId) {
        super(traceId, "AccountNotFoundException");
    }
}