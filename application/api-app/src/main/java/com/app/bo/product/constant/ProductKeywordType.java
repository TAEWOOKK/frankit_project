package com.app.bo.product.constant;

import lombok.Getter;

@Getter
public enum ProductKeywordType {

    PRODUCT_NAME("상품명"),
    PRODUCT_DESCRIPTION("상품 설명");

    private final String description;

    ProductKeywordType(String description) {
        this.description = description;
    }
}
