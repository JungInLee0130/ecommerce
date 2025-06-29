package com.example.ecommerce_platform.member.dto.response;

import com.example.ecommerce_platform.member.domain.Address;
import com.example.ecommerce_platform.member.entity.Member;
import lombok.Builder;

@Builder
public record MemberResponse (
        Long id,
        String email,
        String name,
        Address address
){
    public static MemberResponse from(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .address(member.getAddress())
                .build();
    }
}
