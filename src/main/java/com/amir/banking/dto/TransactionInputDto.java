package com.amir.banking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionInputDto {
    @JsonProperty
    private String accountNo;  //source account

    @JsonProperty
    private String accountTo; //destination account if needed

    @JsonProperty
    private String name;

    @JsonProperty
    private double amount;

    public TransactionInputDto(String accountNo, double amount) {
        this.accountNo = accountNo;
        this.amount = amount;
    }

    public TransactionInputDto(String accountNo, String accountTo, double amount) {
        this.accountNo = accountNo;
        this.accountTo = accountTo;
        this.amount = amount;
    }

    public TransactionInputDto(String accountNo) {
        this.accountNo = accountNo;
    }
}
