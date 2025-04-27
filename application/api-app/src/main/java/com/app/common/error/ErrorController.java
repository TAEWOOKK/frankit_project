package com.app.common.error;

import com.app.model.ErrorTypeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "error", description = "에러 코드 리스트 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/error")
public class ErrorController {
    @Operation(summary = "에러 코드 리스트", description = "서버의 에러 코드를 전부 출력")
    @GetMapping("/error-list")
    public ResponseEntity<List<ErrorTypeResponse>> getErrorList() {
        return ResponseEntity.ok(
                ErrorTypeResponse.getErrorTypes()
        );
    }
}
