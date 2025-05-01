package com.app.bo.product.service;

import com.app.bo.product.dto.request.ProductGetRequestDto;
import com.app.bo.product.dto.request.ProductOptionDetailPostRequestDto;
import com.app.bo.product.dto.request.ProductPutRequestDto;
import com.app.bo.product.dto.response.*;
import com.app.bo.product.dto.request.ProductPostRequestDto;
import com.app.bo.product.repository.BoProductCustomRepository;
import com.app.common.dto.PageResponse;
import com.app.domain.product.constant.ProductOptionType;
import com.app.domain.product.entity.Product;
import com.app.domain.product.entity.ProductOption;
import com.app.domain.product.entity.ProductOptionDetail;
import com.app.domain.product.service.ProductOptionDetailService;
import com.app.domain.product.service.ProductOptionService;
import com.app.domain.product.service.ProductService;
import com.app.error.ErrorType;
import com.app.error.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BoProductApplicationService {

    private final ProductOptionDetailService productOptionDetailService;
    private final BoProductCustomRepository boProductCustomRepository;
    private final ProductService productService;
    private final ProductOptionService productOptionService;

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

        if(productOptionList.size() > 3) {
            throw new BadRequestException(ErrorType.PRODUCT_OPTION_MAX_COUNT);
        }

        productOptionList.forEach(option -> {
            if (isBlank(option.getProductOptionName())) {
                throw new BadRequestException(ErrorType.PRODUCT_OPTION_NAME_NULL);
            }

            if (option.getProductOptionType() == null) {
                throw new BadRequestException(ErrorType.PRODUCT_OPTION_TYPE_NULL);
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

    public PageResponse<ProductGetResponseDto> getProductListPages(ProductGetRequestDto request) {

        Page<ProductGetResponseDto> products = boProductCustomRepository.findByFilter(request);
        return new PageResponse<>(products);
    }

    @Transactional(readOnly = true)
    public ProductDetailGetResponseDto getProductDetail(Long productId) {

        Product product = productService.getProductById(productId);
        return ProductDetailGetResponseDto.toDto(product);
    }

    private void validUpdateProductOptions(List<ProductPutRequestDto.ProductOption> productOptionList) {
        if (productOptionList == null || productOptionList.isEmpty()) return;

        if(productOptionList.size() > 3) {
            throw new BadRequestException(ErrorType.PRODUCT_OPTION_MAX_COUNT);
        }

        productOptionList.forEach(option -> {
            if (isBlank(option.getProductOptionName())) {
                throw new BadRequestException(ErrorType.PRODUCT_OPTION_NAME_NULL);
            }
            if (option.getProductOptionType() == null) {
                throw new BadRequestException(ErrorType.PRODUCT_OPTION_TYPE_NULL);
            }

            if (option.getProductOptionType() == ProductOptionType.SELECT && option.getProductOptionDetailId() == null) {
                throw new BadRequestException(ErrorType.PRODUCT_OPTION_SELECT_ID_NULL);
            }

            if (option.getProductOptionType() == ProductOptionType.INPUT && isBlank(option.getProductOptionDescription())) {
                throw new BadRequestException(ErrorType.PRODUCT_OPTION_INPUT_DESCRIPTION_NULL);
            }
        });
    }

    @Transactional
    public ProductPutResponseDto updateProduct(Long productId, ProductPutRequestDto request) {
        Product product = productService.getProductById(productId);
        List<ProductPutRequestDto.ProductOption> optionList = request.getProductOptionList();

        validUpdateProductOptions(optionList);

        List<ProductOption> updatedOptions = buildProductOptions(optionList, product, product.getProductOptions());

        updateProductFields(product, request, updatedOptions);

        return new ProductPutResponseDto(product.getProductId());
    }

    private List<ProductOption> buildProductOptions(List<ProductPutRequestDto.ProductOption> optionList, Product product, List<ProductOption> existingOptions) {
        List<ProductOption> result = new ArrayList<>();
        if (optionList == null || optionList.isEmpty()) return result;

        List<Long> detailIds = optionList.stream()
                .map(ProductPutRequestDto.ProductOption::getProductOptionDetailId)
                .filter(Objects::nonNull)
                .toList();

        List<ProductOptionDetail> detailList = productOptionDetailService.findByIds(detailIds);

        for (ProductPutRequestDto.ProductOption dto : optionList) {
            ProductOptionDetail detail = findDetail(detailList, dto.getProductOptionDetailId());
            ProductOption option = (dto.getProductOptionId() == null)
                    ? createNewProductOption(dto, detail, product)
                    : updateExistingProductOption(dto, detail, existingOptions);
            result.add(option);
        }

        return result;
    }

    private ProductOptionDetail findDetail(List<ProductOptionDetail> details, Long id) {
        if (id == null) return null;
        return details.stream()
                .filter(d -> d.getProductOptionDetailId().equals(id))
                .findFirst()
                .orElseThrow(() -> new BadRequestException(ErrorType.PRODUCT_OPTION_DETAIL_NOT_FOUND));
    }

    private ProductOption createNewProductOption(ProductPutRequestDto.ProductOption dto, ProductOptionDetail detail, Product product) {
        return ProductOption.createProductOption(
                dto.getProductOptionName(),
                dto.getProductOptionType(),
                dto.getProductOptionDescription(),
                detail,
                product
        );
    }

    private ProductOption updateExistingProductOption(ProductPutRequestDto.ProductOption dto, ProductOptionDetail detail, List<ProductOption> existingOptions) {
        ProductOption option = existingOptions.stream()
                .filter(o -> o.getProductOptionId().equals(dto.getProductOptionId()))
                .findFirst()
                .orElseThrow(() -> new BadRequestException(ErrorType.PRODUCT_OPTION_NOT_FOUND));

        option.updateProductOption(
                dto.getProductOptionName(),
                dto.getProductOptionType(),
                dto.getProductOptionDescription(),
                detail
        );

        return option;
    }

    private void updateProductFields(Product product, ProductPutRequestDto request, List<ProductOption> options) {
        product.updateProduct(
                request.getProductName(),
                request.getProductDescription(),
                request.getProductPrice(),
                request.getDeliveryPrice(),
                options
        );
    }

    @Transactional
    public void deleteProduct(Long productId) {
        productService.deleteProductById(productId);
    }

    public void deleteProductOptionDetail(Long productOptionDetailId) {

        if(productOptionService.existsByProductOptionDetailId(productOptionDetailId)){
            throw new BadRequestException(ErrorType.PRODUCT_OPTION_DETAIL_USED);
        }

        productOptionDetailService.deleteProductOptionDetailById(productOptionDetailId);
    }
}
