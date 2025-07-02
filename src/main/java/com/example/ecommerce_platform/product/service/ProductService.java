package com.example.ecommerce_platform.product.service;

import com.example.ecommerce_platform.product.dto.request.ProductCreateRequest;
import com.example.ecommerce_platform.product.dto.request.ProductUpdateRequest;
import com.example.ecommerce_platform.product.dto.response.ProductResponse;
import com.example.ecommerce_platform.product.entity.Product;

import java.util.*;

public interface ProductService {
    Long addProduct(ProductCreateRequest request);

    void updateProduct(Long productId, ProductUpdateRequest request);

    void increaseProductStock(Long productId, int quantity);

    void decreaseProductStock(Long productId, int quantity);

    ProductResponse getProductById(Long productId);

    List<ProductResponse> getAllProducts();

    List<ProductResponse> getProductsBySellerId(Long sellerId);

    List<ProductResponse> getProductsByCategoryId(Long categoryId);

    void deleteProduct(Long productId);

    List<ProductResponse> searchProductsByName(String keyword);

    List<ProductResponse> searchProductsInCategoryByName(Long categoryId, String keyword);
}
