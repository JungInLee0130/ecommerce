package com.example.ecommerce_platform.category.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CategoryUpdateRequest(
        @NotBlank(message = "카테고리명은 필수 입력 값입니다.")
        @Size(max = 100, message = "카테고리명은 100자를 초과할 수 없습니다.")
        String name
) {
}
