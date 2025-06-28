package com.example.ecommerce_platform.common.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{
    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public CustomException(String message, ErrorCode errorCode) {
        super(errorCode.getMessage()
                + message != null ? "-" + message : "");
        this.errorCode = errorCode;
    }
}
