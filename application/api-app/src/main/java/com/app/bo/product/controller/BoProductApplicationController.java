package com.app.bo.product.controller;

import com.app.bo.product.dto.request.ProductOptionDetailPostRequestDto;
import com.app.bo.product.dto.response.ProductOptionDetailPostResponseDto;
import com.app.bo.product.dto.request.ProductPostRequestDto;
import com.app.bo.product.dto.response.ProductPostResponseDto;
import com.app.bo.product.service.BoProductApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "BO 상품 API", description = "BO 상품 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/bo/product")
public class BoProductApplicationController {

    private final BoProductApplicationService boProductApplicationService;

    @Operation(summary = "상품 등록", description = "상품 등록")
    @PostMapping
    public ResponseEntity<ProductPostResponseDto> createProduct(@RequestBody @Valid ProductPostRequestDto request) {
        return ResponseEntity.ok(boProductApplicationService.createProduct(request));
    }

    @Operation(summary = "상품 옵션 상세 등록", description = "상품 옵션 상세 등록")
    @PostMapping("/option-detail")
    public ResponseEntity<ProductOptionDetailPostResponseDto> createProductOptionDetail(@RequestBody @Valid ProductOptionDetailPostRequestDto request) {
        return ResponseEntity.ok(boProductApplicationService.createProductOptionDetail(request));
    }
}
