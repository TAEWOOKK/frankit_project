package com.app.domain.member.entity;

import com.app.common.BaseEntity;
import com.app.domain.member.constant.MemberStatus;
import com.app.error.ErrorType;
import com.app.error.exception.BadRequestException;
import com.app.util.DateUtils;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false, length = 200, updatable = false, unique = true)
    private String loginId;

    @Column(length = 50)
    private String email;

    @Column(length = 200)
    private String password;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(length = 250)
    private String refreshToken;

    private LocalDateTime tokenExpirationTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private MemberStatus status;

    private LocalDateTime deletedAt;

    @Builder(access = AccessLevel.PRIVATE)
    private Member(String email, String password, String name, String loginId, MemberStatus status) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.loginId = loginId;
        this.status = status;
    }

    public static Member createMember(String email, String password, String name, String loginId) {
        return Member.builder()
                .email(email)
                .password(password)
                .name(name)
                .loginId(loginId)
                .status(MemberStatus.ACTIVE)
                .build();
    }

    public void updateMemberInfo(String name) {
        this.name = name;
    }

    public void updateRefreshToken(String refreshToken, Date refreshTokenExpireTime) {
        this.refreshToken = refreshToken;
        this.tokenExpirationTime = DateUtils.convertToLocalDateTime(refreshTokenExpireTime);
    }

    public void expireRefreshToken(LocalDateTime now) {
        this.tokenExpirationTime = now;
    }

    /**
     * 회원 상태 검증
     */
    public void validateMemberStatus() {
        if (this.status == MemberStatus.DEACTIVATE) throw new BadRequestException(ErrorType.ALREADY_DEACTIVATE);
    }

    /**
     * 비밀번호, 비밀번호 확인 같은지 체크
     */
    public static void checkPasswordConfirmSame(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) throw new BadRequestException(ErrorType.NOT_SAME_PASSWORD);
    }

    public void updateStatus(MemberStatus status) {
        this.status = status;
    }
}
