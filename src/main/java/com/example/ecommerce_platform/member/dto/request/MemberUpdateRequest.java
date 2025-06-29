package com.example.ecommerce_platform.member.dto.request;

import com.example.ecommerce_platform.member.domain.Address;
import jakarta.validation.Valid;
import lombok.Builder;

@Builder
public record MemberUpdateRequest (
        @Valid
        Address address
){
}
