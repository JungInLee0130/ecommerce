package com.example.ecommerce_platform.member.controller;

import com.example.ecommerce_platform.common.exception.CustomException;
import com.example.ecommerce_platform.common.exception.ErrorCode;
import com.example.ecommerce_platform.common.response.ApiResponse;
import com.example.ecommerce_platform.member.dto.request.MemberSignUpRequest;
import com.example.ecommerce_platform.member.dto.request.MemberUpdateRequest;
import com.example.ecommerce_platform.member.dto.response.MemberResponse;
import com.example.ecommerce_platform.member.dto.response.MemberSignUpResponse;
import com.example.ecommerce_platform.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<MemberSignUpResponse>> signUp(
            @Valid @RequestBody MemberSignUpRequest request) {
        if (!request.password().equals(request.passwordConfirm())) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
        }

        MemberSignUpResponse response = memberService.signUp(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("회원가입이 성공적으로 완료되었습니다.", response));
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<ApiResponse<MemberResponse>> getMemberById(@PathVariable Long memberId) {
        MemberResponse response = memberService.findMemberById(memberId);
        return ResponseEntity.ok(ApiResponse.success("회원 조회 성공", response));
    }

    @PatchMapping("/{memberId}")
    public ResponseEntity<ApiResponse<MemberResponse>> updateMember(
            @PathVariable Long memberId,
            @Valid @RequestBody MemberUpdateRequest request) {
        MemberResponse response = memberService.updateMember(memberId, request);
        return ResponseEntity.ok(ApiResponse.success("회원 정보 수정 성공", response));
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<ApiResponse<Void>> deleteMember(
            @PathVariable Long memberId) {
        memberService.deleteMember(memberId);
        return ResponseEntity.noContent().build(); // 204
    }
}
