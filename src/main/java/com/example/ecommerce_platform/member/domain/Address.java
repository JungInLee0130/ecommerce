package com.example.ecommerce_platform.member.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Objects;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Address {
    @Column(nullable = false)
    @NotBlank(message = "도로명/지번 주소는 필수 입력 값입니다.")
    @Size(max = 255, message = "도로명/지번 주소는 255자 이하로 입력해주세요.")
    private String street;

    @Column(nullable = false)
    @Size(max = 255, message = "상세 주소는 255자 이하로 입력해주세요.")
    private String detail;

    @Column(nullable = false)
    @NotBlank(message = "우편번호는 필수 입력 값입니다.")
    @Pattern(regexp = "^\\d{5}$", message = "유효한 5자리 우편번호를 입력해주세요.")
    private String zipcode;

    @Builder
    public Address(String street, String detail, String zipcode) {
        this.street = street;
        this.detail = detail;
        this.zipcode = zipcode;
    }

    // 동등성 추가 : city, street, zipcode가 동일하면 값은값.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(street, address.street)
                    && Objects.equals(detail, address.detail)
                    && Objects.equals(zipcode, address.zipcode);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(street, detail, zipcode);
    }
}
