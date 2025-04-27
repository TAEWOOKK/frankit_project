package com.app.domain.member.constant;

import lombok.Getter;

@Getter
public enum Role {

    ROLE_USER, ROLE_ADMIN, ROLE_SUPER_ADMIN, ROLE_PARTNER;

    public static Role from(String role) {
        return Role.valueOf(role);
    }

    public String getRoleName() {
        if (this == ROLE_USER) return "회원";
        else if (this == ROLE_PARTNER) return "파트너사";
        else if (this == ROLE_ADMIN) return "관리자";
        else if (this == ROLE_SUPER_ADMIN) return "슈퍼 관리자";
        else throw new IllegalArgumentException("존재하지 않는 역할입니다.");
    }

}
