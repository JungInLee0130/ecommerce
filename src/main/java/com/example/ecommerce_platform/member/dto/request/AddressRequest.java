package com.example.ecommerce_platform.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AddressRequest (
        @NotBlank(message = "도로명/지번 주소는 필수 입력 값입니다.")
        @Size(max = 255, message = "도로명/지번 주소는 255자 이하로 입력해주세요.")
        String street,

        @Size(max = 255, message = "상세 주소는 255자 이하로 입력해주세요.")
        String detail,

        @NotBlank(message = "우편번호는 필수 입력 값입니다.")
        @Pattern(regexp = "^\\d{5}$", message = "유효한 5자리 우편번호를 입력해주세요.")
        String zipcode
){ }
