package com.amir.banking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionOutputDto {
    @JsonProperty
    private String traceId;

    @JsonProperty
    private String account;  //source account

    @JsonProperty
    private String holderName;

    @JsonProperty
    private double balance;

    @JsonProperty
    private ResponseErrorDto errorDto;
}
