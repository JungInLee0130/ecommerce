package com.example.ecommerce_platform.product.dto.response;

import com.example.ecommerce_platform.product.entity.Product;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProductResponse(
        Long id,
        String name,
        BigDecimal price,
        Integer stockQuantity,
        String description,
        String imageUrl,
        Long sellerId,
        Long categoryId,
        String categoryName
) {
    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getDescription(),
                product.getImageUrl(),
                product.getSellerId(),
                product.getCategory().getId(),
                product.getCategory().getName()
        );
    }
}
