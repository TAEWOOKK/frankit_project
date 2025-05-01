package com.app.domain.product.repository;

import com.app.domain.product.entity.ProductOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {

    Boolean existsByProductOptionDetail_ProductOptionDetailId(Long productOptionDetailProductOptionDetailId);
}
