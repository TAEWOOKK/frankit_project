package com.app.bo.product.dto.request;

import com.app.domain.product.constant.ProductOptionType;
import com.app.domain.product.entity.Product;
import com.app.domain.product.entity.ProductOptionDetail;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductPutRequestDto {

    @NotBlank
    @Schema(description = "상품 이름", example = "상품 이름")
    private String productName;

    @NotBlank
    @Schema(description = "상품 설명", example = "상품 설명입니다.")
    private String productDescription;

    @NotNull
    @Min(1)
    @Schema(description = "상품 가격", example = "10000")
    private Integer productPrice;

    @NotNull
    @Min(1)
    @Schema(description = "배송비", example = "3000")
    private Integer deliveryPrice;

    @Schema(description = "상품 옵션", example = "[" +
            "{\"productOptionId\":1,\"productOptionName\":\"색상\",\"productOptionType\":\"SELECT\",\"productOptionDetailId\":1}," +
            "{\"productOptionId\":2,\"productOptionName\":\"사이즈\",\"productOptionType\":\"INPUT\",\"productOptionDescription\":\"사용자 입력 사이즈\"}," +
            "{\"productOptionName\":\"소재\",\"productOptionType\":\"INPUT\",\"productOptionDescription\":\"사용자 입력 소재\"}" +
            "]")
    private List<ProductOption> productOptionList;

    @Getter
    public static class ProductOption {

        @Schema(description = "상품 옵션 ID", example = "1")
        private Long productOptionId;

        @Schema(description = "상품 옵션 이름", example = "옵션 이름")
        private String productOptionName;

        @Schema(description = "상품 옵션 유형", example = "INPUT")
        private ProductOptionType productOptionType;

        @Schema(description = "상품 상세 옵션 ID(SELECT 일경우)", example = "1")
        private Long productOptionDetailId;

        @Schema(description = "상품 옵션 설명(INPUT 일경우)", example = "상품 옵션 설명")
        private String productOptionDescription;
    }
}
