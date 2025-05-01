package com.app.domain.member.service;

import com.app.domain.member.constant.Role;
import com.app.domain.member.entity.Member;
import com.app.domain.member.entity.MemberRole;
import com.app.domain.member.repository.MemberRepository;
import com.app.domain.member.repository.MemberRoleRepository;
import com.app.error.ErrorType;
import com.app.error.exception.EntityNotFoundException;
import com.app.error.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 회원과 관련된 비즈니스 로직을 처리하는 서비스 클래스입니다.
 * 이 클래스는 회원 가입, 회원 정보 조회, 권한 관리 등의 기능을 제공합니다.
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    // 회원 저장소를 참조하기 위한 리포지토리
    private final MemberRepository memberRepository;
    private final MemberRoleRepository memberRoleRepository;

    /**
     * 주어진 리프레시 토큰으로 회원을 조회하는 메서드입니다.
     * 토큰이 만료된 경우 인증 예외를 발생시킵니다.
     *
     * @param refreshToken 리프레시 토큰
     * @return 리프레시 토큰에 해당하는 회원 엔티티
     * @throws UnauthorizedException 리프레시 토큰이 유효하지 않거나 만료된 경우 발생
     */
    public Member findByRefreshToken(String refreshToken) {
        // 리프레시 토큰으로 회원 조회
        Member member = memberRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new UnauthorizedException(ErrorType.REFRESH_TOKEN_NOT_FOUND));

        // 토큰 만료 시간 확인
        LocalDateTime tokenExpirationTime = member.getTokenExpirationTime();
        if (tokenExpirationTime.isBefore(LocalDateTime.now())) {
            // 토큰이 만료된 경우 예외 발생
            throw new UnauthorizedException(ErrorType.REFRESH_TOKEN_EXPIRED);
        }

        // 유효한 회원 반환
        return member;
    }

    /**
     * 회원 ID로 회원을 조회하는 메서드입니다.
     * 회원이 존재하지 않을 경우 예외를 발생시킵니다.
     *
     * @param memberId 조회할 회원의 ID
     * @return 해당 회원 ID를 가진 회원 엔티티
     * @throws EntityNotFoundException 회원이 존재하지 않는 경우 발생
     */
    public Member findMemberByMemberId(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorType.MEMBER_NOT_EXISTS));
    }

    @Transactional
    public Member registerMember(Member member, List<Role> roles) {

        // 회원 정보를 저장
        member = memberRepository.save(member);
        Long memberId = member.getMemberId();

        // 회원 역할 정보를 저장
        List<MemberRole> memberRoles = roles.stream().map(role -> MemberRole.of(role, memberId)).toList();
        memberRoleRepository.saveAll(memberRoles);

        // 등록된 회원을 반환
        return member;
    }
}

