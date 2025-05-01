package com.app.bo.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ProductGetResponseDto {

    @Schema(description = "상품 ID", example = "1")
    private Long productId;

    @Schema(description = "상품명", example = "상품명")
    private String productName;

    @Schema(description = "상품 설명", example = "상품 설명")
    private String productDescription;

    @Schema(description = "상품 가격", example = "10000")
    private Integer productPrice;

    @Schema(description = "배송비", example = "3000")
    private Integer deliveryPrice;

    @Schema(description = "생성 기간", example = "2023-01-01")
    private LocalDate createdDate;

    public ProductGetResponseDto(Long productId,
                                 String productName,
                                 String productDescription,
                                 Integer productPrice,
                                 Integer deliveryPrice,
                                 LocalDateTime createTime
    ) {
        this.productId = productId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.deliveryPrice = deliveryPrice;
        this.createdDate = createTime.toLocalDate();
    }
}
