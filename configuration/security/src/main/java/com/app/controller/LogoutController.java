package com.app.controller;

import com.app.service.LogoutService;
import com.app.util.AuthorizationHeaderUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "로그인 관리", description = "로그인 후 JWT 토큰 반환/로그아웃/토큰재발급 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class LogoutController {

    private final LogoutService logoutService;

    /**
     * 로그아웃 요청을 처리하는 API 엔드포인트입니다.
     * Authorization 헤더에서 액세스 토큰을 추출하고, 해당 토큰을 사용해 로그아웃 처리를 수행합니다.
     *
     * @param httpServletRequest HTTP 요청 객체
     * @return 로그아웃 성공 메시지를 담은 ResponseEntity 객체
     */
    @Operation(summary = "로그아웃 API", description = "로그아웃시 refresh token 만료 처리")
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest httpServletRequest) {
        // 1. Authorization 헤더에서 액세스 토큰을 추출
        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        AuthorizationHeaderUtils.validateAuthorization(authorizationHeader); // 헤더 검증

        // 2. "Bearer" 이후의 액세스 토큰 부분만 추출
        String accessToken = authorizationHeader.split(" ")[1];

        // 3. 로그아웃 서비스 호출
        logoutService.logout(accessToken);

        // 4. 로그아웃 성공 응답 반환
        return ResponseEntity.ok("logout success");
    }

}
