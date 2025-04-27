package com.app.domain.member.repository;

import com.app.domain.member.entity.MemberRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRoleRepository extends JpaRepository<MemberRole, Long> {

    List<MemberRole> findByMemberId(Long memberId);

    // 여러 memberId로 조회
    List<MemberRole> findByMemberIdIn(List<Long> memberIds);

    void deleteByMemberId(Long memberId);

}
