package com.app.domain.member.repository;

import com.app.domain.member.constant.Role;
import com.app.domain.member.entity.QMemberRole;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRoleCustomRepository {

    public BooleanExpression equalRoleAdmin(){
        return QMemberRole.memberRole.role.eq(Role.ROLE_ADMIN);
    }

}
