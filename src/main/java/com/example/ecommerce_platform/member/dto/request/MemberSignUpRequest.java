package com.example.ecommerce_platform.member.dto.request;


import com.example.ecommerce_platform.member.domain.Address;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Builder;

@Builder
public record MemberSignUpRequest (
        @NotBlank(message = "이메일은 필수 입력 값입니다.")
        @Email(message = "유효한 이메일 주소를 입력해주세요.")
        @Size(max = 100, message = "이메일은 100자 이하로 입력해주세요.")
        String email,

        @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
        @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해주세요.")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%&*])[A-Za-z\\d!@#$%&*]+$",
        message = "비밀번호는 영문 대소문자, 숫자, 특수문자를 각각 최소 1개 이상 포함해야합니다.")
        // 가입시에는 들어감
        String password,

        @NotBlank(message = "비밀번호 확인은 필수 입력 값입니다.")
        String passwordConfirm,

        @NotBlank(message = "이름은 필수 입력 값입니다.")
        @Size(max = 50, message = "이름은 50자 이하로 입력해주세요.")
        String name,

        @Valid
        Address address
){ }
