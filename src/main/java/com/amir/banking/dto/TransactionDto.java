package com.amir.banking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {
    @JsonProperty
    private String account;  //source account

    @JsonProperty
    private String accountTo; //destination account if needed

    @JsonProperty
    private String holderName;

    @JsonProperty
    private double amount;

    public TransactionDto(String account, double amount) {
        this.account = account;
        this.amount = amount;
    }

    public TransactionDto(String account, String accountTo, double amount) {
        this.account = account;
        this.accountTo = accountTo;
        this.amount = amount;
    }

    public TransactionDto(String account) {
        this.account = account;
    }
}
