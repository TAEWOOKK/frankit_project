package com.app.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmailLoginRequestDto {

    @Email
    @Schema(description = "이메일", example = "admin@admin.com", required = true)
    private String email;

    @NotBlank
    @Schema(description = "비밀번호", example = "Admin1234", required = true)
    private String password;

}
