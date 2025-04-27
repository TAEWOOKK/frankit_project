package com.app.domain.product.entity;

import com.app.domain.product.constant.ProductOptionType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productOptionId;

    @Column(nullable = false)
    private String productOptionName;

    @Enumerated
    @Column(nullable = false, length = 20)
    private ProductOptionType productOptionType;

    private String productOptionDescription;

    //TODO LAZY 안써도 n+1 안터지는지 확인필요
    @OneToOne(mappedBy = "productOption", cascade = CascadeType.ALL, orphanRemoval = true)
    private ProductOptionDetail productOptionDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
