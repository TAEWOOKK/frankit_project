package com.app.common.enums.controller;

import com.app.common.enums.dto.EnumsResponseDto;
import com.app.common.enums.service.EnumsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "enum 체크 API", description = "enum 필드를 확인하는 API")
@RestController
@RequestMapping("/api/v1/enums")
@RequiredArgsConstructor
public class EnumsController {
    private final EnumsService enumsService;

    @GetMapping
    public ResponseEntity<List<EnumsResponseDto>> getEnums() {
        return ResponseEntity.ok(
            enumsService.scanEnums()
        );
    }
}
