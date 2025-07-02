package com.example.ecommerce_platform.category.service;

import com.example.ecommerce_platform.category.dto.request.CategoryCreateRequest;
import com.example.ecommerce_platform.category.dto.request.CategoryUpdateRequest;
import com.example.ecommerce_platform.category.dto.response.CategoryResponse;
import com.example.ecommerce_platform.category.entity.Category;
import com.example.ecommerce_platform.category.repository.CategoryRepository;
import com.example.ecommerce_platform.common.exception.CustomException;
import com.example.ecommerce_platform.common.exception.ErrorCode;
import com.example.ecommerce_platform.product.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Long createCategory(CategoryCreateRequest request) {
        if (categoryRepository.findByName(request.name()).isPresent()) {
            throw new CustomException(ErrorCode.CATEGORY_NAME_DUPLICATED);
        }

        Category category = Category.builder()
                .name(request.name())
                .build();

        categoryRepository.save(category);

        return category.getId();
    }

    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        return CategoryResponse.from(category);
    }

    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(CategoryResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryUpdateRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        if (categoryRepository.existsByName(request.name())
                && !category.getName().equals(request.name())) {
            throw new CustomException(ErrorCode.CATEGORY_NAME_DUPLICATED);
        }

        category.updateName(request.name());
        
        return CategoryResponse.from(category);
    }

    @Transactional
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new CustomException(ErrorCode.CATEGORY_NOT_FOUND);
        }
        // TODO : 카테고리에 상품이 있다면 어떻게 처리할지 결정 (삭제 불가 or 다른 카테고리로 이동)
        // 카테고리에 상품이 있다면 삭제 불가
        if (productRepository.existsByCategoryId(id)) {
            throw new CustomException(ErrorCode.CATEGORY_HAS_PRODUCTS);
        }
        categoryRepository.deleteById(id);
    }
}
