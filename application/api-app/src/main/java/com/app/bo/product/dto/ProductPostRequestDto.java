package com.app.bo.product.dto;

import com.app.domain.product.constant.ProductOptionType;
import com.app.domain.product.entity.Product;
import com.app.domain.product.entity.ProductOption;
import com.app.domain.product.entity.ProductOptionDetail;
import com.app.error.ErrorType;
import com.app.error.exception.BadRequestException;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductPostRequestDto {

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
            "{\"productOptionName\":\"색상\",\"productOptionType\":\"SELECT\",\"productOptionDetailId\":1}," +
            "{\"productOptionName\":\"사이즈\",\"productOptionType\":\"INPUT\",\"productOptionDescription\":\"사용자 입력 사이즈\"}" +
            "]")
    private List<ProductOptionPostRequestDto> productOptionList;

    @Getter
    public static class ProductOptionPostRequestDto {

        @Schema(description = "상품 옵션 이름", example = "옵션 이름")
        private String productOptionName;

        @Schema(description = "상품 옵션 유형", example = "INPUT")
        private ProductOptionType productOptionType;

        @Schema(description = "상품 상세 옵션 ID(SELECT 일경우)", example = "1")
        private Long productOptionDetailId;

        @Schema(description = "상품 옵션 설명(INPUT 일경우)", example = "상품 옵션 설명")
        private String productOptionDescription;

        public ProductOption toEntity(ProductOptionDetail productOptionDetail, Product product) {
            return ProductOption.createProductOption(
                productOptionName,
                productOptionType,
                productOptionDescription,
                productOptionDetail,
                product
            );
        }
    }

    public Product toEntity(List<ProductOptionDetail> productOptionDetails) {

        // 상품 엔티티 생성
        Product product = Product.createProduct(
                productName,
                productDescription,
                productPrice,
                deliveryPrice,
                new ArrayList<>()
        );

        // 상품 옵션 엔티티 존재 여부 확인
        List<ProductOption> productOptions = new ArrayList<>();
        if (productOptionList == null || productOptionList.isEmpty()) {
            return product;
        }
        // 상품 옵션 엔티티 생성
        for (ProductOptionPostRequestDto productOptionPostRequestDto : productOptionList) {

            ProductOptionDetail productOptionDetail = null;

            if (productOptionPostRequestDto.getProductOptionDetailId() != null) {
                productOptionDetail = productOptionDetails.stream()
                        .filter(optionDetail -> optionDetail.getProductOptionDetailId().equals(productOptionPostRequestDto.getProductOptionDetailId()))
                        .findFirst()
                        .orElseThrow(() -> new BadRequestException(ErrorType.PRODUCT_OPTION_DETAIL_NOT_FOUND));
            }
            ProductOption productOption = productOptionPostRequestDto.toEntity(productOptionDetail, product);
            productOptions.add(productOption);
        }
        product.addProductOptions(productOptions);

        return product;
    }
}
