package com.app.config;

import com.app.TokenManager;
import com.app.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터 체인에 등록됨
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

	// JWT 토큰을 관리하는 TokenManager
	private final TokenManager tokenManager;

	private final MemberService memberService;

	@Value("${spring.config.activate.on-profile}")
	private String activeProfile;

	/**
	 * Spring Security 설정을 정의하는 메서드입니다. 이 메서드는 HTTP 보안 설정, 세션 관리, OAuth2 로그인, JWT 인증 필터 등을 설정합니다.
	 *
	 * @param http HTTP 보안 설정을 위한 HttpSecurity 객체
	 * @return SecurityFilterChain 객체를 반환하여 Spring Security 설정을 적용
	 * @throws Exception 예외가 발생할 수 있습니다.
	 */
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http
			// HTTP 기본 인증 비활성화
			.httpBasic(httpSecurity -> httpSecurity.disable())

			// CSRF 보호 비활성화 (JWT 사용 시 비활성화가 일반적)
			.csrf(httpSecurity -> httpSecurity.disable())

			// 세션 관리 설정: 상태를 유지하지 않는 Stateless 방식으로 설정 (세션을 사용하지 않음)
			.sessionManagement(sessionManagementConfigurer ->
				sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

			// JWT 인증 필터를 UsernamePasswordAuthenticationFilter 앞에 추가
			.addFilterBefore(
				new JwtAuthenticationFilter(tokenManager, memberService, activeProfile),
				UsernamePasswordAuthenticationFilter.class
			)

			// JWT 관련 예외를 처리하는 필터를 JwtAuthenticationFilter 앞에 추가
			.addFilterBefore(
				new JwtExceptionHandlerFilter(), JwtAuthenticationFilter.class
			)

			// 설정을 기반으로 SecurityFilterChain 객체 생성
			.build();
	}

	/**
	 * 비밀번호를 암호화하기 위한 PasswordEncoder 빈(bean)을 정의합니다.
	 * PasswordEncoderFactories.createDelegatingPasswordEncoder()를 사용하여 다양한 인코딩 방식 중 가장 적합한 방식을 선택할
	 * 수 있습니다.
	 *
	 * @return PasswordEncoder 객체
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
}
