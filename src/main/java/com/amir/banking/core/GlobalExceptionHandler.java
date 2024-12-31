package com.amir.banking.core;

import com.amir.banking.component.TransactionLogger;
import com.amir.banking.dto.ResponseDto;
import com.amir.banking.dto.TransactionOutputDto;
import com.amir.banking.util.BankingConstants;
import com.amir.banking.util.StringUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.amir.banking.util.BankingConstants.EXCEPTIONS_UNHANDLED;
import static com.amir.banking.util.BankingConstants.responseMap;

@ControllerAdvice
public class GlobalExceptionHandler {
    private final TransactionLogger transactionLogger;
//    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd,HH:mm:ss,SSSSSS");

    public GlobalExceptionHandler(TransactionLogger transactionLogger) {
        this.transactionLogger = transactionLogger;
    }

    @ExceptionHandler(IException.class)
    public ResponseEntity<TransactionOutputDto> handleAllExceptions(IException ex) {
        String traceId = ex.getTraceId();
        String simpleName = ex.getClass().getSimpleName();
        transactionLogger.onTransaction(traceId, "className: " + simpleName);

        TransactionOutputDto outputDto = new TransactionOutputDto();
        outputDto.setTraceId(ex.getTraceId());
        ResponseDto responseDto = responseMap.get(simpleName);
        responseDto.setReqDate(LocalDateTime.now().format(BankingConstants.formatter));
        outputDto.setResponse(responseDto);
        return new ResponseEntity<>(outputDto, HttpStatus.valueOf(responseDto.getHttpResponseCode()));
    }

/*    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto> handleAllExceptions(Exception ex) {
        String generalExceptionTranceId = StringUtil.generateRandomTraceId(16);
        String simpleName = ex.getClass().getSimpleName();
        transactionLogger.onTransaction(generalExceptionTranceId, "className: " + simpleName);
        ResponseDto responseDto = responseMap.get(EXCEPTIONS_UNHANDLED);
        responseDto.setReqDate(LocalDateTime.now().format(formatter));
        return new ResponseEntity<>(responseDto, HttpStatus.valueOf(responseDto.getHttpResponseCode()));
    }*/
}
