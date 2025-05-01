package com.app.domain.product.service;

import com.app.domain.product.repository.ProductOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductOptionService {

    private final ProductOptionRepository productOptionRepository;

    public Boolean existsByProductOptionDetailId(Long productOptionDetailId) {
        return productOptionRepository.existsByProductOptionDetail_ProductOptionDetailId(productOptionDetailId);
    }
}
