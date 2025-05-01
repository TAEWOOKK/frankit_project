package com.app.domain.product.entity;

import com.app.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String productDescription;

    @Column(nullable = false)
    @Check(constraints = "product_price >= 0")
    private Integer productPrice;

    @Column(nullable = false)
    @Check(constraints = "delivery_price >= 0")
    private Integer deliveryPrice;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductOption> productOptions = new ArrayList<>();

    @Builder(access = AccessLevel.PRIVATE)
    public Product(String productName,
                   String productDescription,
                   Integer productPrice,
                   Integer deliveryPrice,
                   List<ProductOption> productOptions
    ) {
        this.productName = productName;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.deliveryPrice = deliveryPrice;
        this.productOptions = productOptions != null ? productOptions : new ArrayList<>();
    }

    public static Product createProduct(String productName,
                                        String productDescription,
                                        Integer productPrice,
                                        Integer deliveryPrice,
                                        List<ProductOption> productOptions
    ) {
        return Product.builder()
                .productName(productName)
                .productDescription(productDescription)
                .productPrice(productPrice)
                .deliveryPrice(deliveryPrice)
                .productOptions(productOptions)
                .build();
    }

    public void addProductOptions(List<ProductOption> productOptionList) {
        this.productOptions.addAll(productOptionList);
        productOptionList.forEach(productOption -> {
                if(productOption.getProduct() != this) {
                    productOption.setProduct(this);
                }
            }
        );
    }
}
