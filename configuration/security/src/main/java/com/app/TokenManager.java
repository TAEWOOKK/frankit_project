package com.app;

import com.app.constant.GrantType;
import com.app.constant.TokenType;
import com.app.domain.member.entity.MemberRole;
import com.app.error.ErrorType;
import com.app.error.exception.UnauthorizedException;
import com.app.model.CustomUser;
import com.app.model.PrincipalDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class TokenManager {

    // 액세스 토큰의 만료 시간 (밀리초 단위)
    private final String accessTokenExpirationTime;

    // 리프레시 토큰의 만료 시간 (밀리초 단위)
    private final String refreshTokenExpirationTime;

    // JWT 토큰을 생성 및 검증할 때 사용하는 비밀 키
    private final String tokenSecret;

    public JwtToken createJwtTokenDto(Long memberId, List<MemberRole> memberRoles) {
        Date accessTokenExpireTime = createAccessTokenExpireTime();
        Date refreshTokenExpireTime = createRefreshTokenExpireTime();

        String roles = memberRoles.stream()
                .map(role -> role.getRole().name())
                .collect(Collectors.joining(","));

        String accessToken = createAccessToken(memberId, roles, accessTokenExpireTime);
        String refreshToken = createRefreshToken(memberId, refreshTokenExpireTime);
        return JwtToken.builder()
                .grantType(GrantType.BEARER.getType())
                .accessToken(accessToken)
                .accessTokenExpireTime(accessTokenExpireTime)
                .refreshToken(refreshToken)
                .refreshTokenExpireTime(refreshTokenExpireTime)
                .build();
    }

    /**
     * 주어진 인증 정보를 바탕으로 JWT 토큰을 생성하고, 이를 포함하는 JwtToken 객체를 반환합니다.
     *
     * @param authentication 인증 정보를 담고 있는 Authentication 객체
     * @return 생성된 JwtToken 객체
     */
    public JwtToken createJwtTokenDto(Authentication authentication) {
        // 1. 토큰 만료 시간 생성
        Date accessTokenExpireTime = createAccessTokenExpireTime();
        Date refreshTokenExpireTime = createRefreshTokenExpireTime();

        // 2. PrincipalDetails에서 memberId 추출
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Long memberId = principalDetails.getMember().getMemberId();

        // 3. 권한 정보를 문자열로 변환
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        // 4. 액세스 토큰 및 리프레시 토큰 생성
        String accessToken = createAccessToken(memberId, roles, accessTokenExpireTime);
        String refreshToken = createRefreshToken(memberId, refreshTokenExpireTime);

        // 5. JwtToken 객체 생성 및 반환
        return JwtToken.builder()
                .grantType(GrantType.BEARER.getType())  // 토큰 타입 설정 (예: Bearer)
                .accessToken(accessToken)               // 액세스 토큰 설정
                .accessTokenExpireTime(accessTokenExpireTime)  // 액세스 토큰 만료 시간 설정
                .refreshToken(refreshToken)             // 리프레시 토큰 설정
                .refreshTokenExpireTime(refreshTokenExpireTime)  // 리프레시 토큰 만료 시간 설정
                .build();
    }

    /**
     * 액세스 토큰의 만료 시간을 생성합니다.
     *
     * @return 생성된 액세스 토큰의 만료 시간
     */
    public Date createAccessTokenExpireTime() {
        return new Date(System.currentTimeMillis() + Long.parseLong(accessTokenExpirationTime));
    }

    /**
     * 리프레시 토큰의 만료 시간을 생성합니다.
     *
     * @return 생성된 리프레시 토큰의 만료 시간
     */
    public Date createRefreshTokenExpireTime() {
        return new Date(System.currentTimeMillis() + Long.parseLong(refreshTokenExpirationTime));
    }

    /**
     * 주어진 사용자 ID, 역할, 만료 시간을 바탕으로 액세스 토큰을 생성합니다.
     *
     * @param memberId 사용자 ID
     * @param roles    사용자 역할
     * @param expirationTime 토큰 만료 시간
     * @return 생성된 액세스 토큰
     */
    public String createAccessToken(Long memberId, String roles, Date expirationTime) {
        // 액세스 토큰에 사용자의 역할 정보를 포함시키는 이유:
        // 사용자의 권한(roles)을 액세스 토큰에 포함시킴으로써, 서버는 토큰만으로도 사용자의 권한을 확인할 수 있습니다.
        // 이렇게 하면, 매 요청마다 데이터베이스에서 사용자의 권한을 조회할 필요가 없어져 성능이 향상됩니다.
        String accessToken = Jwts.builder()
                .setSubject(TokenType.ACCESS.name())    // 토큰 제목 설정
                .setIssuedAt(new Date())                // 토큰 발급 시간 설정
                .setExpiration(expirationTime)          // 토큰 만료 시간 설정
                .claim("memberId", memberId)            // 사용자 ID 설정
                .claim("roles", roles)                  // 사용자 역할 설정
                .signWith(SignatureAlgorithm.HS512, tokenSecret.getBytes(StandardCharsets.UTF_8))  // 서명 알고리즘 및 비밀 키 설정
                .setHeaderParam("typ", "JWT")           // 토큰 유형 설정
                .compact();
        return accessToken;
    }

    /**
     * 주어진 사용자 ID와 만료 시간을 바탕으로 리프레시 토큰을 생성합니다.
     *
     * @param memberId 사용자 ID
     * @param expirationTime 토큰 만료 시간
     * @return 생성된 리프레시 토큰
     */
    public String createRefreshToken(Long memberId, Date expirationTime) {
        String refreshToken = Jwts.builder()
                .setSubject(TokenType.REFRESH.name())   // 토큰 제목 설정
                .setIssuedAt(new Date())                // 토큰 발급 시간 설정
                .setExpiration(expirationTime)          // 토큰 만료 시간 설정
                .claim("memberId", memberId)            // 사용자 ID 설정
                .signWith(SignatureAlgorithm.HS512, tokenSecret.getBytes(StandardCharsets.UTF_8))  // 서명 알고리즘 및 비밀 키 설정
                .setHeaderParam("typ", "JWT")           // 토큰 유형 설정
                .compact();
        return refreshToken;
    }

    /**
     * 주어진 토큰의 유효성을 검증합니다.
     *
     * @param token 검증할 JWT 토큰
     * @throws UnauthorizedException 유효하지 않거나 만료된 토큰인 경우 예외 발생
     */
    public void validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(tokenSecret.getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(token);  // 토큰을 파싱하고 검증
        } catch (ExpiredJwtException e) {
            log.info("token 만료", e);  // 토큰이 만료된 경우 로그 기록
            throw new UnauthorizedException(ErrorType.TOKEN_EXPIRED);  // 만료 예외 발생
        } catch (Exception e) {
            log.info("유효하지 않은 token", e);  // 유효하지 않은 토큰인 경우 로그 기록
            throw new UnauthorizedException(ErrorType.NOT_VALID_TOKEN);  // 유효하지 않은 토큰 예외 발생
        }
    }

    /**
     * 주어진 토큰에서 클레임(Claims)을 추출합니다.
     *
     * @param token JWT 토큰
     * @return 추출된 클레임(Claims) 객체
     * @throws UnauthorizedException 유효하지 않은 토큰인 경우 예외 발생
     */
    public Claims getClaims(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(tokenSecret.getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(token).getBody();  // 토큰에서 클레임을 파싱하고 추출
        } catch (Exception e) {
            log.info("유효하지 않은 token", e);  // 유효하지 않은 토큰인 경우 로그 기록
            throw new UnauthorizedException(ErrorType.NOT_VALID_TOKEN);  // 유효하지 않은 토큰 예외 발생
        }
        return claims;
    }

    /**
     * 주어진 JWT 토큰에서 인증 정보를 추출합니다.
     *
     * @param token JWT 토큰
     * @return 인증 정보를 담고 있는 Authentication 객체
     */
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);  // 토큰에서 클레임 추출

        // Claims에서 역할(roles)과 회원 ID 추출
        String roles = (String) claims.get("roles");
        if (roles == null) {
            throw new RuntimeException("잘못된 토큰입니다.");  // 역할이 없는 경우 예외 발생
        }

        Long memberId = Long.valueOf((Integer) claims.get("memberId"));
        if (memberId == null) {
            throw new RuntimeException("잘못된 토큰입니다.");  // 회원 ID가 없는 경우 예외 발생
        }

        // 권한 정보 추출
        Collection<GrantedAuthority> authorities = List.of(roles.split(","))
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        UserDetails principal = new CustomUser(memberId, claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }
}
