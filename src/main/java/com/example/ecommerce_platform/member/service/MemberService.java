package com.example.ecommerce_platform.member.service;

import com.example.ecommerce_platform.common.exception.CustomException;
import com.example.ecommerce_platform.common.exception.ErrorCode;
import com.example.ecommerce_platform.member.domain.Address;
import com.example.ecommerce_platform.member.domain.Role;
import com.example.ecommerce_platform.member.dto.request.MemberSignUpRequest;
import com.example.ecommerce_platform.member.dto.request.MemberUpdateRequest;
import com.example.ecommerce_platform.member.dto.response.MemberResponse;
import com.example.ecommerce_platform.member.dto.response.MemberSignUpResponse;
import com.example.ecommerce_platform.member.entity.Member;
import com.example.ecommerce_platform.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    //private final PasswordEncoder passwordEncoder;

    @Transactional
    public MemberSignUpResponse signUp(MemberSignUpRequest request) {
        if (isEmailDuplicated(request.email())) {
            // TODO : CustomException(ErrorCode.EMAIL_DUPLICATE) 발생
            throw new CustomException(ErrorCode.EMAIL_DUPLICATED);
        }

        // 2. 비밀번호 암호화 (나중에 Spring Security 추가 시 구현)
        String rawPassword = request.password();

        // 4. MemberSignUpRequest -> Member
        Member memberToSave = Member.builder()
                .email(request.email())
                .password(rawPassword)
                .name(request.name())
                .address(request.address())
                .role(Role.USER)
                .build();

        Member saveMember = memberRepository.save(memberToSave);
        // 5. 회원정보 저장
        return MemberSignUpResponse.from(saveMember);
    }

    private boolean isEmailDuplicated(String email) {
        return memberRepository.existsByEmail(email);
    }

    public MemberResponse findMemberById(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        return MemberResponse.from(member);
    }

    public MemberResponse findMemberByEmail(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        return MemberResponse.from(member);
    }

    @Transactional
    public MemberResponse updateMember(Long memberId, MemberUpdateRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if (request.address() != null) {
            member.updateAddress(request.address());
        }

        return MemberResponse.from(member);
    }

    @Transactional
    public void deleteMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        memberRepository.delete(member);
    }
}
