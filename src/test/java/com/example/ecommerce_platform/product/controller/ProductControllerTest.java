package com.example.ecommerce_platform.product.controller;

import com.example.ecommerce_platform.category.entity.Category;
import com.example.ecommerce_platform.common.exception.CustomException;
import com.example.ecommerce_platform.common.exception.ErrorCode;
import com.example.ecommerce_platform.product.dto.request.ProductCreateRequest;
import com.example.ecommerce_platform.product.dto.response.ProductResponse;
import com.example.ecommerce_platform.product.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@DisplayName("ProductController 테스트")
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;        // Http 요청을 시뮬레이션하는 객체

    @Autowired
    private ObjectMapper objectMapper; // Java객체를 JSON으로 변환하는 객체

    @MockitoBean
    private ProductService productService;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("상품 등록 성공 테스트")
    void registerProduct_Success() throws Exception {
        Long expectedProductId = 1L;

        given(productService.registerProduct(any(ProductCreateRequest.class)))
                .willReturn(expectedProductId);

        ProductCreateRequest validProductCreateRequest = new ProductCreateRequest(
                "프리미엄 무선 게이밍 마우스 XYZ",
                BigDecimal.valueOf(79.0),
                150,
                "최신 센서가 탑재된 인체공학적 디자인의 무선 게이밍 마우스입니다. 뛰어난 반응 속도와 배터리 효율을 자랑합니다.",
                "http://test.com/image.jpg",
                1L,
                1L
        );
        mockMvc.perform(post("/api/v1/products")       // api 경로
                        .contentType(MediaType.APPLICATION_JSON)        // request content-type : json
                        .content(objectMapper.writeValueAsString(validProductCreateRequest)))   // request json as string
                .andDo(print())                     // 결과 print
                .andExpect(status().isCreated())    // header : status
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value(expectedProductId)); // message는 공백 틀리면 틀려서 검증안함.
    }

    @Test
    @DisplayName("상품 등록 실패 테스트 - 유효성 검사 오류 (이름 누락)")
    void registerProduct_ValidationFailure_NamingMissing() throws Exception {
        ProductCreateRequest invalidRequest = new ProductCreateRequest(
                "",
                BigDecimal.valueOf(100),
                10,
                "테스트 상품 설명",
                "http://test.com/image.jpg",
                1L,
                1L
        );

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value(ErrorCode.INVALID_INPUT_VALUE.getMessage()))
                .andExpect(jsonPath("$.data.name").value("상품명은 필수 입력 값입니다."));
    }
    
    @Test
    @DisplayName("상품 등록 실패 테스트 - 서비스 예외 발생 (존재하지 않는 카테고리)")
    void registerProduct_ServiceException_CategoryNotFound() throws Exception {
        given(productService.registerProduct(any(ProductCreateRequest.class)))
                .willThrow(new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

        ProductCreateRequest requestWithInvalidCategory = new ProductCreateRequest(
                "테스트 상품"
                ,BigDecimal.valueOf(100)
                ,10
                , "테스트 상품 설명"
                ,"http://test.com/image.jpg"
                , 1L
                ,  999L
        );

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestWithInvalidCategory)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value(ErrorCode.CATEGORY_NOT_FOUND.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("특정 ID 상품 조회 성공 테스트")
    void getProductById_Success() throws Exception {
        Long productId = 1L;

        ProductResponse mockProductResponse = ProductResponse.builder()
                .id(productId)
                .name("테스트 상품")
                .price(BigDecimal.valueOf(100))
                .stockQuantity(10)
                .description("테스트 설명")
                .imageUrl("http://test.com/image.jpg")
                .sellerId(1L)
                .categoryId(1L)
                .build();

        given(productService.getProductById(productId))
                .willReturn(mockProductResponse);

        mockMvc.perform(get("/api/v1/products/{productId}", productId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(productId));
    }

    @Test
    @DisplayName("모든 상품 목록 조회 성공 테스트")
    void getAllProducts_Success() throws Exception {
        List<ProductResponse> mockProducts = List.of(
                  ProductResponse.builder().id(1L).name("상품1").price(BigDecimal.valueOf(100)).build()
                , ProductResponse.builder().id(2L).name("상품2").price(BigDecimal.valueOf(200)).build()
        );

        given(productService.getAllProducts()).willReturn(mockProducts);

        mockMvc.perform(get("/api/v1/products"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(mockProducts.size()))
                .andExpect(jsonPath("$.data[0].name").value("상품1"));
    }
}