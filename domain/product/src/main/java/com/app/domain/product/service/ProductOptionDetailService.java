package com.app.domain.product.service;

import com.app.domain.product.entity.ProductOptionDetail;
import com.app.domain.product.repository.ProductOptionDetailRepository;
import com.app.error.ErrorType;
import com.app.error.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductOptionDetailService {

    private final ProductOptionDetailRepository productOptionDetailRepository;

    public List<ProductOptionDetail> findByIds(List<Long> productOptionDetailIds) {
        List<ProductOptionDetail> productOptionDetails = productOptionDetailRepository.findAllByProductOptionDetailIdIn(productOptionDetailIds);

        if(productOptionDetailIds.size() != productOptionDetails.size()) {
            throw new BadRequestException(ErrorType.ANY_PRODUCT_OPTION_DETAIL_NOT_FOUND);
        }
        return productOptionDetails;
    }

    public Boolean existsByDetailName(String productOptionDetailName) {
        return productOptionDetailRepository.existsByProductOptionDetailName((productOptionDetailName));
    }

    public ProductOptionDetail createProductOptionDetail(ProductOptionDetail productOptionDetail) {
        return productOptionDetailRepository.save(productOptionDetail);
    }
}
