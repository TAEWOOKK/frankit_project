package com.app.domain.member.entity;

import com.app.common.BaseEntity;
import com.app.domain.member.constant.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * MemberRole 클래스는 회원의 역할을 나타내는 엔티티입니다.
 * 회원은 여러 개의 역할(Role)을 가질 수 있으며, 이 클래스는 그 역할을 매핑합니다.
 */
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberRole extends BaseEntity {

    // 기본 키 (Primary Key)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // 역할 (Role) 필드, ENUM 타입으로 저장
    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private Role role;

    // 회원 ID를 참조하는 필드
    @Column(nullable = false)
    private Long memberId;

    /**
     * 역할과 회원 ID를 기반으로 MemberRole 객체를 생성하는 팩토리 메서드입니다.
     *
     * @param role 회원의 역할
     * @param memberId 회원의 ID
     * @return 생성된 MemberRole 객체
     */
    public static MemberRole of(Role role, Long memberId) {
        return new MemberRole(null, role, memberId);
    }
}

