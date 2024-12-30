package com.amir.banking.core;

import com.amir.banking.component.TransactionLogger;
import com.amir.banking.dto.ResponseErrorDto;
import com.amir.banking.util.StringUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static com.amir.banking.util.BankingConstants.EXCEPTIONS_UNHANDLED;
import static com.amir.banking.util.BankingConstants.errorMap;

@ControllerAdvice
public class GlobalExceptionHandler {
    private final TransactionLogger transactionLogger;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd,HH:mm:ss,SSSSSS");

    public GlobalExceptionHandler(TransactionLogger transactionLogger) {
        this.transactionLogger = transactionLogger;
    }

    @ExceptionHandler(IException.class)
    public ResponseEntity<ResponseErrorDto> handleAllExceptions(IException ex) {
        String traceId = ex.getTraceId();
        String simpleName = ex.getClass().getSimpleName();
        transactionLogger.onTransaction(traceId, "className: " + simpleName);

        ResponseErrorDto responseErrorDto = errorMap.get(simpleName);
        responseErrorDto.setTraceId(traceId);
        responseErrorDto.setReqDate(LocalDateTime.now().format(formatter));
        return new ResponseEntity<>(responseErrorDto, HttpStatus.valueOf(responseErrorDto.getHttpResponseCode()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseErrorDto> handleAllExceptions(Exception ex) {
        String generalExceptionTranceId = StringUtil.generateRandomTraceId(16);
        String simpleName = ex.getClass().getSimpleName();
        transactionLogger.onTransaction(generalExceptionTranceId, "className: " + simpleName);
        ResponseErrorDto responseErrorDto = errorMap.get(EXCEPTIONS_UNHANDLED);
        responseErrorDto.setTraceId(generalExceptionTranceId);
        responseErrorDto.setReqDate(LocalDateTime.now().format(formatter));
        return new ResponseEntity<>(responseErrorDto, HttpStatus.valueOf(responseErrorDto.getHttpResponseCode()));
    }
}
