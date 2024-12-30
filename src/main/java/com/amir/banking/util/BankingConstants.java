package com.amir.banking.util;

import com.amir.banking.dto.ResponseErrorDto;

import java.util.HashMap;
import java.util.Map;

public class BankingConstants {

    public static final int TEST_TRACE_ID_LEN = 16;

    public static final double TEST_INIT_BALANCE_ACC1 = 1000;
    public static final double TEST_INIT_BALANCE_ACC2 = 0;

    public static final String TEST_ACCOUNT_NO1 = "1";
    public static final String TEST_ACCOUNT_NO2 = "2";

    public static final String TRANSACTION_TYPE_CREATE_ACCOUNT = "CREATE_ACCOUNT";
    public static final String TRANSACTION_TYPE_BALANCE = "BALANCE";
    public static final String TRANSACTION_TYPE_DEPOSIT = "DEPOSIT";
    public static final String TRANSACTION_TYPE_WITHDRAW = "WITHDRAW";
    public static final String TRANSACTION_TYPE_TRANSFER_FROM = "TRANSFER_FROM";
    public static final String TRANSACTION_TYPE_TRANSFER_TO = "TRANSFER_TO";

    public static final String EXCEPTIONS_UNHANDLED = "UnhandledException";

    //todo: all exceptions go to db
    public static final Map<String, ResponseErrorDto> errorMap = new HashMap<>();
    static {
        errorMap.put("AccountNotFoundException", new ResponseErrorDto("001", "شماره حسابی یافت نشد", "AccountNotFoundException", 400));
        errorMap.put("AccountAlreadyExistsException", new ResponseErrorDto("002", "شماره حساب تکراری است", "AccountAlreadyExistsException", 400));

        errorMap.put("InsufficientFundException", new ResponseErrorDto("201", "مانده کافی نیست", "InsufficientFundException", 400));
        errorMap.put("TransactionStrategyNoFoundException", new ResponseErrorDto("801", "خطای داخلی برنامه", "TransactionStrategyNoFoundException", 500));

        errorMap.put("UnhandledException", new ResponseErrorDto("999", "خطای داخلی", "UnhandledException", 500));
    }
}
