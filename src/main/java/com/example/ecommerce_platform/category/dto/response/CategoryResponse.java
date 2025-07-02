package com.example.ecommerce_platform.category.dto.response;

import com.example.ecommerce_platform.category.entity.Category;
import lombok.Builder;

@Builder
public record CategoryResponse (
        Long id,
        String name
){
    public static CategoryResponse from(Category category) {
        return CategoryResponse.builder()
                .name(category.getName())
                .build();
    }
}
