package com.amir.banking.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto {
    private String resCode;
    private String resDescFa;
    private String resDescEn;
    private String reqDate;

    @JsonIgnore
    private int httpResponseCode;
    private Object extraData;

    public ResponseDto(String resCode, String resDescFa, String resDescEn, Integer httpResponseCode) {
        this.resCode = resCode;
        this.httpResponseCode = httpResponseCode;
        this.resDescFa = resDescFa;
        this.resDescEn = resDescEn;
    }
}
