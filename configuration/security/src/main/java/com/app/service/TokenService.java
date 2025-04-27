package com.app.service;

import com.app.TokenManager;
import com.app.constant.GrantType;
import com.app.domain.member.entity.Member;
import com.app.domain.member.entity.MemberRole;
import com.app.domain.member.repository.MemberRoleRepository;
import com.app.domain.member.service.MemberService;
import com.app.model.AccessTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenService {

    // Member 관련 비즈니스 로직을 처리하는 서비스
    private final MemberService memberService;

    // MemberRole 관련 데이터베이스 접근을 처리하는 리포지토리
    private final MemberRoleRepository memberRoleRepository;

    // JWT 토큰을 생성 및 관리하는 TokenManager
    private final TokenManager tokenManager;

    /**
     * 리프레시 토큰을 사용하여 새로운 액세스 토큰을 생성합니다.
     *
     * @param refreshToken 클라이언트로부터 받은 리프레시 토큰
     * @return 새로 생성된 액세스 토큰과 관련된 정보가 담긴 AccessTokenResponse 객체
     */
    public AccessTokenResponse createAccessTokenByRefreshToken(String refreshToken) {
        // 1. 리프레시 토큰을 통해 사용자 정보(Member) 조회
        Member member = memberService.findByRefreshToken(refreshToken);

        // 2. 사용자의 역할(MemberRole) 목록을 조회
        List<MemberRole> memberRoles = memberRoleRepository.findByMemberId(member.getMemberId());

        // 3. 사용자의 역할을 콤마(,)로 구분된 문자열로 변환
        List<String> roleList = memberRoles.stream().map(it -> it.getRole().name()).toList();
        String roles = String.join(",", roleList);

        // 4. 새로운 액세스 토큰의 만료 시간을 생성
        Date accessTokenExpireTime = tokenManager.createAccessTokenExpireTime();

        // 5. 새로운 액세스 토큰 생성
        String accessToken = tokenManager.createAccessToken(member.getMemberId(), roles, accessTokenExpireTime);

        // 6. AccessTokenResponse 객체 생성 및 반환
        return AccessTokenResponse.builder()
                .grantType(GrantType.BEARER.getType())  // 토큰 유형 설정 (예: Bearer)
                .accessToken(accessToken)               // 새로 생성된 액세스 토큰 설정
                .accessTokenExpireTime(accessTokenExpireTime)  // 액세스 토큰 만료 시간 설정
                .build();
    }
}
