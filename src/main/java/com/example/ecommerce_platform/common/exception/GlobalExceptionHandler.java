package com.example.ecommerce_platform.common.exception;

import com.example.ecommerce_platform.common.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.print.DocFlavor;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        log.error("Validation Error : {}", errors);

        return new ResponseEntity<>(
                ApiResponse.failure(ErrorCode.INVALID_INPUT_VALUE.getMessage(),
                        errors
                ),
                ErrorCode.INVALID_INPUT_VALUE.getStatus()
        );
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<Void>> handleCustomException(CustomException ex) {
        log.error("CustomException : Code = {}, Message = {}", ex.getErrorCode().getCode()
                , ex.getMessage(), ex);

        return new ResponseEntity<>(
                ApiResponse.failure(ex.getErrorCode()),
                ex.getErrorCode().getStatus()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception ex) {
        log.error("Unhandled Exception : {}", ex.getMessage(), ex);

        return new ResponseEntity<>(
                ApiResponse.failure(
                        ErrorCode.INTERNAL_SERVER_ERROR.getMessage()
                                + (ex.getMessage() != null ? " - " + ex.getMessage() : "")
                ),
                        ErrorCode.INTERNAL_SERVER_ERROR.getStatus()
        );
    }

}
