package com.app.bo.product.constant;

import lombok.Getter;

@Getter
public enum ProductOrderType {

    PRODUCT_NAME("상품명"),
    CREATE_TIME("등록 시간");

    private final String description;

    ProductOrderType(String description) {
        this.description = description;
    }
}
