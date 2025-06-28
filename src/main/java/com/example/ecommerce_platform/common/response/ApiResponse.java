package com.example.ecommerce_platform.common.response;

import com.example.ecommerce_platform.common.exception.ErrorCode;

public record ApiResponse<T> (
        boolean success,
        String message,
        T data
){
    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<>(true, message, null);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

    public static <T> ApiResponse<T> failure(String message) {
        return new ApiResponse<>(false, message, null);
    }

    public static <T> ApiResponse<T> failure(String message, T data) {
        return new ApiResponse<>(false, message, data);
    }

    public static <T> ApiResponse<T> failure(ErrorCode errorCode) {
        return new ApiResponse<>(false, errorCode.getMessage(), null);
    }

    public static <T> ApiResponse<T> failure(ErrorCode errorCode
            , String detailMessage) {
        String fullMessage = errorCode.getMessage();
        if (detailMessage != null && !detailMessage.trim().isEmpty()) {
            fullMessage += " - " + detailMessage;
        }
        return new ApiResponse<>(false, fullMessage, null);
    }
}
