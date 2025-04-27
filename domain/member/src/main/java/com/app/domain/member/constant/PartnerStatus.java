package com.app.domain.member.constant;

import lombok.Getter;

@Getter
public enum PartnerStatus {
    VERIFIED,
    UNVERIFIED
    ;

    public String toStatusName() {
        if (this == VERIFIED) return "승인완료";
        else if (this == UNVERIFIED) return "미승인";
        else throw new IllegalArgumentException("존재하지 않는 파트너 상태입니다.");
    }
}
