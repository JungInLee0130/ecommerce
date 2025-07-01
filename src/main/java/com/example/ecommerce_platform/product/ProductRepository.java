package com.example.ecommerce_platform.product;

import com.example.ecommerce_platform.category.entity.Category;
import com.example.ecommerce_platform.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByName(String name);

    List<Product> findBySellerId(Long sellerId);

    List<Product> findByCategory(Category category);

    Optional<Product> findByNameAndSellerId(String name, Long sellerId);

    List<Product> findBySellerIdAndCategory(Long sellerId, Category category);

    List<Product> findByStockQuantityGreaterThan(Integer stockQuantity);

    List<Product> findByCategoryAndStockQuantityGreaterThan(Category category, Integer stockQuantity);

    List<Product> findByNameContainingIgnoreCase(String name);

    List<Product> findByNameContainingIgnoreCaseAndCategory(String name, Category category);
}
