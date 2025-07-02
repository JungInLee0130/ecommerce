package com.example.ecommerce_platform.product.service;

import com.example.ecommerce_platform.category.entity.Category;
import com.example.ecommerce_platform.category.repository.CategoryRepository;
import com.example.ecommerce_platform.common.exception.CustomException;
import com.example.ecommerce_platform.common.exception.ErrorCode;
import com.example.ecommerce_platform.product.repository.ProductRepository;
import com.example.ecommerce_platform.product.dto.request.ProductCreateRequest;
import com.example.ecommerce_platform.product.dto.request.ProductUpdateRequest;
import com.example.ecommerce_platform.product.dto.response.ProductResponse;
import com.example.ecommerce_platform.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public Long addProduct(ProductCreateRequest request) {
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        Product product = Product.builder()
                .name(request.name())
                .price(request.price())
                .stockQuantity(request.stockQuantity())
                .description(request.description())
                .imageUrl(request.imageUrl())
                .sellerId(request.sellerId())
                .category(category)
                .build();

        productRepository.save(product);

        return product.getId();
    }

    @Override
    @Transactional
    public void updateProduct(Long productId, ProductUpdateRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        Category category = null;
        if (request.categoryId() != null) {
            category = categoryRepository.findById(request.categoryId())
                    .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
        }

        product.updateProductDetails(request.name(),
                request.price(),
                request.description(),
                request.imageUrl(),
                category);

    }

    @Override
    @Transactional
    public void increaseProductStock(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        product.increaseStock(quantity);
    }

    @Override
    @Transactional
    public void decreaseProductStock(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        product.decreaseStock(quantity);
    }

    // 특정 ID의 상품 조회
    @Override
    public ProductResponse getProductById(Long productId) {
        return productRepository.findById(productId)
                .map(ProductResponse::from)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> getProductsBySellerId(Long sellerId) {
        return productRepository.findBySellerId(sellerId)
                .stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> getProductsByCategoryId(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        return productRepository.findByCategory(category)
                .stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteProduct(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new CustomException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        productRepository.deleteById(productId);
    }

    @Override
    public List<ProductResponse> searchProductsByName(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword)
                .stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> searchProductsInCategoryByName(Long categoryId, String keyword) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        return productRepository.findByNameContainingIgnoreCaseAndCategory(keyword, category)
                .stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
    }
}
