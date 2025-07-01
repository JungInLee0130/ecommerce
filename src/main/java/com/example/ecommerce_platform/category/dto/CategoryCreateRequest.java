package com.example.ecommerce_platform.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryCreateRequest(
        @NotBlank(message = "카테고리명은 필수 입력 값입니다.")
        @Size(max = 100, message = "카테고리명은 100자를 초과할 수 없습니다.")
        String name
) {

}
