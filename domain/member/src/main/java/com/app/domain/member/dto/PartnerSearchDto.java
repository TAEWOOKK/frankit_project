package com.app.domain.member.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class PartnerSearchDto {
    private String companyName;
    private String email;
    private LocalDate year;

    @Builder
    private PartnerSearchDto(String companyName, String email, LocalDate year) {
        this.companyName = companyName;
        this.email = email;
        this.year = year;
    }

    public static PartnerSearchDto of(String companyName, String email, LocalDate year) {
        return PartnerSearchDto.builder()
                .companyName(companyName)
                .email(email)
                .year(year)
                .build();
    }
}
