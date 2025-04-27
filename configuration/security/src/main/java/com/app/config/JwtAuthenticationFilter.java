package com.app.config;

import com.app.TokenManager;
import com.app.domain.member.constant.MemberStatus;
import com.app.domain.member.constant.Role;
import com.app.domain.member.entity.Member;
import com.app.domain.member.service.MemberService;
import com.app.error.ErrorType;
import com.app.error.exception.ForbiddenException;
import com.app.model.CustomUser;
import com.app.util.AuthorizationHeaderUtils;
import com.app.util.SecurityMemberUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.GenericFilterBean;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final TokenManager tokenManager;
    private final MemberService memberService;
    private final String activeProfile;

    /**
     * 해당 URI는 권한 검사 X
     */
    private final List<String> notAuthorizationUri = List.of(
        "/api/health",
        "/favicon.ico",
        "/error",
        "/auth/success",
        "/api/v1/fo/members/check-email",
        "/api/v1/fo/members/register",
        "/api/v1/fo/countries",
        "/api/v1/fo/localities",
        "/api/v1/auth/login",
        "/api/v1/auth/access-token/issue",
        "/api/v1/email/password-forgot/send",
        "/api/v1/members/password/reset"
    );

    /**
     * 해당 URI로 시작하는 경우 권한 검사 X
     */
    private final List<String> notAuthorizationUriStart = List.of(
        "/actuator",
        "/swagger-ui",
        "/v3/api-docs",
        "/api/v1/email"
    );

    private final List<String> auhorizationUri = List.of(
        "/api/v1/fo/assign/contract-engage"
    );

    @Override
    public void doFilter(
        ServletRequest request,
        ServletResponse response,
        FilterChain chain
    ) throws IOException, jakarta.servlet.ServletException {
        // TODO: 로컬에서 프론트/백엔드 풀스택으로 진행하는 경우 mock user가 아닌 실제 유저 데이터를 불러와야해서 임시로 주석처리하였음
//        if (activeProfile.equals("local")) {
//            mockAuthenticationInLocal(request, response, chain);
//            return;
//        }

        // 1. Authorization Header 검증
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        // 2. 인증 생략
        String requestURI = httpServletRequest.getRequestURI();
        if (
            (
                notAuthorizationUri.stream().anyMatch(uri -> uri.equals(requestURI)) ||
                    notAuthorizationUriStart.stream().anyMatch(requestURI::startsWith)
            )
                && auhorizationUri.stream().noneMatch(uri -> uri.equals(requestURI))
        ) {
            chain.doFilter(
                request,
                response
            );
        } else {
            String authorizationHeader = httpServletRequest.getHeader("Authorization");
            AuthorizationHeaderUtils.validateAuthorization(authorizationHeader);

            // 3. 토큰 검증
            String token = authorizationHeader.split(" ")[1];
            tokenManager.validateToken(token);

            // 4. 스프링 시큐리티에 인증정보 세팅
            var authentication = tokenManager.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 5. 어드민 API의 경우 어드민 유저만 실행 가능
            validateAdminRole(
                requestURI,
                authentication
            );

            // 6. ACTIVE 회원이 아닌 경우 API 호출 불가
            validateMemberStatus();

            chain.doFilter(
                request,
                response
            );
        }
    }

    private static void validateAdminRole(
        String requestURI,
        Authentication authentication
    ) {
        if (requestURI.startsWith("/api/v1/bo/")) {
            boolean hasAdminRole = authentication.getAuthorities().stream()
                                                 .map(GrantedAuthority::getAuthority)
                                                 .anyMatch(
                                                     role -> role.equals(Role.ROLE_ADMIN.name())
                                                         || role.equals(Role.ROLE_SUPER_ADMIN.name())
                                                         || role.equals(Role.ROLE_PARTNER.name())
                                                 );
            if (!hasAdminRole) {
                throw new ForbiddenException(ErrorType.NOT_ADMIN_MEMBER);
            }
        }
    }

    private void validateMemberStatus() {
        CustomUser customUser = SecurityMemberUtils.getCustomUser();
        Member member = memberService.findMemberByMemberId(customUser.getUserId());
        if (member.getStatus() == MemberStatus.STOP) {
            throw new ForbiddenException(ErrorType.MEMBER_STATUS_STOP);
        }
        if (member.getStatus() == MemberStatus.DEACTIVATE) {
            throw new ForbiddenException(ErrorType.MEMBER_STATUS_DEACTIVATE);
        }
    }

    private void mockAuthenticationInLocal(
        ServletRequest request,
        ServletResponse response,
        FilterChain chain
    ) {
        UserDetails localUser = new CustomUser(
            1L,
            "슈퍼관리자",
            "",
            List.of(new SimpleGrantedAuthority("ROLE_SUPER_ADMIN"))
        );
        // 특정 사용자의 Mock Authentication 객체 생성
        UsernamePasswordAuthenticationToken mockAuthentication =
            new UsernamePasswordAuthenticationToken(
                localUser,
                // 사용자 이름
                null,
                // 자격 증명(생략)
                List.of(new SimpleGrantedAuthority("ROLE_SUPER_ADMIN"))
                // 권한 목록
            );

        // Spring Security의 SecurityContext에 Authentication 설정
        SecurityContextHolder.getContext().setAuthentication(mockAuthentication);

        // 필터 체인 계속 진행
        try {
            chain.doFilter(
                request,
                response
            );
        } catch (Exception e) {
            log.error(
                "Mock Authentication Error : {}",
                e.getMessage(),
                e
            );
        }
    }
}