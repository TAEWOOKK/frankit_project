package com.app.bo.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductPostResponseDto {

    @Schema(description = "상품 ID", example = "1")
    private Long productId;
}
