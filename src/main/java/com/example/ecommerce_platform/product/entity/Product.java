package com.example.ecommerce_platform.product.entity;

import com.example.ecommerce_platform.category.entity.Category;
import com.example.ecommerce_platform.common.exception.CustomException;
import com.example.ecommerce_platform.common.exception.ErrorCode;
import com.example.ecommerce_platform.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "PRODUCTS")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(nullable = false) // default = default 255
    private String name;

    @Column(nullable = false, precision = 10) // scale = default 0
    private BigDecimal price;

    @Column(nullable = false)
    private Integer stockQuantity;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 500)
    private String imageUrl;

    //@ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(referencedColumnName = "member_id", name = "seller_id", nullable = false)
    //private Member seller;
    private Long sellerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Builder
    public Product(String name, BigDecimal price, Integer stockQuantity, String description, String imageUrl, Long sellerId, Category category) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.description = description;
        this.imageUrl = imageUrl;
        this.sellerId = sellerId;
        this.category = category;
    }

    // --- 비즈니스 로직 (재고 감소, 정보 수정 등) ---
    public void updateProductDetails(String name, BigDecimal price, String description
            , String imageUrl, Category category) {
        if (name != null) this.name = name;
        if (price != null) this.price = price;
        if (description != null) this.description = description;
        if (imageUrl != null) this.imageUrl = imageUrl;
        if (category != null) this.category = category;
    }

    public void increaseStock(int quantity){
        if (quantity < 0) {
            throw new CustomException(ErrorCode.INVALID_PRODUCT_STOCK_QUANTITY);
        }
        this.stockQuantity += quantity;
    }

    public void decreaseStock(int quantity) {
        if (quantity < 0) {
            throw new CustomException(ErrorCode.INVALID_PRODUCT_STOCK_QUANTITY);
        }
        if (this.stockQuantity - quantity < 0) {
            throw new CustomException(ErrorCode.NOT_ENOUGH_PRODUCT_STOCK);
        }
        this.stockQuantity -= quantity;
    }
}
