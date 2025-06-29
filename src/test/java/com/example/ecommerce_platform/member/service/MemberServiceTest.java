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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    private MemberSignUpRequest signUpRequest;
    private Member member;
    private MemberSignUpResponse signUpResponse;

    @BeforeEach
    void setUp() {
        Address testAddress = Address.builder()
                .street("서울특별시 강남구 테헤란로 42길 12")
                .detail("3층 301호")
                .zipcode("06179")
                .build();

        signUpRequest = MemberSignUpRequest.builder()
                .email("test@example.com")
                .password("pAssword123!")
                .passwordConfirm("pAssword123!")
                .name("테스터")
                .address(testAddress)
                .build();

        member = new Member(1L
                , "test@example.com"
                , "pAssword123!"
                , "테스터"
                , testAddress
                , Role.USER);

        signUpResponse = MemberSignUpResponse.from(member);
    }

    @DisplayName("회원가입 성공 테스트")
    @Test
    void signUp_Success() {
        when(memberRepository.existsByEmail(anyString())).thenReturn(false);
        when(memberRepository.save(any(Member.class))).thenReturn(member);

        MemberSignUpResponse result = memberService.signUp(signUpRequest);

        assertThat(result).isNotNull();
        assertThat(result.memberId()).isEqualTo(member.getId());

        verify(memberRepository, times(1))
                .existsByEmail(signUpRequest.email());
        verify(memberRepository, times(1))
                .save(any(Member.class));
    }

    @DisplayName("회원가입 실패 - 이메일 중복")
    @Test
    void signUp_Fail_EmailDuplication() {
        when(memberRepository.existsByEmail(anyString())).thenReturn(true);

        assertThatThrownBy(() -> memberService.signUp(signUpRequest))
                .isInstanceOf(CustomException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.EMAIL_DUPLICATED);

        verify(memberRepository, times(1)).existsByEmail(signUpRequest.email());
        verify(memberRepository, never()).save(any(Member.class));
    }

    @DisplayName("회원정보 수정 성공")
    @Test
    void updateMember_Success() {
        Address updatedAddress = Address.builder()
                .street("부산광역시 해운대구 센텀중앙로 42")
                .detail("201호")
                .zipcode("48058")
                .build();

        MemberUpdateRequest updateRequest = MemberUpdateRequest.builder()
                .address(updatedAddress)
                .build();

        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));

        MemberResponse result = memberService.updateMember(1L, updateRequest);

        assertThat(result).isNotNull();
        assertThat(result.address()).isEqualTo(updatedAddress);

        verify(memberRepository, times(1)).findById(anyLong());
        // 트랜잭션 종료후 JPA에서 커밋을 진행하기때문에 save 메소드는 실행되지않는다.
        verify(memberRepository, never()).save(any(Member.class));
    }

    @DisplayName("회원 삭제 성공")
    @Test
    void deleteMember_Success() {
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
        doNothing().when(memberRepository).delete(any(Member.class));

        memberService.deleteMember(1L);

        verify(memberRepository, times(1)).findById(anyLong());
        verify(memberRepository, times(1)).delete(any(Member.class));
    }
}
