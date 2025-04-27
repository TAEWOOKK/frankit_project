package com.app.domain.product.constant;

import lombok.Getter;

@Getter
public enum ProductOptionType {
    INPUT("입력타입"),
    SELECT("선택타입");

    private final String description;

    ProductOptionType(String description) {
        this.description = description;
    }
}
