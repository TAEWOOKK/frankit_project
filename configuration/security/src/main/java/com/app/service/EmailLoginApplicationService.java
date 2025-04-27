package com.app.service;

import com.app.JwtToken;
import com.app.TokenManager;
import com.app.domain.member.entity.Member;
import com.app.domain.member.entity.MemberRole;
import com.app.domain.member.repository.MemberRepository;
import com.app.domain.member.repository.MemberRoleRepository;
import com.app.dto.EmailLoginRequestDto;
import com.app.error.ErrorType;
import com.app.error.exception.BadRequestException;
import com.app.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailLoginApplicationService {

    private final MemberRoleRepository memberRoleRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenManager tokenManager;

    @Transactional
    public JwtToken login(EmailLoginRequestDto requestDto) {

        Optional<Member> optionalMember = memberRepository.findByEmail(requestDto.getEmail());
        if (optionalMember.isEmpty()) {
            throw new EntityNotFoundException(ErrorType.MEMBER_NOT_EXISTS);
        }
        Member member = optionalMember.get();
        // 멤버 탈퇴 여부 확인
        member.validateMemberStatus();
        // 비밀번호 확인
        if (!passwordEncoder.matches(requestDto.getPassword(), member.getPassword())) {
            throw new BadRequestException(ErrorType.NOT_VALID_PASSWORD);
        }

        // 멤버 권한 조회
        List<MemberRole> memberRoles = memberRoleRepository.findByMemberId(member.getMemberId());

        // refresh token 업데이트
        JwtToken jwtTokenDto = tokenManager.createJwtTokenDto(member.getMemberId(), memberRoles);
        member.updateRefreshToken(jwtTokenDto.getRefreshToken(), jwtTokenDto.getRefreshTokenExpireTime());

        return jwtTokenDto;
    }
}
