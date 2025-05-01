package com.app.bo.product.controller;

import com.app.bo.product.dto.request.ProductGetRequestDto;
import com.app.bo.product.dto.request.ProductOptionDetailPostRequestDto;
import com.app.bo.product.dto.request.ProductPutRequestDto;
import com.app.bo.product.dto.response.*;
import com.app.bo.product.dto.request.ProductPostRequestDto;
import com.app.bo.product.service.BoProductApplicationService;
import com.app.common.dto.PageResponse;
import com.app.constant.ResultCode;
import com.app.model.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "상품 리스트 조회", description = "상품 리스트 조회")
    @GetMapping
    public ResponseEntity<PageResponse<ProductGetResponseDto>> getProductList(ProductGetRequestDto request) {
        return ResponseEntity.ok(boProductApplicationService.getProductListPages(request));
    }

    @Operation(summary = "상품 상세 조회", description = "상품 상세 조회")
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDetailGetResponseDto> getProductDetail(@PathVariable Long productId) {
        return ResponseEntity.ok(boProductApplicationService.getProductDetail(productId));
    }

    @Operation(summary = "상품 수정", description = "상품 수정")
    @PutMapping("/{productId}")
    public ResponseEntity<ProductPutResponseDto> updateProduct(@PathVariable Long productId, @RequestBody @Valid ProductPutRequestDto request) {
        return ResponseEntity.ok(boProductApplicationService.updateProduct(productId, request));
    }

    @Operation(summary = "상품 삭제", description = "상품 삭제")
    @DeleteMapping("/{productId}")
    public ResponseEntity<SuccessResponse> deleteProduct(@PathVariable Long productId) {
        boProductApplicationService.deleteProduct(productId);
        return ResponseEntity.ok(new SuccessResponse(ResultCode.SUCCESS));
    }

    @Operation(summary = "상품 옵션 상세 삭제", description = "상품 옵션 상세 삭제")
    @DeleteMapping("/{productOptionDetailId}/option-detail")
    public ResponseEntity<SuccessResponse> deleteProductOptionDetail(@PathVariable Long productOptionDetailId) {
        boProductApplicationService.deleteProductOptionDetail(productOptionDetailId);
        return ResponseEntity.ok(new SuccessResponse(ResultCode.SUCCESS));
    }
}
