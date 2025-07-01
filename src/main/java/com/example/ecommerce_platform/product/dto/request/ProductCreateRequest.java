package com.example.ecommerce_platform.product.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductCreateRequest(
        @NotBlank(message = "상품명은 필수 입력 값입니다.")
        @Size(max = 255, message = "상품명은 255자를 초과할 수 없습니다.")
        String name,

        @NotNull(message = "가격은 필수 입력 값입니다.")
        @DecimalMin(value = "0.0", message = "가격은 0이상이여야 합니다.")
        BigDecimal price,

        @NotNull(message = "재고 수량은 필수 입력 값입니다.")
        @Min(value = 0, message = "재고 수량은 0 이상이여야 합니다.")
        Integer stockQuantity,

        @Size(max = 2000, message = "상품 설명은 2000자를 초과할 수 없습니다.")
        String description,

        @Size(max = 500, message = "이미지 URL은 500자를 초과할 수 없습니다.")
        String imageUrl,

        @NotNull(message = "판매자 ID는 필수 입력 값입니다.")
        Long sellerId,

        @NotNull(message = "카테고리 ID는 필수 입력 값입니다.")
        Long categoryId
) {

}
