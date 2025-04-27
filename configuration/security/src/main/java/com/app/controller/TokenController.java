package com.app.controller;

import com.app.dto.RefreshTokenDto;
import com.app.model.AccessTokenResponse;
import com.app.service.TokenService;
import com.app.util.AuthorizationHeaderUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "로그인 관리", description = "로그인 후 JWT 토큰 반환/로그아웃/토큰재발급 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class TokenController {

    private final TokenService tokenService;

    /**
     * 리프레시 토큰을 사용하여 새로운 액세스 토큰을 발급하는 API 엔드포인트입니다.
     *
     * @param httpServletRequest HTTP 요청 객체
     * @return 새로 발급된 액세스 토큰을 담은 AccessTokenResponse 객체
     */
    @Operation(summary = "Access Token 재발급 API", description = "Access Token 재발급 API")
    @PostMapping("/access-token/issue")
    public ResponseEntity<AccessTokenResponse> createAccessToken(@RequestBody RefreshTokenDto refreshTokenDto) {

        // 1. 리프레시 토큰을 사용해 새로운 액세스 토큰 생성
        AccessTokenResponse accessTokenResponse = tokenService.createAccessTokenByRefreshToken(refreshTokenDto.getRefreshToken());

        // 2. 새로운 액세스 토큰 응답 반환
        return ResponseEntity.ok(accessTokenResponse);
    }

}
