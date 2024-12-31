package com.amir.banking.util;

import com.amir.banking.dto.ResponseDto;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class BankingConstants {

    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd,HH:mm:ss,SSSSSS");

    public static final int TEST_TRACE_ID_LEN = 16;

    public static final double TEST_INIT_BALANCE_ACC1 = 1000;
    public static final double TEST_INIT_BALANCE_ACC2 = 0;

    public static final String TEST_ACCOUNT_NO1 = "1";
    public static final String TEST_ACCOUNT_NO2 = "2";

    public static final String RESPONSE_SUCCESS = "Success";

    public static final String TRANSACTION_TYPE_CREATE_ACCOUNT = "CREATE_ACCOUNT";
    public static final String TRANSACTION_TYPE_BALANCE = "BALANCE";
    public static final String TRANSACTION_TYPE_DEPOSIT = "DEPOSIT";
    public static final String TRANSACTION_TYPE_WITHDRAW = "WITHDRAW";
    public static final String TRANSACTION_TYPE_TRANSFER_FROM = "TRANSFER_FROM";
    public static final String TRANSACTION_TYPE_TRANSFER_TO = "TRANSFER_TO";

    public static final String EXCEPTIONS_UNHANDLED = "UnhandledException";

    //todo: all exceptions go to db
    public static final Map<String, ResponseDto> responseMap = new HashMap<>();
    static {
        responseMap.put("Success", new ResponseDto("000", "تراکنش با موفقیت انجام شد", "Success", 200));

        responseMap.put("AccountNotFoundException", new ResponseDto("001", "شماره حسابی یافت نشد", "AccountNotFoundException", 400));
        responseMap.put("AccountAlreadyExistsException", new ResponseDto("002", "شماره حساب تکراری است", "AccountAlreadyExistsException", 400));

        responseMap.put("InsufficientFundException", new ResponseDto("201", "مانده کافی نیست", "InsufficientFundException", 400));
        responseMap.put("TransactionStrategyNoFoundException", new ResponseDto("801", "خطای داخلی برنامه", "TransactionStrategyNoFoundException", 500));

        responseMap.put("UnhandledException", new ResponseDto("999", "خطای داخلی", "UnhandledException", 500));
    }
}
