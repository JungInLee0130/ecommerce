package com.example.ecommerce_platform.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // Common (공통 에러)
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C001", "잘못된 입력 값입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C004", "서버 내부 오류가 발생했습니다."),

    // Member (회원 관련 에러)
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "M001", "회원정보를 찾을 수 없습니다."),
    EMAIL_DUPLICATED(HttpStatus.CONFLICT, "M002", "이미 사용중인 이메일입니다."),
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "M003", "비밀번호 확인이 일치하지않습니다."),

    // Product (상품 관련 에러)
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "P001", "상품을 찾을 수 없습니다."),
    INVALID_PRODUCT_STOCK_QUANTITY(HttpStatus.BAD_REQUEST, "P002", "유효하지 않은 상품 재고 수량입니다."),
    NOT_ENOUGH_PRODUCT_STOCK(HttpStatus.BAD_REQUEST, "P003", "상품 재고가 부족합니다."),

    // CATEGORY(카테고리 관련 에러)
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "CAT001", "카테고리를 찾을 수 없습니다."),
    CATEGORY_NAME_DUPLICATED(HttpStatus.CONFLICT, "CAT002", "이미 존재하는 카테고리 이름입니다."),
    CATEGORY_HAS_PRODUCTS(HttpStatus.BAD_REQUEST, "CAT003", "카테고리에 상품이 있어 삭제할 수 없습니다.");

    private final HttpStatus status;
    private final String code; // 비즈니스 에러 코드
    private final String message; // 클라이언트에 전달할 에러 메시지
}
