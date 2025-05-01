package com.app.domain.product.repository;

import com.app.domain.product.entity.ProductOptionDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface ProductOptionDetailRepository extends JpaRepository<ProductOptionDetail, Long> {

    List<ProductOptionDetail> findAllByProductOptionDetailIdIn(List<Long> productOptionDetailIds);

    Boolean existsByProductOptionDetailName(String productOptionDetailName);
}
