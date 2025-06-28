package com.example.ecommerce_platform.member.entity;


import com.example.ecommerce_platform.member.domain.Address;
import com.example.ecommerce_platform.member.domain.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "MEMBERS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;       // 이메일

    @Column(name = "password", nullable = false)
    /*@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+=\\[{\\]};:<>|./?~-]).{8,20}$"
    ,message = "비밀번호는 영문, 숫자, 특수문자를 포함한 8~20자여야합니다.")*/
    private String password;

    @Column(name = "member_name", nullable = false)
    private String name;        // 이름

    @Embedded
    // @Embeddable 클래스를 가져옴. 이 내부에서 재정의를 안함. // 새로운 테이블로 재생성 x
    private Address address;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public Member(String email, String password, String name, Address address, Role role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.address = address;
        this.role = role;
    }

    @Builder
    public Member(Long id, String email, String password, String name, Address address, Role role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.address = address;
        this.role = role;
    }

    public void addAddress(Address address) {
        this.address = address;
    }

    /*// 테스트용이면 Builder나 생성자에서 처리
    public void setId(Long id){
        this.id = id;
    }*/
}

