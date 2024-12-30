package com.amir.banking.core;

public class AccountAlreadyExistsException extends IException {

    public AccountAlreadyExistsException(String traceId) {
        super(traceId, "AccountAlreadyExistsException");

    }
}