package com.example.ecommerce_platform.category.controller;

import com.example.ecommerce_platform.category.dto.request.CategoryCreateRequest;
import com.example.ecommerce_platform.category.dto.response.CategoryResponse;
import com.example.ecommerce_platform.category.service.CategoryService;
import com.example.ecommerce_platform.common.exception.CustomException;
import com.example.ecommerce_platform.common.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    @Test
    @DisplayName("카테고리 생성 API - 성공")
    void createCategory_success() throws Exception {
        CategoryCreateRequest request = new CategoryCreateRequest("전자제품");
        given(categoryService.createCategory(any(CategoryCreateRequest.class))).willReturn(1L);

        mockMvc.perform(post("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data").value(1L));
    }

    @Test
    @DisplayName("카테고리 단일 조회 API - 성공")
    void getCategoryById_success() throws Exception {
        CategoryResponse response = new CategoryResponse(1L, "전자제품");
        given(categoryService.getCategoryById(1L)).willReturn(response);

        mockMvc.perform(get("/api/v1/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.name").value("전자제품"));
    }

    @Test
    @DisplayName("카테고리 전체 조회 API - 성공")
    void getAllCategories_success() throws Exception {
        List<CategoryResponse> responses = Arrays.asList(
                new CategoryResponse(1L, "전자제품"),
                new CategoryResponse(2L, "패션"));
        given(categoryService.getAllCategories()).willReturn(responses);

        mockMvc.perform(get("/api/v1/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(1L))
                .andExpect(jsonPath("$.data[1].name").value("패션"));
    }

    @Test
    @DisplayName("카테고리 삭제 API - 성공")
    void deleteCategory_success() throws Exception {
        willDoNothing().given(categoryService).deleteCategory(1L);

        mockMvc.perform(delete("/api/v1/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("카테고리를 삭제했습니다."));
    }

    @Test
    @DisplayName("카테고리 생성 API - 유효성 실패")
    void createCategory_validationFail() throws Exception {
        CategoryCreateRequest request = new CategoryCreateRequest(""); // name이 빈 값

        mockMvc.perform(post("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("잘못된 입력 값입니다."))
                .andExpect(jsonPath("$.data.name").value("카테고리명은 필수 입력 값입니다."));
    }

    @Test
    @DisplayName("카테고리 단일 조회 API - 존재하지 않는 ID")
    void getCategoryById_notFound() throws Exception {
        given(categoryService.getCategoryById(999L))
                .willThrow(new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        mockMvc.perform(get("/api/v1/categories/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("카테고리를 찾을 수 없습니다."));
    }

    @Test
    @DisplayName("카테고리 삭제 API - 존재하지 않는 ID")
    void deleteCategory_notFound() throws Exception {
        willThrow(new CustomException(ErrorCode.CATEGORY_NOT_FOUND))
                .given(categoryService).deleteCategory(999L);

        mockMvc.perform(delete("/api/v1/categories/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("카테고리를 찾을 수 없습니다."));
    }
}