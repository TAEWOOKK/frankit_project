package com.app.bo.product.dto.response;

import com.app.domain.product.constant.ProductOptionType;
import com.app.domain.product.entity.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailGetResponseDto {

    @Schema(description = "상품 ID", example = "1")
    private Long productId;

    @Schema(description = "상품 이름", example = "상품 이름")
    private String productName;

    @Schema(description = "상품 설명", example = "상품 설명입니다.")
    private String productDescription;

    @Schema(description = "상품 가격", example = "10000")
    private Integer productPrice;

    @Schema(description = "배송비", example = "3000")
    private Integer deliveryPrice;

    @Schema(description = "상품 등록 시간", example = "2023-10-01 12:00:00")
    private LocalDateTime createTime;

    @Schema(description = "상품 옵션", example = "[" +
            "{\"productOptionId\":1,\"productOptionName\":\"색상\",\"productOptionType\":\"SELECT\",\"productOptionDetailId\":1,\"productOptionDescription\":\"상세 옵션 이름\"}," +
            "{\"productOptionId\":2,\"productOptionName\":\"사이즈\",\"productOptionType\":\"INPUT\",\"productOptionDetailId\":2,\"productOptionDescription\":\"사용자 입력 사이즈\"}" +
            "]")
    private List<ProductOptionDetail> productOptionList;

    @Getter
    @AllArgsConstructor
    public static class ProductOptionDetail {

        @Schema(description = "상품 옵션 ID", example = "1")
        private Long productOptionId;

        @Schema(description = "상품 옵션 이름", example = "옵션 이름")
        private String productOptionName;

        @Schema(description = "상품 옵션 유형", example = "INPUT")
        private ProductOptionType productOptionType;

        @Schema(description = "상품 상세 옵션 ID(SELECT 일경우)", example = "1")
        private Long productOptionDetailId;

        @Schema(description = "상품 상세 옵션 이름", example = "상세 옵션 이름")
        private String productOptionDescription;
    }

    public static ProductDetailGetResponseDto toDto(Product product) {
        return new ProductDetailGetResponseDto(
            product.getProductId(),
            product.getProductName(),
            product.getProductDescription(),
            product.getProductPrice(),
            product.getDeliveryPrice(),
            product.getCreateTime(),
            product.getProductOptions().stream()
                .map(option -> new ProductOptionDetail(
                    option.getProductOptionId(),
                    option.getProductOptionName(),
                    option.getProductOptionType(),
                    option.getProductOptionType() == ProductOptionType.SELECT ?
                            option.getProductOptionDetail().getProductOptionDetailId() : null,
                    option.getProductOptionType() == ProductOptionType.SELECT ?
                            option.getProductOptionDetail().getProductOptionDetailName() : option.getProductOptionDescription())
                )
                .toList()
        );
    }
}
