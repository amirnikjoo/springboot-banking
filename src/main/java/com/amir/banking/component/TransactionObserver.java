package com.amir.banking.component;

public interface TransactionObserver {
    void onTransaction(String traceId, String accountNumber, String transactionType, double amount);
    void onTransaction(String traceId, String message);

}