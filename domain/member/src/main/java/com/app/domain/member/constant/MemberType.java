package com.app.domain.member.constant;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum MemberType {

    GOOGLE,
    NAVER,
    KAKAO,
    EMAIL
    ;

    public static MemberType from(String type) {
        return MemberType.valueOf(type.toUpperCase());
    }

    public static boolean isMemberType(String type) {
        List<MemberType> memberTypes = Arrays.stream(MemberType.values())
                .filter(memberType -> memberType.name().equals(type))
                .collect(Collectors.toList());
        return memberTypes.size() != 0;
    }

    public String toMemberTypeName() {
        if (this == GOOGLE) return "구글";
        else if (this == NAVER) return "네이버";
        else if (this == KAKAO) return "카카오";
        else if (this == EMAIL) return "이메일";
        else throw new IllegalArgumentException("존재하지 않는 가입 유형입니다.");
    }

}
