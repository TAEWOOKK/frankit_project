package com.app.domain.product.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductOptionDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productOptionDetailId;

    private String productOptionDetailName;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_option_id", nullable = false)
    private ProductOption productOption;
}
