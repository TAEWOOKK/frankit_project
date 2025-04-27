package com.app.domain.member.constant;

import lombok.Getter;

@Getter
public enum MemberStatus {
    ACTIVE("활성 상태"),       // 활성 상태
    DEACTIVATE("탈퇴 상태"),   // 탈퇴 상태
    STOP("중단 상태"),         // 중단 상태
    ;

    private final String description;

    MemberStatus(String description) {
        this.description = description;
    }


    public String toStatusName() {
        if (this == STOP) return "중지회원";
        else if (this == DEACTIVATE) return "탈퇴회원";
        else if (this == ACTIVE) return "회원";
        else throw new IllegalArgumentException("존재하지 않는 회원 상태입니다.");
    }

}
