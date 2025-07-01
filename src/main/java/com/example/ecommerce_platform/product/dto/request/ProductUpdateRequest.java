package com.example.ecommerce_platform.product.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductUpdateRequest (
        @Size(max = 255, message = "상품명은 255자를 초과할 수 없습니다.")
        String name,

        @DecimalMin(value = "0.0", message = "가격은 0이상이여야 합니다.")
        BigDecimal price,

        @Size(max = 2000, message = "상품 설명은 2000자를 초과할 수 없습니다.")
        String description,

        @Size(max = 500, message = "이미지 URL은 500자를 초과할 수 없습니다.")
        String imageUrl,

        Long categoryId
){

}
