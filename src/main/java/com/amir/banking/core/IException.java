package com.amir.banking.core;

import lombok.Data;

@Data
public abstract class IException extends Exception {
    static final long serialVersionUID = -334147822944743062L;

    private String traceId;

    public IException(String traceId, String message) {
        super(message);
        this.traceId = traceId;
    }
}
