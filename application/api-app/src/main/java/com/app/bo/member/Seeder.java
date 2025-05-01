package com.app.bo.member;

import com.app.domain.member.constant.MemberStatus;
import com.app.domain.member.constant.MemberType;
import com.app.domain.member.constant.Role;
import com.app.domain.member.entity.Member;
import com.app.domain.member.repository.MemberRepository;
import com.app.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Profile({"local"})
@Component
@RequiredArgsConstructor
public class Seeder implements ApplicationRunner {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberService memberService;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        createAdminMember();
    }

    private void createAdminMember() {
        String email = "test@admin.com";

        if (memberRepository.findByEmail(email).isPresent()) {
            return;
        }

        Member member = Member.createMember(
                email,
                passwordEncoder.encode("1234"),
                "어드민 테스트 계정",
                ""
        );
        memberService.registerMember(member, List.of(Role.ROLE_SUPER_ADMIN, Role.ROLE_ADMIN));
    }
}
