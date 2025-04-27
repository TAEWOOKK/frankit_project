package com.app.service;

import com.app.TokenManager;
import com.app.constant.TokenType;
import com.app.domain.member.entity.Member;
import com.app.domain.member.service.MemberService;
import com.app.error.ErrorType;
import com.app.error.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class LogoutService {

    // Member 관련 비즈니스 로직을 처리하는 서비스
    private final MemberService memberService;

    // JWT 토큰을 생성 및 검증하는 TokenManager
    private final TokenManager tokenManager;

    /**
     * 주어진 액세스 토큰을 사용하여 사용자를 로그아웃 처리합니다.
     *
     * @param accessToken 클라이언트로부터 받은 액세스 토큰
     */
    public void logout(String accessToken) {

        // 1. 토큰 검증
        // 주어진 액세스 토큰이 유효한지 검증합니다.
        tokenManager.validateToken(accessToken);

        // 2. 토큰 타입 확인
        // 토큰의 클레임(Claims)을 추출하여 토큰 타입이 액세스 토큰인지 확인합니다.
        Claims tokenClaims = tokenManager.getClaims(accessToken);
        String tokenType = tokenClaims.getSubject();
        if(!TokenType.isAccessToken(tokenType)) {
            // 토큰 타입이 액세스 토큰이 아닌 경우 예외를 발생시킵니다.
            throw new UnauthorizedException(ErrorType.NOT_ACCESS_TOKEN_TYPE);
        }

        // 3. 리프레시 토큰 만료 처리
        // 토큰에서 추출한 memberId를 사용하여 회원을 조회하고, 해당 회원의 리프레시 토큰을 만료 처리합니다.
        Long memberId = Long.valueOf((Integer)tokenClaims.get("memberId"));
        Member member = memberService.findMemberByMemberId(memberId);
        member.expireRefreshToken(LocalDateTime.now());  // 현재 시각을 기준으로 리프레시 토큰 만료 처리
    }

}
