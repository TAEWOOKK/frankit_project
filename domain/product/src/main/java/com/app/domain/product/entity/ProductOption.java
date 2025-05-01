package com.app.domain.product.entity;

import com.app.domain.product.constant.ProductOptionType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ProductOptionType productOptionType;

    private String productOptionDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_option_detail_id")
    private ProductOptionDetail productOptionDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Builder(access = AccessLevel.PRIVATE)
    public ProductOption(String productOptionName,
                         ProductOptionType productOptionType,
                         String productOptionDescription,
                            ProductOptionDetail productOptionDetail,
                         Product product
    ) {
        this.productOptionName = productOptionName;
        this.productOptionType = productOptionType;
        this.productOptionDescription = productOptionDescription;
        this.productOptionDetail = productOptionDetail;
        this.product = product;
    }

    public static ProductOption createProductOption(String productOptionName,
                                                    ProductOptionType productOptionType,
                                                    String productOptionDescription,
                                                    ProductOptionDetail productOptionDetail,
                                                    Product product
    ) {
        return ProductOption.builder()
                .productOptionName(productOptionName)
                .productOptionType(productOptionType)
                .productOptionDescription(productOptionDescription)
                .productOptionDetail(productOptionDetail)
                .product(product)
                .build();
    }

    public void setProduct(Product product) {
        this.product = product;

        if(!product.getProductOptions().contains(this)){
            product.getProductOptions().add(this);
        }
    }

    public void updateProductOption(String productOptionName,
                                 ProductOptionType productOptionType,
                                 String productOptionDescription,
                                 ProductOptionDetail productOptionDetail
    ) {
        this.productOptionName = productOptionName;
        this.productOptionType = productOptionType;
        this.productOptionDescription = productOptionDescription;
        this.productOptionDetail = productOptionDetail;
    }
}
