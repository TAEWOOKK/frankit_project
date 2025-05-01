package com.app.domain.product.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductOptionDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productOptionDetailId;

    @Column(nullable = false, unique = true)
    private String productOptionDetailName;

    @Builder(access = AccessLevel.PRIVATE)
    public ProductOptionDetail(String productOptionDetailName) {
        this.productOptionDetailName = productOptionDetailName;
    }

    public static ProductOptionDetail createProductOptionDetail(String productOptionDetailName) {
        return ProductOptionDetail.builder()
            .productOptionDetailName(productOptionDetailName)
            .build();
    }
}
