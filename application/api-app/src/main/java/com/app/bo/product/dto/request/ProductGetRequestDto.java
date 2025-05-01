package com.app.bo.product.dto.request;

import com.app.bo.product.constant.ProductKeywordType;
import com.app.bo.product.constant.ProductOrderType;
import com.app.model.PageRequest;
import com.querydsl.core.types.Order;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductGetRequestDto extends PageRequest {

    @Schema(description = "검색어 타입", example = "PRODUCT_NAME")
    private ProductKeywordType productKeywordType;

    @Schema(description = "검색어", example = "상품명")
    private String keyword;

    @Schema(description = "정렬 기준", example = "PRODUCT_NAME")
    private ProductOrderType productOrderType;

    @Schema(description = "정렬 차순", example = "ASC")
    private Order order;
}
