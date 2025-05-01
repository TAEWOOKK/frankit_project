package com.app.bo.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductOptionDetailPostResponseDto {

    @Schema(description = "상품 옵션 상세 ID", example = "1")
    private Long productOptionDetailId;
}
