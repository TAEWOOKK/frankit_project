package com.app.bo.product.dto.request;

import com.app.domain.product.entity.ProductOptionDetail;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductOptionDetailPostRequestDto {

    @NotBlank
    @Schema(description = "상품 옵션 상세 이름", example = "상세 옵션 이름")
    private String productOptionDetailName;

    public ProductOptionDetail toEntity() {
        return ProductOptionDetail.createProductOptionDetail(productOptionDetailName);
    }
}
