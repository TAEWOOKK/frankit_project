package com.app.domain.member.repository;

import com.app.domain.member.constant.MemberStatus;
import com.app.domain.member.constant.MemberType;
import com.app.util.DateUtils;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

import static com.app.domain.member.entity.QMember.member;

@Repository
public class MemberCustomRepository {

    public BooleanExpression isCreatedAtBetween(LocalDate startTime, LocalDate endTime) {
        if (startTime == null || endTime == null) return null;
        return member.createTime.between(
                DateUtils.getSearchStartDateTime(startTime), DateUtils.getSearchEndDateTime(endTime)
        );
    }

    public BooleanExpression equalMemberStatus(MemberStatus memberStatus){
        return memberStatus == null ? null : member.status.eq(memberStatus);
    }

}
