package com.example.ecommerce_platform.common.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{
    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public CustomException(String detailMessage, ErrorCode errorCode) {
        super(errorCode.getMessage()
                + detailMessage != null && !detailMessage.trim().isEmpty()
                ? "-" + detailMessage : "");
        this.errorCode = errorCode;
    }
}
