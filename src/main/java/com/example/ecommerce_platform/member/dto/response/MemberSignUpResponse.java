package com.example.ecommerce_platform.member.dto.response;

import com.example.ecommerce_platform.member.entity.Member;

public record MemberSignUpResponse(
        Long memberId,
        String email,
        String name
) {
    public static MemberSignUpResponse from(Member member) {
        return new MemberSignUpResponse(
                member.getId()
                ,member.getEmail()
                ,member.getName()
        );
    }
}
