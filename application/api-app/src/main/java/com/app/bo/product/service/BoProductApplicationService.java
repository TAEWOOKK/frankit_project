package com.app.bo.product.service;

import com.app.bo.product.dto.ProductOptionDetailPostRequestDto;
import com.app.bo.product.dto.ProductOptionDetailPostResponseDto;
import com.app.bo.product.dto.ProductPostRequestDto;
import com.app.bo.product.dto.ProductPostResponseDto;
import com.app.domain.product.constant.ProductOptionType;
import com.app.domain.product.entity.Product;
import com.app.domain.product.entity.ProductOptionDetail;
import com.app.domain.product.service.ProductOptionDetailService;
import com.app.domain.product.service.ProductService;
import com.app.error.ErrorType;
import com.app.error.exception.BadRequestException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BoProductApplicationService {

    private final ProductOptionDetailService productOptionDetailService;
    private final ProductService productService;

    @Transactional
    public ProductPostResponseDto createProduct(ProductPostRequestDto request) {

        List<ProductOptionDetail> productOptionDetails = getProductOptions(request);
        // 상품 엔티티 생성
        Product product = request.toEntity(productOptionDetails);
        // 상품 저장
        Product savedProduct = productService.createProduct(product);
        return new ProductPostResponseDto(savedProduct.getProductId());
    }

    @Transactional
    public ProductOptionDetailPostResponseDto createProductOptionDetail(ProductOptionDetailPostRequestDto request) {

        // 상품 옵션 상세 중복 체크
        if (productOptionDetailService.existsByDetailName(request.getProductOptionDetailName())) {
            throw new BadRequestException(ErrorType.PRODUCT_OPTION_DETAIL_NAME_DUPLICATE);
        }
        // 상품 옵션 상세 엔티티 생성
        ProductOptionDetail productOptionDetail = request.toEntity();
        // 상품 옵션 상세 저장
        ProductOptionDetail savedProductOptionDetail =
                productOptionDetailService.createProductOptionDetail(productOptionDetail);
        return new ProductOptionDetailPostResponseDto(savedProductOptionDetail.getProductOptionDetailId());
    }

    private List<ProductOptionDetail> getProductOptions(ProductPostRequestDto request) {
        List<ProductPostRequestDto.ProductOptionPostRequestDto> optionList =
                Optional.ofNullable(request.getProductOptionList()).orElse(Collections.emptyList());

        if (optionList.isEmpty()) {
            return Collections.emptyList();
        }

        validProductOptions(optionList);

        List<Long> detailIds = optionList.stream()
                .map(ProductPostRequestDto.ProductOptionPostRequestDto::getProductOptionDetailId)
                .filter(Objects::nonNull)
                .toList();

        return detailIds.isEmpty() ? Collections.emptyList() : productOptionDetailService.findByIds(detailIds);
    }

    private void validProductOptions(List<ProductPostRequestDto.ProductOptionPostRequestDto> productOptionList) {
        if (productOptionList == null || productOptionList.isEmpty()) return;

        productOptionList.forEach(option -> {
            if (isBlank(option.getProductOptionName())) {
                throw new BadRequestException(ErrorType.PRODUCT_OPTION_NAME_NULL);
            }

            if (option.getProductOptionType() == ProductOptionType.SELECT && option.getProductOptionDetailId() == null) {
                throw new BadRequestException(ErrorType.PRODUCT_OPTION_SELECT_ID_NULL);
            }

            if (option.getProductOptionType() == ProductOptionType.INPUT && isBlank(option.getProductOptionDescription())) {
                throw new BadRequestException(ErrorType.PRODUCT_OPTION_INPUT_DESCRIPTION_NULL);
            }
        });
    }

    private boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }
}
