package com.amir.banking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionOutputDto {
    @JsonProperty
    private String traceId;

    @JsonProperty
    private String account;  //source account

    @JsonProperty
    private String name;

    @JsonProperty
    private Double balance;

    @JsonProperty
    private ResponseDto response;
}
