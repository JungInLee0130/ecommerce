package com.example.ecommerce_platform.product.controller;

import com.example.ecommerce_platform.common.response.ApiResponse;
import com.example.ecommerce_platform.product.dto.request.ProductCreateRequest;
import com.example.ecommerce_platform.product.dto.response.ProductResponse;
import com.example.ecommerce_platform.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    // 제품등록
    @PostMapping
    public ResponseEntity<ApiResponse<Long>> registerProduct(@Valid @RequestBody ProductCreateRequest request) {
        Long productId = productService.registerProduct(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("상품이 성공적으로 등록되었습니다.", productId));
    }

    // 특정상품조회
    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(
            @PathVariable Long productId) {
        ProductResponse response = productService.getProductById(productId);

        return ResponseEntity.ok(ApiResponse.success("상품 조회 성공", response));
    }

    // 모든상품목록조회 성공
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAllProducts() {
        List<ProductResponse> products = productService.getAllProducts();

        return ResponseEntity.ok(ApiResponse.success("모든 상품 목록 조회 설정", products));
    }
}
