package com.app.controller;

import com.app.JwtToken;
import com.app.dto.EmailLoginRequestDto;
import com.app.service.EmailLoginApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "로그인 관리")
@RestController
@RequestMapping(("/api/v1/auth"))
@RequiredArgsConstructor
public class EmailLoginController {

    private final EmailLoginApplicationService emailLoginApplicationService;

    @Operation(summary = "이메일 로그인 API", description = "이메일 기반 로그인 API")
    @PostMapping("/login")
    public ResponseEntity<JwtToken> login(
            @RequestBody @Validated EmailLoginRequestDto requestDto
    ) {
        return ResponseEntity.ok(
                emailLoginApplicationService.login(requestDto)
        );
    }

}
